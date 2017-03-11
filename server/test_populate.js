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
        }
        
        var like_msg = {
            "type": "like_movie",
            "token": token,
            "movie_title": "Facebook"
        };
        handler.handle(client, like_msg);
    }
    
    // get list of liked movies

], function(err, res) {
    if (err) {
        console.log(err);
        return;
    } 
    if (res) {
        console.log(res);
        return;
    }
});