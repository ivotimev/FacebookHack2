var Set = require('Set');
var utils_base = require(__dirname + "/utils_base.js");
var socketio = require(__dirname + "/socketio.js");
var session = require(__dirname + "/session.js");
var db = require(__dirname + "/db.js");
 
var messages_unauthenticated = new Set([
    "login"
]);

var messages_authenticated = new Set([
    "my_movies"
]);

// =============================================================================
var handle = function(client, msgobj) {
    var type = msgobj["type"];
    if (!type || !utils_base.is_string(type)) {
        console.log("no valid type field");
        return;
    }
    if (messages_unauthenticated.contains(msgobj)) {
        handle_unauthenticated_msg(client, msgobj);
    } else if (messages_authenticated.contains(msgobj)) {
        handle_authenticated_msg(client, msgobj);
    } else if (messages_elevated.contains(msgobj)) {
        handle_elevated_msg(client, msgobj);
    } else {
        console.log("unrecognized message!");
    }
}

var handle_unauthenticated_msg = function(client, msgobj) {
    var type = msgobj["type"];
    
    if (type == "login") {
        handle_login(client, msgobj, function(err, res) {
            if (err) {
                console.log(err);
                return;
            }
            if (res) {
                console.log(res);
                return;
            }
        });
    }
}

// =============================================================================

var handle_login = function(client, msgobj, callback) {
    console.log('handling login');
    // NOTE this login function doesn't authenticate, it always allows to login successfully
    var username = msgobj["username"];
    var password = msgobj["password"];
    
    var token = session.make_token(username);
    
    var resp = {
        "type": "login_success",
        "token": token,
        "user_type": "normal"
    }
    socketio.send(client, JSON.stringify(resp));
};

module.exports = {
    "handle": handle
};