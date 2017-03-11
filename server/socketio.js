var server = require('http').createServer();
var handler = require(__dirname + '/handler.js');

var io = require('socket.io')(server);



io.on('connection', function(client){
    client.on('event', function(data){
        console.log("event. data:");
        console.log(data);
    });
    client.on('disconnect', function(){
        console.log("Client disconnected");
    });
});
server.listen(22333);