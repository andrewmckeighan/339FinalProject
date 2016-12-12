/**
 * Created by mrjoshte on 12/1/2016.
 */

var express = require('express');
var sockets = require('socket.io');

var app = express();
var io = sockets();
var fs = require('fs');
var server;
var port = 6668;
//For each key, there is a qak -> question answer key object.
var keys = [];

var liveSessions = {'key':[]};

var fullqak = [];

var answers = {};

//---------------------Functions--------------------------
//Generates and saves a random key used for authentication
var generatekey = function(){
	console.log("Generating key");
    var text = "";
    var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    for( var i=0; i < 5; i++ )
        text += possible.charAt(Math.floor(Math.random() * possible.length));
	keys.push(text);
    fs.writeFile('keys.json', JSON.stringify(keys, null, 4), function(err){
        if(err) {
             console.log(err);
        } else{
			console.log("New key created.");
            //getKeys();

        }
    });
    return text;
};

var getKeys = function(){
	console.log("Getting keys");
    fs.readFile('keys.json', function(err, data){
        if(err){
            console.log(err);
        } else{
			debugger;
            keys = JSON.parse(data);
			console.log(keys);
        }
        //Handles callback if function is being called on server startup
    });
};

//key and question need to be strings, answers needs to be a string array
var setQA = function(session, Question, Answers){
	var foundKey = false;
	//qak is Question,Answer,Key combination
	var qak = {};
	for(var i = 0; i < keys.length; i ++){
		if(sessionEquals(keys[i], session)){
			qak.session = session;
			qak.Question = Question;
			qak.Answers = Answers;
			foundKey = true;
			break;
		}
	}
	if(!foundKey){
		console.log('Key '+session+' not recognized');
		return false;
	}
	else{
		fs.readFile('qak.json', function(err, data){
			if(err){
				console.log(err);
			} else{
				fullqak = JSON.parse(data);
			}
		});
		fullqak.push(qak);
		fs.writeFile('qak.json', JSON.stringify(fullqak, null, 4), function(err){
			if(err) {
				 console.log(err);
			}
		});
		return true;
	}
};

var getQA = function(key){
	fs.readFile('qak.json', function(err, data){
			if(err){
				console.log(err);
			} else{
				fullqak = JSON.parse(data);
			}
		});
	for(var i = 0; i < fullqak.length; i++){
		if(sessionEquals(fullqak[i].session, key)){
			return JSON.stringify(fullqak[i]);
		}
	}
	return null;
		//find which QA we need for this key given.
		//emit that to the user.
}

var enterRoom = function(data, socket){
	//if the session for that room is live, send them the Q&A
		var joined = false;
		var validKey = false;
        var toReturn = 0;
		//Search through all the keys to make sure the current key is valid.
		for(var j = 0; j < keys.length; j++){
			if(sessionEquals(keys[j], data.session)){
				validKey = true;
                socket.emit('keyconf', "true");
				break;
			}
		}
		//Searching through all the live session keys to see if this key session is live yet..
		if(validKey){
			for(var i = 0; i < liveSessions.key.length; i++){
				if(sessionEquals(liveSessions.key[i], data.session)){
					socket.join(data.session);
					joined = true;
					break;
				}
			}
			if(joined){
				var qa = getQA(data.session);
                console.log(qa + " ----------------------------------");
                socket.emit('sendQA', qa);
			}else{
				socket.emit('notSuccessful', false);
			}
		}
		else{
			socket.emit('keyconf', "false");
		}
}

//Handles the initial server setup before starting
var initializeServer = function(startServer) {
            startServer();
};

//---------------------Server Start--------------------------
(function(){
    //Link required startup methods
    //var functions = [getKeys, getQA];
    //What to do once initialization finishes
    //var start = function(){
        //security.init({'maxClientConnections': maxClientConnections, 'debounceThreshold': debounceThreshold});
		debugger;
        //Starts the Express server
        server = app.listen(port, function () {
            //Server started
            console.log('Web server running on port ' + port);

            //Start socket server
            io.listen(server);
            console.log('Socket server running on port ' + port);

        });
   // };
    //initializeServer();
		console.log("key time");
			getKeys();
			//generatekey();
})();

//---------------------Websocket stuff--------------------------
//Socket routes
io.on('error', function(socket){
	console.log("There was an error.");
});
io.on('connection', function (socket){

	console.log("New Connection");
	socket.on('getKey', function(){
		var text = generatekey();
		socket.join(text);
		console.log("Key:"+text);
		var json = {"session":text};

        socket.emit('sendKey', JSON.stringify(json, null, 4));
    });

	socket.on('submitQA', function(data){
		console.log(typeof data);
		console.log(data);
		liveSessions.key.push(data.session);
        var toret = setQA(data.session, data.Question, data.Answers);
		if(toret){
			socket.emit('submitConf', JSON.stringify({"conf":"true"}));
			io.to(data.session).emit('sendQA', data);
		}else{
			socket.emit('submitConf', JSON.stringify({"conf":"false"}));
		}
    });

	socket.on('enterRoom', function(data){
        console.log("connected...")
		enterRoom(data, socket);
	});

	socket.on('answerQA', function(data){
        console.log("session-");
        var ans = data.Answer + 1;
        var sessionAnswers = answers[data.session.toUpperCase()];
        if(!sessionAnswers) {
            sessionAnswers = answers[data.session.toUpperCase()] = {};
        }
        if(isNaN(sessionAnswers[ans+""])){
            sessionAnswers[ans+""] = 0;
        }
		sessionAnswers[ans+""]++;
        console.log(sessionAnswers);
		//Logic here in order to answer a question.
	});

	socket.on('close', function(data){
		//gather the data that will be sent back to the desktop.
		//results.session, results.conf, results.results
        console.log(answers);
        console.log(data);
        console.log(answers[data.session.toUpperCase()]);
		socket.emit('sendResults', JSON.stringify({answers:answers[data.session.toUpperCase()]}, null, 4));
        socket.leave(data.session);
        console.log('close');
        //remove the key from liveSessions.
	});
});

io.on("disconnect", function(){
    console.log("disconnect");
});

function sessionEquals(session1, session2){
    if(!session1 || !session2){
        return false;
    }
    return session1.toUpperCase() == session2.toUpperCase();
}
