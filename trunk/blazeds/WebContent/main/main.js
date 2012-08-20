 // For version detection, set to min. required Flash Player version, or 0 (or 0.0.0), for no version detection. 
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
attributes.id = "Mainflex";
attributes.name = "Mainflex";
attributes.align = "middle";
swfobject.embedSWF(
    "main/Main.swf", "flashContent", 
    "950", "540", 
    swfVersionStr, xiSwfUrlStr, 
    flashvars, params, attributes);
// JavaScript enabled so display the flashContent div in case it is not replaced with a swf object.
swfobject.createCSS("#flashContent", "display:block;height:1500px;text-align:left;");

function readyPhone() { }