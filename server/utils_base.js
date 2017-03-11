var is_string = function(s) {
    return typeof s === 'string' || s instanceof String;
}

var exit_nodejs = function(code) {
    setTimeout(function() {
        process.exit(code);
    }, 3000);
}

module.exports = {
    "is_string": is_string,
    "exit_nodejs": exit_nodejs
}