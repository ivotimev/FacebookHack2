var is_string = function(s) {
    return typeof s === 'string' || s instanceof String;
}

module.exports = {
    "is_string": is_string
}