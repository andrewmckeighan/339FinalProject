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

var fullqak;

//for each key there is also a list of numbers, which are answers to questions.
var akCombo;

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
	var qak;
	for(var i = 0; i < keys.length; i ++){
		if(keys[i] == session){
			qak.session = session;
			qak.Question = Question;
			qak.Answers = Answers;
			foundKey = true;
			break;
		}
	}
	if(!foundKey){
		console.log('Key '+key+' not recognized');
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
		var qa;
	for(var i = 0; i < fullqak.length; i++){
		if(fullqa[i].session == key){
			return fullqa[i];
		}
	}
	return null;
		//find which QA we need for this key given.
		//emit that to the user.
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
        setQA(data.session, data.Question, data.Answers);
		socket.emit('submitConf', true);
    });
	
    socket.on('getQA', function(data){
        getQA(data);
		io.to(data.session).emit(data.Question, data.Answers);
    });
	
	socket.on('close', function(data){
		socket.close(data);
		//gather the data that will be sent back to the desktop.
		//results.session, results.conf, results.results
		socket.emit('sendResults', results);
	});
	
	socket.on('sendA', function(data){
		
	});
	//Android user sends key.
	//Push questions to android user.
	//Set up rooms.
});