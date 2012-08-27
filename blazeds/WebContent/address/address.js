var swfVersionStr = "10.2.0";
// To use express install, set to playerProductInstall.swf, otherwise the empty string. 
var xiSwfUrlStr = "playerProductInstall.swf";
var flashvars = {};
var params = {};
params.quality = "high";
params.bgcolor = "#ffffff";
params.allowscriptaccess = "sameDomain";
params.allowfullscreen = "true";
var attributes = {};
attributes.id = "Address";
attributes.name = "Address";
attributes.align = "middle";
swfobject.embedSWF(
    "address/Address.swf", "flashContent", 
    "950", "850",
    swfVersionStr, xiSwfUrlStr, 
    flashvars, params, attributes);
// JavaScript enabled so display the flashContent div in case it is not replaced with a swf object.
swfobject.createCSS("#flashContent", "display:block;text-align:left;");

function readyPhone() { }