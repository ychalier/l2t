function heartbeat () {
	var xmlHttp = new XMLHttpRequest();
	xmlHttp.open("GET", "http://localhost:8080/pulse", true);
	xmlHttp.send(null);
}

setInterval(heartbeat, 2000);