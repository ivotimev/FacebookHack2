// #############################################################################
// DATABASE PART

var Datastore = require('nedb');
var path = require('path');

var db = {};

// USERS
// {
//     nickname
//     is_admin?
// }
db.users = new Datastore({
    filename:  __dirname + '/../db/users.db',
    autoload: true});
db.users.persistence.setAutocompactionInterval(3600 * 1000);

// MOVIES
// {
//     title
// }
db.movies = new Datastore({
    filename:  __dirname + '/../db/movies.db',
    autoload: true});
db.movies.persistence.setAutocompactionInterval(3600 * 1000);

// USER_MOVIE
// {
//     nickname
//     movieid
// }
db.user_movie = new Datastore({
    filename:  __dirname + '/../db/user_movie.db',
    autoload: true});
db.user_movie.persistence.setAutocompactionInterval(3600 * 1000);

// Callbacks a boolean that tells whether db.users is empty or not
db.is_empty = function(callback) {
    db.users.count({}, function(err, count) {
        if (err) {
            callback(err);
            return;
        }
        var empty = (count == 0);
        callback(null, empty);
        return;
    });
}

module.exports = db;