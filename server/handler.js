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

// First order handlers
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
        handle_login(client, msgobj, generic_handler_callback);
    }
}



var handle_authenticated_msg = function(client, msgobj) {
    var type = msgobj["type"];
    
    var token = msgobj["token"];
    if (!token || !utils_base.is_string(token)) {
        console.log("no token field");
        return;
    }
    
    var username = session.validate_token(token);
    if (!username) {
        console.log("no valid token");
        var resp = {
            "type": "invalid_token"
        };
        socketio.send(client, JSON.stringify(resp));
        return;
    }
    
    if (type == "my_movies") {
        handle_my_movies(username, client, msgobj, generic_handler_callback);
    }
}

// Unauthenticated handlers
// =============================================================================

var handle_login = function(client, msgobj, callback) {
    console.log('handling login');
    // NOTE this login function doesn't authenticate, it always allows to login successfully
    
    var username = msgobj["username"];
    if (!username || !utils_base.is_string(username)) {
        console.log("no username");
        var resp = {
            "type": "login_fail"
        };
        socketio.send(client, JSON.stringify(resp));
        return;
    }
    
    var password = msgobj["password"];
    if (!password || !utils_base.is_string(password)) {
        console.log("no password");
        var resp = {
            "type": "login_fail"
        };
        socketio.send(client, JSON.stringify(resp));
        return;
    }
    
    var token = session.make_token(username);
    
    var resp = {
        "type": "login_success",
        "token": token,
        "user_type": "normal"
    }
    socketio.send(client, JSON.stringify(resp));
};

// Authenticated handlers
// =============================================================================

var handle_my_movies = function(username, client, msgobj, callback) {
    async.waterfall([
    
        // query database for movies
        function(callback) {
            db.user_movie.find({
                "nickname": username
            }, function(err, docs) {
                if (err) {
                    callback(err);
                    return;
                }
                callback(null, docs);
            });
        },
        
        // application-level join for the titles
        function(docs, callback) {
            var movie_titles = [];
            async.each(docs, function(doc, callback) {
                var movieid = doc["movieid"];
                db.movies.find({
                    "_id": movieid
                }, function(err, docs) {
                    if (err) {
                        callback(err);
                        return;
                    }
                    if (docs.length == 1) {
                        var doc = docs[0];
                        var title = doc["title"];
                        movie_titles.push(title);
                        callback(null);
                        return;
                    } else {
                        callback("expected only 1 result, instead " + docs.length);
                        return;
                    }
                });
            }, function(err) {
                if (err) {
                    callback(err);
                    return;
                }
                callback(null, movie_titles);
            });
        },
        
        // send response
        function(movie_titles, callback) {

            var resp = {};
            resp["type"] = "my_movies";
            
            var movies = [];
            for (var i = 0; i < movie_titles.length; i++) {
                var movieobj = {};
                movieobj.title = movie_titles[i];
                movies.push(movieobj);
            }
            resp["movies"] = movies;
            
            socketio.send(client, JSON.stringify(resp));
        }
    
    
    ], function(err, res) {
        if (err) {
            console.log(err);
            return;
        }
    });
}

// Helpers
// =============================================================================
var generic_handler_callback = function(err, res) {
    if (err) {
        console.log(err);
        return;
    }
    if (res) {
        console.log(res);
        return;
    }
}

module.exports = {
    "handle": handle
};