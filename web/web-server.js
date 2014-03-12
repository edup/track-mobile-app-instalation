/*************
You need to have different approachs for android and iphone
For iphone is simpler, just check the iphone.js file
For android you need to be connected to a place (server) where the browser is constantly receiving instructions
***********/

var express = require('express');
var server = express();

//WebServer definition
server.configure(function() {

    server.use('/config.js', function(req, res) {
        res.sendfile(__dirname + '/config.js');
    });
    server.use('/android.js', function(req, res) {
        console.log("Loaded android script");
        res.sendfile(__dirname + '/public/android.js');
    });
    server.use('/iphone.js', function(req, res) {
        console.log("Loaded iphone script");
        res.sendfile(__dirname + '/public/iphone.js');
    });
    server.use(express.static(__dirname + '/public'));
});

var port = process.env.PORT || 7000;
server.listen(port, function() {
    console.log("Listening on " + port);
});


//You need to connect to a middleware
//because, in the client side, when the browser is not focus
//it can receive instructions
//we choose muzzley middleware to do it
var muzzley = require('muzzley-client');

//You should change this config file according to your configurations in http://www.muzzley.com site
var config = require('./config');

var options = {
    token: config.muzzley.token,
    activityId: config.muzzley.activity
};


muzzley.connectApp(options, function(err, activity) {
    if (err) return console.log("err: " + err);

    // Usually you'll want to show this Activity's QR code image
    // or its id so that muzzley users can join.
    // They are in the `activity.qrCodeUrl` and `activity.activityId`
    // properties respectively.
    //console.log(activity);

    activity.on('participantJoin', function(participant) {
        console.log("participantJoin: " + participant.id);

        var sendMsg = function() {
            participant.sendSignal("try-again", {});
            //console.log("Sending message to participant: ");
        };
        sendMsg();

        participant.on('signalingMessage', function(type, obj, callback) {
            //console.log("ID: " + participant.id + ", " + type);
            //console.log("Receveid msg: " + type);
            if (type == 'error-connecting') {
                setTimeout(sendMsg, 1000);
            }
        });
        participant.on('quit', function() {
            console.log('quit');
        });

    });
});