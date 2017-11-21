(function(){var f,h=this;function l(a){return"string"==typeof a}
function m(a){a=a.split(".");for(var b=h,c=0;c<a.length;c++)if(b=b[a[c]],null==b)return null;return b}
function aa(){}
function n(a){var b=typeof a;if("object"==b)if(a){if(a instanceof Array)return"array";if(a instanceof Object)return b;var c=Object.prototype.toString.call(a);if("[object Window]"==c)return"object";if("[object Array]"==c||"number"==typeof a.length&&"undefined"!=typeof a.splice&&"undefined"!=typeof a.propertyIsEnumerable&&!a.propertyIsEnumerable("splice"))return"array";if("[object Function]"==c||"undefined"!=typeof a.call&&"undefined"!=typeof a.propertyIsEnumerable&&!a.propertyIsEnumerable("call"))return"function"}else return"null";
else if("function"==b&&"undefined"==typeof a.call)return"object";return b}
function p(a){var b=n(a);return"array"==b||"object"==b&&"number"==typeof a.length}
function ba(a){var b=typeof a;return"object"==b&&null!=a||"function"==b}
var q="closure_uid_"+(1E9*Math.random()>>>0),ca=0;function da(a,b,c){return a.call.apply(a.bind,arguments)}
function ea(a,b,c){if(!a)throw Error();if(2<arguments.length){var d=Array.prototype.slice.call(arguments,2);return function(){var c=Array.prototype.slice.call(arguments);Array.prototype.unshift.apply(c,d);return a.apply(b,c)}}return function(){return a.apply(b,arguments)}}
function r(a,b,c){Function.prototype.bind&&-1!=Function.prototype.bind.toString().indexOf("native code")?r=da:r=ea;return r.apply(null,arguments)}
function fa(a,b){for(var c in b)a[c]=b[c]}
var ha=Date.now||function(){return+new Date};
function t(a,b){var c=a.split("."),d=h;c[0]in d||!d.execScript||d.execScript("var "+c[0]);for(var e;c.length&&(e=c.shift());)c.length||void 0===b?d[e]&&d[e]!==Object.prototype[e]?d=d[e]:d=d[e]={}:d[e]=b}
function u(a,b){function c(){}
c.prototype=b.prototype;a.D=b.prototype;a.prototype=new c;a.prototype.constructor=a;a.W=function(a,c,g){for(var e=Array(arguments.length-2),d=2;d<arguments.length;d++)e[d-2]=arguments[d];return b.prototype[c].apply(a,e)}}
;var ia=Array.prototype.indexOf?function(a,b){return Array.prototype.indexOf.call(a,b,void 0)}:function(a,b){if(l(a))return l(b)&&1==b.length?a.indexOf(b,0):-1;
for(var c=0;c<a.length;c++)if(c in a&&a[c]===b)return c;return-1},v=Array.prototype.forEach?function(a,b,c){Array.prototype.forEach.call(a,b,c)}:function(a,b,c){for(var d=a.length,e=l(a)?a.split(""):a,g=0;g<d;g++)g in e&&b.call(c,e[g],g,a)};
function ja(a,b){a:{var c=a.length;for(var d=l(a)?a.split(""):a,e=0;e<c;e++)if(e in d&&b.call(void 0,d[e],e,a)){c=e;break a}c=-1}return 0>c?null:l(a)?a.charAt(c):a[c]}
function ka(a){return Array.prototype.concat.apply([],arguments)}
function w(a){var b=a.length;if(0<b){for(var c=Array(b),d=0;d<b;d++)c[d]=a[d];return c}return[]}
;function la(a,b){this.c=a;this.f=b;this.b=0;this.a=null}
la.prototype.get=function(){if(0<this.b){this.b--;var a=this.a;this.a=a.next;a.next=null}else a=this.c();return a};var ma=/&/g,na=/</g,oa=/>/g,pa=/"/g,qa=/'/g,ra=/\x00/g,sa=/[\x00&<>"']/;var x;a:{var ta=h.navigator;if(ta){var ua=ta.userAgent;if(ua){x=ua;break a}}x=""};function va(a){var b=z,c;for(c in b)if(a.call(void 0,b[c],c,b))return c}
;function wa(a){h.setTimeout(function(){throw a;},0)}
var A;
function xa(){var a=h.MessageChannel;"undefined"===typeof a&&"undefined"!==typeof window&&window.postMessage&&window.addEventListener&&-1==x.indexOf("Presto")&&(a=function(){var a=document.createElement("IFRAME");a.style.display="none";a.src="";document.documentElement.appendChild(a);var b=a.contentWindow;a=b.document;a.open();a.write("");a.close();var c="callImmediate"+Math.random(),d="file:"==b.location.protocol?"*":b.location.protocol+"//"+b.location.host;a=r(function(a){if(("*"==d||a.origin==d)&&
a.data==c)this.port1.onmessage()},this);
b.addEventListener("message",a,!1);this.port1={};this.port2={postMessage:function(){b.postMessage(c,d)}}});
if("undefined"!==typeof a&&-1==x.indexOf("Trident")&&-1==x.indexOf("MSIE")){var b=new a,c={},d=c;b.port1.onmessage=function(){if(void 0!==c.next){c=c.next;var a=c.G;c.G=null;a()}};
return function(a){d.next={G:a};d=d.next;b.port2.postMessage(0)}}return"undefined"!==typeof document&&"onreadystatechange"in document.createElement("SCRIPT")?function(a){var b=document.createElement("SCRIPT");
b.onreadystatechange=function(){b.onreadystatechange=null;b.parentNode.removeChild(b);b=null;a();a=null};
document.documentElement.appendChild(b)}:function(a){h.setTimeout(a,0)}}
;function B(){this.b=this.a=null}
var ya=new la(function(){return new C},function(a){a.reset()});
B.prototype.add=function(a,b){var c=ya.get();c.set(a,b);this.b?this.b.next=c:this.a=c;this.b=c};
B.prototype.remove=function(){var a=null;this.a&&(a=this.a,this.a=this.a.next,this.a||(this.b=null),a.next=null);return a};
function C(){this.next=this.b=this.a=null}
C.prototype.set=function(a,b){this.a=a;this.b=b;this.next=null};
C.prototype.reset=function(){this.next=this.b=this.a=null};function za(a){D||Aa();E||(D(),E=!0);Ba.add(a,void 0)}
var D;function Aa(){if(-1!=String(h.Promise).indexOf("[native code]")){var a=h.Promise.resolve(void 0);D=function(){a.then(Ca)}}else D=function(){var a=Ca;
"function"!=n(h.setImmediate)||h.Window&&h.Window.prototype&&-1==x.indexOf("Edge")&&h.Window.prototype.setImmediate==h.setImmediate?(A||(A=xa()),A(a)):h.setImmediate(a)}}
var E=!1,Ba=new B;function Ca(){for(var a;a=Ba.remove();){try{a.a.call(a.b)}catch(c){wa(c)}var b=ya;b.f(a);100>b.b&&(b.b++,a.next=b.a,b.a=a)}E=!1}
;function F(){this.c=this.c;this.f=this.f}
F.prototype.c=!1;F.prototype.dispose=function(){this.c||(this.c=!0,this.A())};
F.prototype.A=function(){if(this.f)for(;this.f.length;)this.f.shift()()};function Da(a,b){var c,d;var e=document;e=b||e;if(e.querySelectorAll&&e.querySelector&&a)return e.querySelectorAll(""+(a?"."+a:""));if(a&&e.getElementsByClassName){var g=e.getElementsByClassName(a);return g}g=e.getElementsByTagName("*");if(a){var k={};for(c=d=0;e=g[c];c++){var y=e.className,U;if(U="function"==typeof y.split)U=0<=ia(y.split(/\s+/),a);U&&(k[d++]=e)}k.length=d;return k}return g}
function Ea(a,b){for(var c=0;a;){if(b(a))return a;a=a.parentNode;c++}return null}
;var G="StopIteration"in h?h.StopIteration:{message:"StopIteration",stack:""};function H(){}
H.prototype.next=function(){throw G;};
H.prototype.o=function(){return this};
function Fa(a){if(a instanceof H)return a;if("function"==typeof a.o)return a.o(!1);if(p(a)){var b=0,c=new H;c.next=function(){for(;;){if(b>=a.length)throw G;if(b in a)return a[b++];b++}};
return c}throw Error("Not implemented");}
function Ga(a,b){if(p(a))try{v(a,b,void 0)}catch(c){if(c!==G)throw c;}else{a=Fa(a);try{for(;;)b.call(void 0,a.next(),void 0,a)}catch(c){if(c!==G)throw c;}}}
function Ha(a){if(p(a))return w(a);a=Fa(a);var b=[];Ga(a,function(a){b.push(a)});
return b}
;var Ia=h.JSON.stringify;function I(a){F.call(this);this.m=1;this.g=[];this.h=0;this.a=[];this.b={};this.v=!!a}
u(I,F);f=I.prototype;f.subscribe=function(a,b,c){var d=this.b[a];d||(d=this.b[a]=[]);var e=this.m;this.a[e]=a;this.a[e+1]=b;this.a[e+2]=c;this.m=e+3;d.push(e);return e};
function Ja(a,b,c){var d=J;if(a=d.b[a]){var e=d.a;(a=ja(a,function(a){return e[a+1]==b&&e[a+2]==c}))&&d.F(a)}}
f.F=function(a){var b=this.a[a];if(b){var c=this.b[b];if(0!=this.h)this.g.push(a),this.a[a+1]=aa;else{if(c){var d=ia(c,a);0<=d&&Array.prototype.splice.call(c,d,1)}delete this.a[a];delete this.a[a+1];delete this.a[a+2]}}return!!b};
f.I=function(a,b){var c=this.b[a];if(c){for(var d=Array(arguments.length-1),e=1,g=arguments.length;e<g;e++)d[e-1]=arguments[e];if(this.v)for(e=0;e<c.length;e++){var k=c[e];Ka(this.a[k+1],this.a[k+2],d)}else{this.h++;try{for(e=0,g=c.length;e<g;e++)k=c[e],this.a[k+1].apply(this.a[k+2],d)}finally{if(this.h--,0<this.g.length&&0==this.h)for(;c=this.g.pop();)this.F(c)}}return 0!=e}return!1};
function Ka(a,b,c){za(function(){a.apply(b,c)})}
f.clear=function(a){if(a){var b=this.b[a];b&&(v(b,this.F,this),delete this.b[a])}else this.a.length=0,this.b={}};
f.A=function(){I.D.A.call(this);this.clear();this.g.length=0};function La(){}
;function K(){}
u(K,La);K.prototype.clear=function(){var a=Ha(this.o(!0)),b=this;v(a,function(a){b.remove(a)})};function L(a){this.a=a}
u(L,K);function Ma(a){if(a.a)try{a.a.setItem("__sak","1"),a.a.removeItem("__sak")}catch(b){}}
f=L.prototype;f.set=function(a,b){try{this.a.setItem(a,b)}catch(c){if(0==this.a.length)throw"Storage mechanism: Storage disabled";throw"Storage mechanism: Quota exceeded";}};
f.get=function(a){a=this.a.getItem(a);if(!l(a)&&null!==a)throw"Storage mechanism: Invalid value was encountered";return a};
f.remove=function(a){this.a.removeItem(a)};
f.o=function(a){var b=0,c=this.a,d=new H;d.next=function(){if(b>=c.length)throw G;var d=c.key(b++);if(a)return d;d=c.getItem(d);if(!l(d))throw"Storage mechanism: Invalid value was encountered";return d};
return d};
f.clear=function(){this.a.clear()};
f.key=function(a){return this.a.key(a)};function Na(){var a=null;try{a=window.localStorage||null}catch(b){}this.a=a}
u(Na,L);function Oa(){var a=null;try{a=window.sessionStorage||null}catch(b){}this.a=a}
u(Oa,L);var Pa=/^(?:([^:/?#.]+):)?(?:\/\/(?:([^/?#]*)@)?([^/#?]*?)(?::([0-9]+))?(?=[/#?]|$))?([^?#]+)?(?:\?([^#]*))?(?:#([\s\S]*))?$/;function Qa(a){var b=a.match(Pa);a=b[1];var c=b[2],d=b[3];b=b[4];var e="";a&&(e+=a+":");d&&(e+="//",c&&(e+=c+"@"),e+=d,b&&(e+=":"+b));return e}
function Ra(a,b,c){if("array"==n(b))for(var d=0;d<b.length;d++)Ra(a,String(b[d]),c);else null!=b&&c.push(a+(""===b?"":"="+encodeURIComponent(String(b))))}
function Sa(a){var b=[],c;for(c in a)Ra(c,a[c],b);return b.join("&")}
var Ta=/#|$/;var M=window.yt&&window.yt.config_||window.ytcfg&&window.ytcfg.data_||{};t("yt.config_",M);function Ua(a){var b=arguments;if(1<b.length)M[b[0]]=b[1];else{b=b[0];for(var c in b)M[c]=b[c]}}
;function Va(a){return a&&window.yterr?function(){try{return a.apply(this,arguments)}catch(b){Wa(b)}}:a}
function Wa(a,b){var c=m("yt.logging.errors.log");c?c(a,b,void 0,void 0,void 0):(c=[],c="ERRORS"in M?M.ERRORS:c,c.push([a,b,void 0,void 0,void 0]),Ua("ERRORS",c))}
;var Xa=0;t("ytDomDomGetNextId",m("ytDomDomGetNextId")||function(){return++Xa});var Ya={stopImmediatePropagation:1,stopPropagation:1,preventMouseEvent:1,preventManipulation:1,preventDefault:1,layerX:1,layerY:1,screenX:1,screenY:1,scale:1,rotation:1,webkitMovementX:1,webkitMovementY:1};
function N(a){this.type="";this.source=this.data=this.currentTarget=this.relatedTarget=this.target=null;this.charCode=this.keyCode=0;this.shiftKey=this.ctrlKey=this.altKey=!1;this.clientY=this.clientX=0;this.changedTouches=this.touches=null;if(a=a||window.event){this.a=a;for(var b in a)b in Ya||(this[b]=a[b]);(b=a.target||a.srcElement)&&3==b.nodeType&&(b=b.parentNode);this.target=b;if(b=a.relatedTarget)try{b=b.nodeName?b:null}catch(c){b=null}else"mouseover"==this.type?b=a.fromElement:"mouseout"==
this.type&&(b=a.toElement);this.relatedTarget=b;this.clientX=void 0!=a.clientX?a.clientX:a.pageX;this.clientY=void 0!=a.clientY?a.clientY:a.pageY;this.keyCode=a.keyCode?a.keyCode:a.which;this.charCode=a.charCode||("keypress"==this.type?this.keyCode:0);this.altKey=a.altKey;this.ctrlKey=a.ctrlKey;this.shiftKey=a.shiftKey}}
N.prototype.preventDefault=function(){this.a&&(this.a.returnValue=!1,this.a.preventDefault&&this.a.preventDefault())};
N.prototype.stopPropagation=function(){this.a&&(this.a.cancelBubble=!0,this.a.stopPropagation&&this.a.stopPropagation())};
N.prototype.stopImmediatePropagation=function(){this.a&&(this.a.cancelBubble=!0,this.a.stopImmediatePropagation&&this.a.stopImmediatePropagation())};var z=m("ytEventsEventsListeners")||{};t("ytEventsEventsListeners",z);var Za=m("ytEventsEventsCounter")||{count:0};t("ytEventsEventsCounter",Za);function $a(a,b,c,d){d=void 0===d?!1:d;a.addEventListener&&("mouseenter"!=b||"onmouseenter"in document?"mouseleave"!=b||"onmouseenter"in document?"mousewheel"==b&&"MozBoxSizing"in document.documentElement.style&&(b="MozMousePixelScroll"):b="mouseout":b="mouseover");return va(function(e){return!!e.length&&e[0]==a&&e[1]==b&&e[2]==c&&e[4]==!!d})}
function ab(a){a&&("string"==typeof a&&(a=[a]),v(a,function(a){if(a in z){var b=z[a],d=b[0],e=b[1],g=b[3];b=b[4];d.removeEventListener?d.removeEventListener(e,g,b):d.detachEvent&&d.detachEvent("on"+e,g);delete z[a]}}))}
function bb(a,b,c){var d=void 0===d?!1:d;if(a&&(a.addEventListener||a.attachEvent)){var e=$a(a,b,c,d);if(!e){e=++Za.count+"";var g=!("mouseenter"!=b&&"mouseleave"!=b||!a.addEventListener||"onmouseenter"in document);var k=g?function(d){d=new N(d);if(!Ea(d.relatedTarget,function(b){return b==a}))return d.currentTarget=a,d.type=b,c.call(a,d)}:function(b){b=new N(b);
b.currentTarget=a;return c.call(a,b)};
k=Va(k);a.addEventListener?("mouseenter"==b&&g?b="mouseover":"mouseleave"==b&&g?b="mouseout":"mousewheel"==b&&"MozBoxSizing"in document.documentElement.style&&(b="MozMousePixelScroll"),a.addEventListener(b,k,d)):a.attachEvent("on"+b,k);z[e]=[a,b,c,k,d]}}}
;function cb(a){"function"==n(a)&&(a=Va(a));return window.setInterval(a,250)}
;var db={};function eb(a){return db[a]||(db[a]=String(a).replace(/\-([a-z])/g,function(a,c){return c.toUpperCase()}))}
;var O={},P=[],J=new I,fb={};function gb(){v(P,function(a){a()})}
function hb(a,b){b||(b=document);var c=w(b.getElementsByTagName("yt:"+a)),d="yt-"+a,e=b||document;d=w(e.querySelectorAll&&e.querySelector?e.querySelectorAll("."+d):Da(d,b));return ka(c,d)}
function Q(a,b){return"yt:"==a.tagName.toLowerCase().substr(0,3)?a.getAttribute(b):a?a.dataset?a.dataset[eb(b)]:a.getAttribute("data-"+b):null}
function ib(a,b){J.I.apply(J,arguments)}
;function R(a,b,c){this.h=this.a=this.b=null;this.g=this[q]||(this[q]=++ca);this.c=0;this.B=!1;this.w=[];this.f=null;this.m=c;this.v={};c=document;if(a=l(a)?c.getElementById(a):a)if(c="iframe"==a.tagName.toLowerCase(),b.host||(b.host=c?Qa(a.src):"https://www.youtube.com"),this.b=new S(b),c||(b=jb(this,a),this.h=a,(c=a.parentNode)&&c.replaceChild(b,a),a=b),this.a=a,this.a.id||(a=b=this.a,a=a[q]||(a[q]=++ca),b.id="widget"+a),O[this.a.id]=this,window.postMessage){this.f=new I;kb(this);b=T(this.b,"events");
for(var d in b)b.hasOwnProperty(d)&&this.addEventListener(d,b[d]);for(var e in fb)lb(this,e)}}
f=R.prototype;f.S=function(a,b){this.a.width=a;this.a.height=b;return this};
f.R=function(){return this.a};
f.J=function(a){this.l(a.event,a)};
f.addEventListener=function(a,b){var c=b;"string"==typeof b&&(c=function(){window[b].apply(window,arguments)});
this.f.subscribe(a,c);mb(this,a);return this};
function lb(a,b){var c=b.split(".");if(2==c.length){var d=c[1];a.m==c[0]&&mb(a,d)}}
f.P=function(){this.a.id&&(O[this.a.id]=null);var a=this.f;a&&"function"==typeof a.dispose&&a.dispose();if(this.h){a=this.a;var b=a.parentNode;b&&b.replaceChild(this.h,a)}else(a=this.a)&&a.parentNode&&a.parentNode.removeChild(a);V&&(V[this.g]=null);this.b=null;a=this.a;for(var c in z)z[c][0]==a&&ab(c);this.h=this.a=null};
f.s=function(){return{}};
function W(a,b,c){c=c||[];c=Array.prototype.slice.call(c);b={event:"command",func:b,args:c};a.B?a.C(b):a.w.push(b)}
f.l=function(a,b){if(!this.f.c){var c={target:this,data:b};this.f.I(a,c);ib(this.m+"."+a,c)}};
function jb(a,b){for(var c=document.createElement("iframe"),d=b.attributes,e=0,g=d.length;e<g;e++){var k=d[e].value;null!=k&&""!=k&&"null"!=k&&c.setAttribute(d[e].name,k)}c.setAttribute("frameBorder",0);c.setAttribute("allowfullscreen",1);c.setAttribute("gesture","media");c.setAttribute("title","YouTube "+T(a.b,"title"));(d=T(a.b,"width"))&&c.setAttribute("width",d);(d=T(a.b,"height"))&&c.setAttribute("height",d);var y=a.s();y.enablejsapi=window.postMessage?1:0;window.location.host&&(y.origin=window.location.protocol+
"//"+window.location.host);y.widgetid=a.g;window.location.href&&v(["debugjs","debugcss"],function(a){var b=window.location.href;var c=b.search(Ta);b:{var d=0;for(var e=a.length;0<=(d=b.indexOf(a,d))&&d<c;){var g=b.charCodeAt(d-1);if(38==g||63==g)if(g=b.charCodeAt(d+e),!g||61==g||38==g||35==g)break b;d+=e+1}d=-1}if(0>d)b=null;else{e=b.indexOf("&",d);if(0>e||e>c)e=c;d+=a.length+1;b=decodeURIComponent(b.substr(d,e-d).replace(/\+/g," "))}null===b||(y[a]=b)});
c.src=T(a.b,"host")+a.u()+"?"+Sa(y);return c}
f.H=function(){this.a&&this.a.contentWindow?this.C({event:"listening"}):window.clearInterval(this.c)};
function kb(a){nb(a.b,a,a.g);a.c=cb(r(a.H,a));bb(a.a,"load",r(function(){window.clearInterval(this.c);this.c=cb(r(this.H,this))},a))}
function mb(a,b){a.v[b]||(a.v[b]=!0,W(a,"addEventListener",[b]))}
f.C=function(a){a.id=this.g;a.channel="widget";a=Ia(a);var b=this.b;var c=Qa(this.a.src);b=0==c.indexOf("https:")?[c]:b.b?[c.replace("http:","https:")]:b.f?[c]:[c,c.replace("http:","https:")];if(!this.a.contentWindow)throw Error("The YouTube player is not attached to the DOM.");for(c=0;c<b.length;c++)try{this.a.contentWindow.postMessage(a,b[c])}catch(d){if(d.name&&"SyntaxError"==d.name)Wa(d,"WARNING");else throw d;}};Ma(new Na);Ma(new Oa);function ob(a){return(0==a.search("cue")||0==a.search("load"))&&"loadModule"!=a}
function pb(a){return 0==a.search("get")||0==a.search("is")}
;function X(a,b){if(!a)throw Error("YouTube player element ID required.");var c={title:"video player",videoId:"",width:640,height:360};b&&fa(c,b);R.call(this,a,c,"player");this.i={};this.j={}}
u(X,R);function qb(a){if("iframe"!=a.tagName.toLowerCase()){var b=Q(a,"videoid");b&&(b={videoId:b,width:Q(a,"width"),height:Q(a,"height")},new X(a,b))}}
f=X.prototype;f.u=function(){return"/embed/"+T(this.b,"videoId")};
f.s=function(){var a=T(this.b,"playerVars");if(a){var b={},c;for(c in a)b[c]=a[c];a=b}else a={};window!=window.top&&document.referrer&&(a.widget_referrer=document.referrer.substring(0,256));return a};
f.J=function(a){var b=a.event;a=a.info;switch(b){case "apiInfoDelivery":if(ba(a))for(var c in a)this.i[c]=a[c];break;case "infoDelivery":rb(this,a);break;case "initialDelivery":window.clearInterval(this.c);this.j={};this.i={};sb(this,a.apiInterface);rb(this,a);break;default:this.l(b,a)}};
function rb(a,b){if(ba(b))for(var c in b)a.j[c]=b[c]}
function sb(a,b){v(b,function(a){this[a]||("getCurrentTime"==a?this[a]=function(){var a=this.j.currentTime;if(1==this.j.playerState){var b=(ha()/1E3-this.j.currentTimeLastUpdated_)*this.j.playbackRate;0<b&&(a+=Math.min(b,1))}return a}:ob(a)?this[a]=function(){this.j={};
this.i={};W(this,a,arguments);return this}:pb(a)?this[a]=function(){var b=0;
0==a.search("get")?b=3:0==a.search("is")&&(b=2);return this.j[a.charAt(b).toLowerCase()+a.substr(b+1)]}:this[a]=function(){W(this,a,arguments);
return this})},a)}
f.V=function(){var a=parseInt(T(this.b,"width"),10);var b=parseInt(T(this.b,"height"),10);var c=T(this.b,"host")+this.u();sa.test(c)&&(-1!=c.indexOf("&")&&(c=c.replace(ma,"&amp;")),-1!=c.indexOf("<")&&(c=c.replace(na,"&lt;")),-1!=c.indexOf(">")&&(c=c.replace(oa,"&gt;")),-1!=c.indexOf('"')&&(c=c.replace(pa,"&quot;")),-1!=c.indexOf("'")&&(c=c.replace(qa,"&#39;")),-1!=c.indexOf("\x00")&&(c=c.replace(ra,"&#0;")));a='<iframe width="'+a+'" height="'+b+'" src="'+c+'" frameborder="0" gesture="media" allowfullscreen></iframe>';
return a};
f.U=function(a){return this.i.namespaces?a?this.i[a].options||[]:this.i.namespaces||[]:[]};
f.T=function(a,b){if(this.i.namespaces&&a&&b)return this.i[a][b]};function Y(a,b){var c={title:"Thumbnail",videoId:"",width:120,height:68};b&&fa(c,b);R.call(this,a,c,"thumbnail")}
u(Y,R);function tb(a){if("iframe"!=a.tagName.toLowerCase()){var b=Q(a,"videoid");if(b){b={videoId:b,events:{}};b.width=Q(a,"width");b.height=Q(a,"height");b.thumbWidth=Q(a,"thumb-width");b.thumbHeight=Q(a,"thumb-height");b.thumbAlign=Q(a,"thumb-align");var c=Q(a,"onclick");c&&(b.events.onClick=c);new Y(a,b)}}}
Y.prototype.u=function(){return"/embed/"+T(this.b,"videoId")};
Y.prototype.s=function(){return{player:0,thumb_width:T(this.b,"thumbWidth"),thumb_height:T(this.b,"thumbHeight"),thumb_align:T(this.b,"thumbAlign")}};
Y.prototype.l=function(a,b){Y.D.l.call(this,a,b?b.info:void 0)};function S(a){this.c=a||{};this.a={};this.f=this.b=!1;a=document.getElementById("www-widgetapi-script");if(this.b=!!("https:"==document.location.protocol||a&&0==a.src.indexOf("https:"))){a=[this.c,window.YTConfig||{},this.a];for(var b=0;b<a.length;b++)a[b].host&&(a[b].host=a[b].host.replace("http://","https://"))}}
var V=null;function T(a,b){for(var c=[a.c,window.YTConfig||{},a.a],d=0;d<c.length;d++){var e=c[d][b];if(void 0!=e)return e}return null}
function nb(a,b,c){V||(V={},bb(window,"message",r(a.g,a)));V[c]=b}
S.prototype.g=function(a){if(a.origin==T(this,"host")||a.origin==T(this,"host").replace(/^http:/,"https:")){try{var b=JSON.parse(a.data)}catch(c){return}this.f=!0;this.b||0!=a.origin.indexOf("https:")||(this.b=!0);if(a=V[b.id])a.B=!0,a.B&&(v(a.w,a.C,a),a.w.length=0),a.J(b)}};function ub(a){S.call(this,a);this.a.host="https://www.youtube.com";this.a.title="upload widget";this.a.width=640;this.a.height=.67*T(this,"width")}
u(ub,S);function Z(a,b){var c=new ub(b);R.call(this,a,c,"upload")}
u(Z,R);f=Z.prototype;f.u=function(){return"/upload_embed"};
f.s=function(){var a={},b=T(this.b,"webcamOnly");null!=b&&(a.webcam_only=b);return a};
f.l=function(a,b){Z.D.l.call(this,a,b);"onApiReady"==a&&W(this,"hostWindowReady")};
f.K=function(a){W(this,"setVideoDescription",arguments)};
f.M=function(a){W(this,"setVideoKeywords",arguments)};
f.N=function(a){W(this,"setVideoPrivacy",arguments)};
f.L=function(a){W(this,"setVideoDraftPrivacy",arguments)};
f.O=function(a){W(this,"setVideoTitle",arguments)};t("YT.PlayerState.UNSTARTED",-1);t("YT.PlayerState.ENDED",0);t("YT.PlayerState.PLAYING",1);t("YT.PlayerState.PAUSED",2);t("YT.PlayerState.BUFFERING",3);t("YT.PlayerState.CUED",5);t("YT.UploadWidgetEvent.API_READY","onApiReady");t("YT.UploadWidgetEvent.UPLOAD_SUCCESS","onUploadSuccess");t("YT.UploadWidgetEvent.PROCESSING_COMPLETE","onProcessingComplete");t("YT.UploadWidgetEvent.STATE_CHANGE","onStateChange");t("YT.UploadWidgetState.IDLE",0);t("YT.UploadWidgetState.PENDING",1);
t("YT.UploadWidgetState.ERROR",2);t("YT.UploadWidgetState.PLAYBACK",3);t("YT.UploadWidgetState.RECORDING",4);t("YT.UploadWidgetState.STOPPED",5);t("YT.get",function(a){return O[a]});
t("YT.scan",gb);t("YT.subscribe",function(a,b,c){J.subscribe(a,b,c);fb[a]=!0;for(var d in O)lb(O[d],a)});
t("YT.unsubscribe",function(a,b,c){Ja(a,b,c)});
t("YT.Player",X);t("YT.Thumbnail",Y);t("YT.UploadWidget",Z);R.prototype.destroy=R.prototype.P;R.prototype.setSize=R.prototype.S;R.prototype.getIframe=R.prototype.R;R.prototype.addEventListener=R.prototype.addEventListener;X.prototype.getVideoEmbedCode=X.prototype.V;X.prototype.getOptions=X.prototype.U;X.prototype.getOption=X.prototype.T;Z.prototype.setVideoDescription=Z.prototype.K;Z.prototype.setVideoKeywords=Z.prototype.M;Z.prototype.setVideoPrivacy=Z.prototype.N;Z.prototype.setVideoTitle=Z.prototype.O;
Z.prototype.setVideoDraftPrivacy=Z.prototype.L;P.push(function(a){a=hb("player",a);v(a,qb)});
P.push(function(){var a=hb("thumbnail");v(a,tb)});
"undefined"!=typeof YTConfig&&YTConfig.parsetags&&"onload"!=YTConfig.parsetags||gb();var vb=m("onYTReady");vb&&vb();var wb=m("onYouTubeIframeAPIReady");wb&&wb();var xb=m("onYouTubePlayerAPIReady");xb&&xb();}).call(this);
