/*

HOW TO ADD NEW PLAYER:
 - increase nPlayers
 - implement the initialization
 		- handle onReady to increase nPlayersReady
 		- methods to play/pause/load song
		- play/pause/end events handling
		- a way to get song duration
	- edit loadSong to load a new media
	- add media to button bindings

*/


/* =====================================================================
 *                            THUMBNAIL HANDLING
 * =====================================================================
 */


// $("#song-link").on("mouseenter", function(){
// 	$("#bg").css("background-image", "url(" + data[index-1][4] + ")");
// });

// $("#song-link").on("mouseleave", function(){
//	$("#bg").css("background-image", "none");
//	$("#bg").css("background-color", "#FFF");
// });

/* =====================================================================
 *                          GLOBAL VARIABLES
 * =====================================================================
 */

// Current song index in global var 'data'
var index =  0;

/* Current song playing state,
 * inspired from YouTube API:
 * -1: not started
 *  0: ended
 *  1: playing
 *  2: paused
 */
var state = -1;

// Number of players: YouTube, Soundcloud
var nPlayers = 2;
// Number of players ready
var nPlayersReady = 0;

// Progression variables
var duration = 0;
var tStart   = 0;
var tStopped = 0;
var tElapsed = 0;

/* =====================================================================
 *                   MEDIA PLAYERS INITIALIZATION
 * =====================================================================
 */

/*
 * Soundcloud player initialization
 * doc @ https://developers.soundcloud.com/docs/api/html5-widget
 */

// Load iframe and notify when ready
var scPlayer = SC.Widget('sc-player');
scPlayer.bind(SC.Widget.Events.READY,  function(event){
	nPlayersReady++;
});

// Handle events: YouTube
scPlayer.bind(SC.Widget.Events.PLAY,   function(event){
  state = 1;
  playPlayer();
});
scPlayer.bind(SC.Widget.Events.PAUSE,  function(event){
  state = 2;
  pausePlayer();
});
scPlayer.bind(SC.Widget.Events.FINISH, function(event){
	state = 0;
	loadSong();
});

/*
 * YouTube player initialization
 * doc @ https://developers.google.com/youtube/iframe_api_reference
 */

// API loading
try{
	var tag = document.createElement('script');
	tag.src = "https://www.youtube.com/iframe_api";
	var firstScriptTag = document.getElementsByTagName('script')[0];
	firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);
} catch (e){
	//TODO: handle exception
}

// iframe creation
var ytPlayer;    // Will contain the js object to use methods from
var ytPlayerTmp; // Temporary, do not use after onYtPlayerReady
function onYouTubeIframeAPIReady() {
	ytPlayerTmp = new YT.Player('yt-player', {
  	height: '0',
    width: '0',
    videoId: "QyrrvEuRNWU",
    events: {
    	'onReady': onYtPlayerReady,
      'onStateChange': onYtPlayerStateChange
    }
	});
}

// Notify when ready
function onYtPlayerReady(event) {
	ytPlayer = event.target;
	nPlayersReady++;
}

// Handling events: Soundcloud
function onYtPlayerStateChange(event) {
    state = event.data;
    if (state == 0) {
    	loadSong();
    }
    if (state == 1){
    	playPlayer();
    	getYtDuration();
    } else {
    	pausePlayer();
    }
}

function getYtDuration(){
	duration = ytPlayer.getDuration();
}

/* =====================================================================
 *                          GENERAL FUNCTIONS
 * =====================================================================
 */

// Handling events: general
function pausePlayer(){
	$("#song-play").removeClass("fa-pause");
    $("#song-play").addClass("fa-play");
    tStopped = tElapsed;
}
function playPlayer(){
	$("#song-play").removeClass("fa-play");
    $("#song-play").addClass("fa-pause");
    tStart = new Date().getTime() / 1000;
}

// Song loading
function loadSong(){

	if (data.length == 0) {
		// No song loaded
		$("#player-info > *:first-child")
			.html("No matching results <br><br>"
					+ "<img alt='travolta confused' src="
					+ "'https://media.giphy.com/media/hEc4k5pN17GZq/giphy.gif'"
					+ " />");
	} else if (nPlayersReady < nPlayers){
		// Wait for players to be ready
		setTimeout(loadSong, 100);
  } else if (index >= 0 && index < data.length){

		// By default stop everything, to avoid overlaps
  	ytPlayer.stopVideo();
    scPlayer.pause();

		// For each player, requests the load of a new song
		if (data[index][0] == "yt"){
    	ytPlayer.cueVideoById({'videoId': data[index][1]});
      ytPlayer.playVideo();
    } else if (data[index][0] == "sc"){
      scPlayer.load(data[index][1]);
			// When the song is loaded, the event is fired
      scPlayer.bind(SC.Widget.Events.READY, function(event){
      	scPlayer.play();
        scPlayer.getDuration(function(d){
        	duration = d / 1000;
        })
      });
    }

		// Setting up song information
    $("#song-artist").html(data[index][2]);
    $("#song-title").html(data[index][3]);
    $("#song-index").html(index + 1);
    $("#song-max").html(data.length);
		if (data[index][0] == "yt"){
			$("#song-link").attr("href", "http://youtu.be/" + data[index][1]);
		} else if (data[index][0] == "sc"){
			$("#song-link").attr("href", data[index][1]);
		}

		// Reset progression vars
    tStart   = 0;
    tStopped = 0;
    tElapsed = 0;
    $("#progression").css("width", "0%");

		// Get ready for next song
		index++;
  }
}

/* =====================================================================
 *                          BUTTONS BINDING
 * =====================================================================
 */

$("#song-prev").click(function(event){
	if (index >= 2){
		index -= 2;
		loadSong();
	}
});
$("#song-next").click(function(event){
	if (index < data.length){
  	loadSong();
	}
});
$("#song-play").click(function(event){
	if (state == 1){
		if (data[index-1][0] == "yt"){
    	ytPlayer.pauseVideo();
    } else if (data[index-1][0] == "sc"){
      scPlayer.pause();
    }
  } else if (state == 2) {
    if (data[index-1][0] == "yt"){
      ytPlayer.playVideo();
    } else if (data[index-1][0] == "sc"){
      scPlayer.play();
    }
  }
});

/* =====================================================================
 *                      SCRIPT BEGINNING
 * =====================================================================
 */

loadSong();

setInterval(function(){
  if (state == 1 && duration != null && duration > 0){
    tElapsed = tStopped + (new Date().getTime() / 1000) - tStart;
    var percent = parseInt(100 * tElapsed / duration);
    $("#progression").css("width", percent.toString() + "%");
  }
}, 1000);
