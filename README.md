# leethan 2 theece
Leethan2Theece is a music discovery program based on /r/listentothis (yeah, there is a play on word with this subreddit, 1 cookie to whoever finds it!). 

## description

**user's perspective**

Just start the program. Your default browser will open with a loading screen. Once it's done, a search bar allows you to type a genre query (with suggestions).
Hit enter, and a playlist of songs of that type will start automatically, to please your ears with unknown but great songs!

**what's behind**

 - Fetches posts from Reddit subreddit /r/listentothis
 - Extracts songs information (artist, title, genre, ...)
 - For each song, fetches more info from the domain the song is hosted on (YouTube likes, Soundcloud play count, ...)
 - Computes a score for each song, based on their fame and their "quality"
 - Hosts a local HTTP server to provide a user interface
 - A search engine handles user's queries and returns corresponding songs
 - A player handles songs from different domains to play the playlist automatically

Until now, supported domains are _YouTube_ and _Soundcloud_, the two most used domains on this subreddit from what I've seen. I plan to add _Spotify_ and _Bandcamp_.

**why Java?**

Good question. Don't really have an answer. Why not? Just wanted to try it out. Not disappointed.

## installation

You can for sure clone this repo, but the jar file is enough. It contains everything.

## getting started

If you're using the jar file, you can use it by double-clicking in a graphic interface. Though I recommand is using it in command lines:

	java -jar l2t.jar [options]

### options


 option | extanded   | argument | description              
------- | ---------- | -------- | -------------
 `-h`   | `--help`   |          | display this help menu   
 `-l`   | `--log`    |          | activate the logger
 `-c`   | `--config` | filename | load a given config file


See below for more info about the logger and the config file.

### config

A configuration file allows any user to changes the way the program handles the posts and the songs. Lines starting with a `#` are ommitted, as for empty lines. Syntax of a line is

	PARAMETER=value

See the file `.config` for an example

parameter | default | description
--------- | --------- | -----------
`PORT` | 8080 | The port to open the local server on. Do not change unless you already have a library file to load.
`SOCKET_TIMEOUT` | 10000 | Server timeout, in milliseconds, applied once the library is built.
`FETCH_AMOUNT` | 999 | The number of posts to fetch on the subreddit.
`REFRESH_AMOUNT` | 100 | The number of posts to fetch when refreshing the library.
`WEIGHT_FAME` | 2 | Importance of fame score in global song score
`WEIGHT_QUALITY` | 1 | Importance of quality score in global song score
`MATCH_SCORE_MAIN` | 3 | Importance of main genre noun match in match score
`MATCH_SCORE_SUBS` | 1 | Importance of genre adjective match in match score
`WEIGHT_SONG_SCORE` | 1 | Importance of song score in song search score
`WEIGHT_MATCH_SCORE` | 1 | Importance of match score in song search score
`FILE_LIBRARY` | library.json | The file to save the library to
`FILE_TOEKN` | token.json | The file to save the token at
`FILE_LOG` | .log | The file to save the log at
`USER_AGENT` | Mozilla/5.0 | User agent to use when send HTTP requests.
`CORR` | see example | To handle typos and synonimity across genres, the software uses a table to map the corresponding genre. If one line in the configuration file starts with `CORR`, then the default map is totally emptied and replaced by the lines from this file. Format is `main genre:sub genre 1;sub genre 2`. You can add as much sub genres as you want, by separating them with semi-colons.

### logger

The logger saves the output of the program (the differents steps, request handled) and most importantly the SongExcpetion raised when the loaded could not
handle a song when adding to the library. Its purpose was mainly to understand why some songs could not be appended. It is not a proper debug log.
Therefore, if you encounter a bug, you can report the stack trace you got from the terminal.

## usage

### Web interface

The interface consists of a small website. The sitemap is the following:

	/		            landing page, with the search bar
	    /wait	            waiting page, when the library's loading
	    /library		    a display of all the database
	    /search/(query)	    playlist page
	    /search/(query)?rand    playlist page with the songs shuffled

The interface is pretty intuitive I think, you should not be too lost.

### Exiting

Web pages send pulses to the server to keep it alive everything 2 seconds. If the server does not receive any request for 10 seconds, it automatically closes. If anything happens, try to start the program with command lines. You might see an error like:

	Exception in thread "main" java.net.BindException: Address already in use (Bind failed)

It means the previous program did no close correctly and still runs in the background. So you'll have to kill the process manually.

### External files

#### Reddit authentication

First the program needs to connect to Reddit be able to use its API. Therefore the program retrieves a token which by default is stored in `token.json`.
This token is account based from what I understand, so ensure to keep it to your own. Loosing it is not big of a deal, the program will take a bit longer
at the beginning to retrieve it, that's all.

#### Library building

Then a library is built by the program. By default, it is stored in `library.json`. The amount of data stored is set by the `FETCH_AMOUNT` parameter in the config file.
The library does update itself, but only to fetch new posts on Reddit. So if you want to fully refresh it (including YouTube statistics for example), just delete the file and a new one will be generated.

## development

All the code documentation is stored in `/doc/`. It is a javadoc, generated by Java. I hope it is complete enough.

