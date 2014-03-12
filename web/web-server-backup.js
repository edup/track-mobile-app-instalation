/*************
 
 ***********/

var express = require('express');
var server = express();

//WebServer definition
server.configure(function() {
    server.use(express.static(__dirname + '/public'));
    server.use('/config.js', function(req, res) {
        var ip = req.headers['x-forwarded-for'] || req.connection.remoteAddress;
        console.log("Hello from: " + ip);
        res.sendfile(__dirname + '/config.js');
    });
});



var port = process.env.PORT || 7000;
server.listen(port, function() {
    console.log("Listening on " + port);
});