// Import the Express module
const express = require('express');

// Import the serve-static module
const serveStatic = require('serve-static');

// Import the 'path' module (packaged with Node.js)
const path = require('path');

// Create a new instance of Express
const app = express();

// Import the server file.
const serverFile = require('./server');

// Serve static html, js, css, and image files from the 'public' directory
app.use(serveStatic('public'));

// Create a Node.js based http server on port 8080
const server = require('http').createServer(app).listen(8080);

// Create a Socket.IO server and attach it to the http server
const io = require('socket.io').listen(server);

app.get('/', function(req, res){
    res.sendFile(__dirname + '/public/index.html');
});

// Listen for Socket.IO Connections. Once connected, start the game logic.
io.sockets.on('connection', function (socket) {
    console.log('client connected');
    serverFile.init(io, socket);
});



// var app = require('express')();
// var http = require('http').Server(app);
// var io = require('socket.io')(http);
//
// app.get('/', function(req, res){
//     res.sendFile(__dirname + '/index.html');
// });
//
// io.on('connection', function(socket){
//     console.log('a user connected');
//     socket.on('disconnect', function(){
//         console.log('user disconnected');
//     });
//
//     socket.on('chat message', function(msg){
//         socket.emit('chat message', msg);
//     });
// });
//
// http.listen(8080, function(){
//     console.log('listening on *:8080');
// });