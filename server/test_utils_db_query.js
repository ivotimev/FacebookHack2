var utils_db_query = require(__dirname + "/utils_db_query.js");
var utils_base = require(__dirname + "/utils_base.js");

utils_db_query.get_user_movies("gj", "like", function(err, movies) {
    if (err) {
        console.log(err);
    } else {
        console.log(movies);
    }
    utils_base.exit_nodejs(0);
});