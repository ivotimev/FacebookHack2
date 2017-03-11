var randomstring = require("randomstring");

var generate_token = function() {
    return randomstring.generate(30);
}

var user_token = {};

var token_user = {};

var make_token = function(email) {
    var existing_token = user_token[email];
    if (existing_token) {
        delete token_user[existing_token];
        delete user_state[email];
    }
    
    var new_token = generate_token();
    
    user_token[email] = new_token;
    token_user[new_token] = email;
    
    console.log('There are ' + Object.keys(token_user).length + ' users logged in');
    
    return new_token;
};

// Takes a token, returns a username if found any
var validate_token = function(token) {
    return token_user[token];
}

module.exports = {
    make_token: make_token,
    validate_token: validate_token
};