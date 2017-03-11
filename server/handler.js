var Set = require('Set');
var utils_base = require(__dirname + "/utils_base.js");
var socketio = require(__dirname + "/socketio.js");
var session = require(__dirname + "/session.js");
var db = require(__dirname + "/db.js");
var async = require("async");
var omdb = require('omdb');
 
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
                var imdbid = doc["imdbid"];
                console.log("user likes " + imdbid);
                db.movies.find({
                    "imdbid": imdbid
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
                        console.log("no movie found for id " + imdbid);
                        callback(null);
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
    
    var imdbid = msgobj["imdbid"];
    if (imdbid && !utils_base.is_string(imdbid)) {
        var resp = {};
        resp["type"] = "like_movie_status";
        resp["status"] = "fail";
        socketio.send(client, JSON.stringify(resp));
        callback("movie id not a string");
        return;
    }

    async.waterfall([
    
        // get moves from imdb given title
        function(callback) {
            if (imdbid) {
                callback(null, imdbid);
                return;
            } else {
                omdb.search(movie_title, function(err, movies) {
                    if(err) {
                        return console.error(err);
                    }
                    
                    // no movies found, error
                    if (movies.length < 1) {
                        callback("no movies find with this title");
                        return;
                    }
                    
                    // only one movie found, proceeding
                    if (movies.length == 1) {
                        var movie = movies[0];
                        var imdbid = movie.imdb;
                        callback(null, imdbid);
                        return;
                    }
                    
                    // send user a list of choices to make
                    if (movies.length > 1) {

                        var resp = {};
                        resp["type"] = "like_movie_status";
                        resp["status"] = "choose";
                        
                        var choose = [];
                        
                        for (var i = 0; i < movies.length; i++) {
                            var m = movies[i];
                            var chooseobj = {};
                            chooseobj["imdbid"] = m["imdb"];
                            chooseobj["poster"] = m["poster"];
                            chooseobj["title"] = m["title"];
                            choose.push(chooseobj);
                        }
                        
                        resp["choose"] = choose;
                        
                        socketio.send(client, JSON.stringify(resp));
                        
                        callback("void");
                    }

                });
            }
        },
    
        // check if database has this movie already
        function(imdbid, callback) {
            db.movies.find({
                "imdbid": imdbid
            }, function(err, docs) {
                if (err) {
                    callback(err);
                    return;
                }
                var movie_exists = docs.length > 0;
                callback(null, movie_exists, imdbid);
                return;
            });
        },
        
        // add movie to database if necessary
        function(movie_exists, imdbid, callback) {
            if (movie_exists) {
                callback(null, imdbid);
                return;
            }
            
            omdb.get({ imdb: imdbid }, true, function(err, movie) {
                if (err) {
                    callback(err);
                    return;
                }
                var title = movie.title;
                var posterimg = movie.poster;
                var year = movie.year;
                
                var newdoc = {
                    "imdbid": imdbid,
                    "title": title,
                    "posterimg": posterimg,
                    "year": year
                };
                db.movies.insert(newdoc, function(err, ndoc) {
                    if (err) {
                        callback(err);
                        return;
                    }
                    console.log("inserted new doc: " + JSON.stringify(ndoc));
                    callback(null, imdbid);
                    return;
                });
            });

        },
        
        // check if the binding already exists
        function(imdbid, callback) {

            db.user_movie.find({
                "nickname": username,
                "imdbid": imdbid
            }, function(err, docs) {
                if (err) {
                    callback(err);
                    return;
                }
                if (docs.length > 0) {
                    callback("binding already exists for this user and movie " + username);
                    return;
                } else {
                    callback(null, username, imdbid);
                }
            });

        },
        
        // add binding user-movie
        function(username, imdbid, callback) {

            var newdoc = {};
            newdoc["nickname"] = username;
            newdoc["imdbid"] = imdbid;
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
            if (err == "void") {
                callback(null);
                return;
            }
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