var app_verified = false;

function init() {
    document.getElementById("trackingId").innerHTML = trackingId;
    addIframe(trackingId);
}

function writeToScreen(message) {
    var pre = document.createElement("p");
    pre.style.wordWrap = "break-word";
    pre.innerHTML = message;
    output.appendChild(pre);
}

function addIframe(trackingId) {
    var obj = document.createElement("iframe");
    obj.src = checkerSrc;
    obj.style.width = "1px";
    obj.style.height = "1px";
    obj.style.border = "0";

    document.getElementById('body').appendChild(obj);
}