var handler = require(__dirname + "/handler.js");
var async = require("async");

//         {
//             "type": "login",
//             "username": "me",
//             "password": "pass"
//         }

var test_liked_movie = "Star Wars";

async.waterfall([

    // send message for login
    function(callback) {
        var client = {};
        
        var login_msg = {
            "type": "login",
            "username": "gj",
            "password": "password"
        };
        handler.handle(client, login_msg);
    }
    
    // send message for add a new liked movie
    
    // get list of liked movies

], function(err, res) {
    if (err) {
        console.log(err);
    } else {
        console.log(res);
    }
});