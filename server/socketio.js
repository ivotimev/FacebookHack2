var server = require('http').createServer();
var handler = require(__dirname + '/handler.js');

var io = require('socket.io')(server);


io.on('connection', function(client){
    client.on('event', function(data){
        console.log("event. data:");
        console.log(data);
        var msgobj = JSON.parse(data);
        handler.handle(client, msgobj);
    });
    client.on('disconnect', function(){
        console.log("Client disconnected");
    });
});
server.listen(22333);

var send = function(client, data) {
    client.emit("message", data);
}

module.exports = {
    "send": send
}