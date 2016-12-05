/**
 * Created by mrjoshte on 12/1/2016.
 */
 
var express = require('express');
var sockets = require('socket.io');

var app = express();
var io = sockets();
var fs = require('fs');
var server;
var port = 1337;
//For each key, there is a qak -> question answer key object.
var keys[];

var fullqak[];

//for each key there is also a list of numbers, which are answers to questions.
var akCombo;

//---------------------Functions--------------------------
//Generates and saves a random key used for authentication
var generatekey = function(){
    var text = "";
    var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    for( var i=0; i < 5; i++ )
        text += possible.charAt(Math.floor(Math.random() * possible.length));
	
    fs.writeFile('keys.json', JSON.stringify(text, null, 4), function(err){
        if(err) {
             console.log(err);
        } else{
            getKeys();
        }
    });
    return text;
};

var getKeys = function(startupCallback){
    fs.readFile('keys.json', function(err, data){
        if(err){
            console.log(err);
        } else{
            keys = JSON.parse(data);
        }
        //Handles callback if function is being called on server startup
        if(startupCallback){ startupCallback() }
    });
};

//key and question need to be strings, answers needs to be a string array
var setQA = function(key, question, answers){
	boolean foundKey = false;
	//qak is Question,Answer,Key combination
	var qak;
	for(int i = 0; i < keys.length; i ++){
		if(keys[i] == key){
			qak.key = key;
			qak.question = question;
			qak.answers = answers;
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
			//Handles callback if function is being called on server startup
			if(startupCallback){
				startupCallback()
			}
		});
		fullqak += qak;
		fs.writeFile('qak.json', JSON.stringify(fullqak, null, 4), function(err){
			if(err) {
				 console.log(err);
			}
		});
	}
}

var getQA = function(key){
	fs.readFile('qak.json', function(err, data){
			if(err){
				console.log(err);
			} else{
				fullqak = JSON.parse(data);
			}
			//Handles callback if function is being called on server startup
			if(startupCallback){
				startupCallback()
			}
		});
}


//---------------------Server Start--------------------------
(function(){
    //Link required startup methods
    var functions = [getkeys, getQA];

    //What to do once initialization finishes
    var start = function(){
        security.init({'maxClientConnections': maxClientConnections, 'debounceThreshold': debounceThreshold});

        //Starts the Express server
        server = app.listen(port, function () {
            //Server started
            console.log('Web server running on port ' + port);

            //Start socket server
            io.listen(server);
            console.log('Socket server running on port ' + port);
        });
    };
    initializeServer(functions, start);
})();

//---------------------Websocket stuff--------------------------
//Socket routes
io.on('connection', function (socket) {	
	
	socket.on('getKey', function(){
        socket.emit('sendKey', generatekey());
    });
	
	socket.on('submitQA', function(data){
        setQA(data.key, data.question, data.answers);
    });
	
    socket.on('getQA', function(data){
        getQA(data);
    });
	//Android user sends key.
	//Push questions to android user.
	//Set up rooms.
}