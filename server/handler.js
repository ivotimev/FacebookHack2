var Set = require('Set');
var utils_base = require(__dirname + "/utils_base.js");
var socketio = require(__dirname + "/socketio.js");
var session = require(__dirname + "/session.js");
var db = require(__dirname + "/db.js");
var async = require("async");
 
var messages_unauthenticated = new Set([
    "login"
]);

var messages_authenticated = new Set([
    "my_movies",
    "like_movie"
]);

var messages_elevated = new Set([

]);

// First order handlers
// =============================================================================
var handle = function(client, msgobj) {
    var type = msgobj["type"];
    if (!type || !utils_base.is_string(type)) {
        console.log("no valid type field");
        return;
    }
    if (messages_unauthenticated.contains(type)) {
        handle_unauthenticated_msg(client, msgobj);
    } else if (messages_authenticated.contains(type)) {
        handle_authenticated_msg(client, msgobj);
    } else if (messages_elevated.contains(type)) {
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
    } else if (type == "like_movie") {
        handle_like_movie(username, client, msgobj, generic_handler_callback);
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
        callback("login fail due to no username");
        return;
    }
    
    var password = msgobj["password"];
    if (!password || !utils_base.is_string(password)) {
        console.log("no password");
        var resp = {
            "type": "login_fail"
        };
        socketio.send(client, JSON.stringify(resp));
        callback("login fail due to no password");
        return;
    }
    
    async.waterfall([
        
        // check if database has the user
        function(callback) {
            db.users.find({
                "nickname": username
            }, function(err, docs) {
                if (err) {
                    callback(err);
                    return;
                }
                var user_exists = docs.length == 1;
                callback(null, user_exists);
            });
        },
        
        // insert user in database if necessary
        function(user_exists, callback) {
            if (user_exists) {
                callback(null);
                return;
            } else {
                add_new_user(username, callback); // callback is here
                return;
            }
        },
        
        // send response to user
        function(callback) {
            var token = session.make_token(username);
            
            var resp = {
                "type": "login_success",
                "token": token,
                "user_type": "normal"
            }
            socketio.send(client, JSON.stringify(resp));
        }
        
    ], callback);

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
    
    
    ], callback);
}

var handle_like_movie = function(username, client, msgobj, callback) {

    var movie_title = msgobj["movie_title"];
    if (!movie_title || !utils_base.is_string(movie_title)) {
        var resp = {};
        resp["type"] = "like_movie_status";
        resp["status"] = "fail";
        socketio.send(client, JSON.stringify(resp));
        callback("movie title not found or not a string");
        return;
    }

    async.waterfall([
    
        // check if database already has this movie
        function(callback) {
            db.movies.find({
                "title": movie_title
            }, function(err, docs) {
                if (err) {
                    callback(err);
                    return;
                }
                var movie_exists = docs.length > 0;
                callback(null, movie_exists);
                return;
            });
        },
        
        // add the movie to database if necessary
        function(movie_exists, callback) {
            if (movie_exists) {
                callback(null);
                return
            } else {
                var newdoc = {};
                newdoc["title"] = movie_title;
                db.movies.insert(newdoc, function(err, ndoc) {
                    if (err) {
                        callback(err);
                        return;
                    }
                    console.log("successfully inserted: " + JSON.stringify(ndoc));
                    callback(null);
                    return;
                });
            }
        },
        
        // get the movies from database corresponding to title
        function(callback) {
            db.movies.find({
                "title": movie_title
            }, function(err, docs) {
                if (err) {
                    callback(err);
                    return;
                } else {
                    if (docs.length == 0) {
                        callback("no movies found? this should be impossible because movies that are not there are added before this step");
                        return;
                    } else {
                        var movie_ids = [];
                        for (var i = 0; i < docs.length; i++) {
                            var doc = docs[i];
                            var the_id = doc["_id"];
                            movie_ids.push(the_id);
                        }
                        callback(null, movie_ids);
                    }
                }
            });
        },
        
        // check if the binding already exists
        function(movie_ids, callback) {
            if (movie_ids.length < 1) {
                callback("this should be impossible! add binidng user-movie step");
                return;
            } if (movie_ids.length > 1) {
                console.log("More than 1 movies matching with the same title. ONLY getting the first one!");
            }
            var the_movie_id = movie_ids[0];
            
            db.user_movie.find({
                "nickname": username,
                "movieid": the_movie_id
            }, function(err, docs) {
                if (err) {
                    callback(err);
                    return;
                }
                if (docs.length > 0) {
                    callback("binding already exists for this user and movie " + username + " " + movie_title);
                    return;
                } else {
                    callback(null, username, the_movie_id);
                }
            });

        },
        
        // add binding user-movie
        function(username, the_movie_id, callback) {

            var newdoc = {};
            newdoc["nickname"] = username;
            newdoc["movieid"] = the_movie_id;
            db.user_movie.insert(newdoc, function(err, ndoc) {
                if (err) {
                    callback(err);
                    return;
                } else {
                    console.log("inserted doc: " + JSON.stringify(ndoc));
                    callback(null);
                    return;
                }
            });
        },
        
        // send user feedback
        function(callback) {
            var resp = {};
            resp["type"] = "like_movie_status";
            resp["status"] = "success";
            socketio.send(client, JSON.stringify(resp));
            callback(null);
        }
    
    ], function(err) {
        if (err) {
            var resp = {};
            resp["type"] = "like_movie_status";
            resp["status"] = "fail";
            socketio.send(client, JSON.stringify(resp));
            callback(err);
        } else {
            callback(null);
        }
    });
}

// Helpers: operations
// =============================================================================
var add_new_user = function(username, callback) {
    var newdoc = {};
    newdoc["nickname"] = username;
    db.users.insert(newdoc, function(err, ndoc) {
        if (err) {
            callback(err);
            return;
        } else {
            console.log("Successfully inserted: " + JSON.stringify(ndoc));
            callback(null);
            return;
        }
    });
}

// Helpers: basics
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

// =============================================================================
module.exports = {
    "handle": handle
};