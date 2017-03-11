var db = require(__dirname + "/db.js");
var async = require("async");

// Usage:
// get_user_movies("bob", "like", function(err, movies) {
//     if (err) {
//         // handle error
//     } else {
//         console.log(movies);
//     }
// });
var get_user_movies = function(username, movie_status, callback) {
    async.waterfall([
    
        // selected movie ids of the user
        function(callback) {
            db.user_movie.find({
                "nickname": username,
                "action": movie_status
            }, function(err, docs) {
                if (err) {
                    callback(err);
                    return;
                }
                callback(err, docs);
                return;
            });
        },
        
        // get the full movie objects from database movies
        function(docs, callback) {
            var selected_movies = [];
            
            async.each(docs, function(doc, callback) {
                var imdbid = doc.imdbid;
                // get more movie info about this
                db.movies.find({
                    "imdbid": imdbid
                }, function(err, docs) {
                    if (err) {
                        callback(err);
                        return;
                    }
                    if (docs.length == 0) {
                        console.log("no movies found for imdbid " + imdbid);
                        callback(null);
                        return;
                    }
                    if (docs.length > 1) {
                        console.log("duplicated movies found for imdbid " + imdbid);
                        callback(null);
                        return;
                    }
                    var d = docs[0];
                    selected_movies.push(d);
                    callback(null);
                    return;
                });
            }, function(err) {
                if (err) {
                    callback(err);
                    return;
                } else {
                    callback(null, selected_movies);
                }
            });
        }
        
    ], callback);

}

module.exports = {
    "get_user_movies": get_user_movies
}