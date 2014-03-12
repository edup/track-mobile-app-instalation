var muzzleyConn;
var app_verified = false;
var websocket;

function init() {
    output = document.getElementById("output");
    document.getElementById("trackingId").innerHTML = trackingId;

    //Connecting to middleware
    muzzley.connectUser("guest", muzzleyConfig.activity, function(err, participant) {
        if (err) return console.log("Error: " + error);
        muzzleyConn = participant; //make it global

        participant.on('signalingMessage', function(eventName, data) {
            //console.log("Data signalingMessage: " + eventName);
            if (eventName == 'try-again') {
                //writeToScreen("Trying to init connection");
                initConnection();
            }
        });

        participant.on('quit', function() {
            console.log("quit");
        });

    });
}

function initConnection() {
    websocket = new WebSocket(wsUri);
    websocket.onopen = function(evt) {
        onOpen(evt)
    };
    websocket.onclose = function(evt) {
        onClose(evt)
    };
    websocket.onmessage = function(evt) {
        onMessage(evt)
    };
    websocket.onerror = function(evt) {
        onError(evt)
    };
}

function onOpen(evt) {
    writeToScreen("The app is installed.");
}

function onClose(evt) {
    //the event onError doesn't work on my mobile browser of htc one
    //so we've to workarround
    if (!app_verified)
        muzzleyConn.sendSignal("error-connecting", {});
}

function onMessage(evt) {
    writeToScreen("Message received: " + evt.data);

    try {
        var data = JSON.parse(evt.data);

        if (data.question == "tracking-id") {
            writeToScreen("Sending trackingID: " + trackingId);
            doSend(trackingId);
            app_verified = true; //if i could send the message it means that the app was verified
        }

    } catch (e) {
        writeToScreen("Exception: " + e.message);
    }

    websocket.close();
}

function onError(evt) {
    //This error function doesn't work in my mobile browser in HTC ONE          
}

function doSend(message) {
    writeToScreen("SENT: " + message);
    websocket.send(message);
}

function writeToScreen(message) {
    return; //closed for now

    var pre = document.createElement("p");
    pre.style.wordWrap = "break-word";
    pre.innerHTML = message;
    output.appendChild(pre);
}

function getUniqueTrackingId() {
    //This is only for testing purposes
    //You should get this tracking id from your webserver/tracking-id manager
    return "id" + Math.random().toString(16).slice(2);
}