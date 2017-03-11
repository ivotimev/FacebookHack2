var handler = require(__dirname + "/handler.js");
var async = require("async");

var test_liked_movie = "Star Wars";

async.waterfall([

    // send message for login
    function(callback) {
        var client = {};
        client.emit = function(emittype, data) {
            console.log("Client data: " + data);
            var dobj = JSON.parse(data);
            var token = dobj.token;
            callback(null, token);
        }
        
        var login_msg = {
            "type": "login",
            "username": "gj",
            "password": "password"
        };
        handler.handle(client, login_msg);
    },
    
    // send message for add a new liked movie
    function(token, callback) {
        var client = {};
        client.emit = function(emittype, data) {
            console.log("Client data: " + data);
            var dobj = JSON.parse(data);
            callback(null, dobj, token);
        }
        
        var like_msg = {
            "type": "like_movie",
            "token": token,
            "movie_title": "Facebook"
        };
        handler.handle(client, like_msg);
    },
    
    // if it's a choose, choose a movie
    function(dobj, token, callback) {
        var client = {};
        client.emit = function(emittype, data) {
            console.log("Client data: " + data);
            callback(null, token);
        }
        
        if (dobj.type == "like_movie_status" && dobj.status == "choose") {
            var like_msg = {
                "type": "like_movie",
                "token": token,
                "movie_title": "Status Update: A Facebook Fairytale",
                "imdbid": "tt2222428"
            };
            handler.handle(client, like_msg);
        } else {
            callback("Unexpected state!");
        }
    },
    
    // get list of liked movies
    function(token, callback) {
        var client = {};
        client.emit = function(emittype, data) {
            console.log("Client data: " + data);
            callback(null);
        }

        var movies_msg = {
            "type": "my_movies",
            "token": token
        };
        handler.handle(client, movies_msg);
    }

], function(err, res) {
    if (err) {
        console.log(err);
        process.exit(1);
    } 
    if (res) {
        console.log(res);
    }
    process.exit(0);
});