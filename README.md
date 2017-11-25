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

You can for sure clone this repo, but the `.jar` file is enough. It contains everything.

## getting started

If you're using the `.jar` file, you can use it by double-clicking in a graphic interface (strongly NOT recommanded, as you won't be able to stop the web server  unless you kill the process with the task manager).
What I recommand is using it in command lines:

	java -jar jarfile.jar [options]

### options


 option | extanded   | argument | description              
------- | ---------- | -------- | -------------
 `-h`   | `--help'   |          | display this help menu   
 `-l`   | `--log`    |          | activate the logger
 `-c`   | `--config` | FILENAME | load a given config file


See below for more info about the logger and the config file.

### config

A configuration file allows any user to changes the way the program handles the posts and the songs. Lines starting with a `#` are ommitted, as for empty lines. See the file `.config` for an example.

	# Default lines contains default
	# values that are hard-coded in the
	# software.
	
	# The port to open the local server on. Do not change unless you alreadyhave a library file to load.
	# PORT=8080
	
	# The number of posts to fetch on the subreddit.
	# FETCH_AMOUNT=999
	
	# When computing the score of a song, it averages between a fame score, based on views, and a quality score based on likes (more or less). The weights of this average are those:
	# WEIGHT_FAME=2
	# WEIGHT_QUALITY=1
	
	# When using the search engine on the webpage, a 'match' score is computed based on how the song genres match the query. If it matches the most important noun, it is a main match. If it matches an adjective-like word, it is a sub match. Those are worth:
	# MATCH_SCORE_MAIN=3
	# MATCH_SCORE_SUBS=1

	# Once this is done, the final results are ordered with a score averaging the match score previously computed with the song score.
	# WEIGHT_SONG_SCORE=1
	# WEIGHT_MATCH_SCORE=1
	
	# The file to save the library to
	# FILE_LIBRARY=library.json
	
	# The file to save the token at
	# FILE_TOKEN=token.json
	
	# The file to save the log at
	# FILE_LOG=.log
	
	# User agent to use when send HTTP requests.
	# USER_AGENT=Mozilla/5.0
    
	# Correspondences: To handle typos and synonimity across genres, the software uses this table to map the corresponding genre.
	#
	# Syntax is the following:
	# main genre:sub genre;sub genre; sub genre
	#
	# Default correspondences are hard-coded. If one line in this configuration file starts with CORR, then the default map is totally emptied and replaced by the lines from this file.
	#
	# CORR=hiphop:hip-hop;hip hop
	# CORR=chillhop:chill hop
	# CORR=rnb:r&b
	# CORR=rock&roll:rock'n'roll
	# CORR=electro:electronica;electronic;electonic
	# CORR=psychedelic:psych
	# CORR=alternative:alt
	# CORR=acapelle:cappella
	# CORR=chill:chillout;chillwave;downtempo;ambient
	# CORR=pop electro:electopop;electropop
	# CORR=punk electro:electropunk
	# CORR=rock electro:electrorock
	# CORR=edm:idm
	# CORR=indie:indi
	# CORR=jazz:jazzhop

### logger

The logger saves the output of the program (the differents steps, request handled) and most importantly the SongExcpetion raised when the loaded could not
handle a song when adding to the library. Its purpose was mainly to understand why some songs could not be appended. It is not a proper debug log.
Therefore, if you encounter a bug, you can report the stack trace you got from the terminal.

## usage

### External files

#### Reddit authentication

First the program needs to connect to Reddit be able to use its API. Therefore the program retrieves a token which by default is stored in `token.json`.
This token is account based from what I understand, so ensure to keep it to your own. Loosing it is not big of a deal, the program will take a bit longer
at the beginning to retrieve it, that's all.

#### Library building

Then a library is built by the program. By default, it is stored in `library.json`. The amount of data stored is set by the FETCH_AMOUNT parameter in the config file.
The library does not update itself. So if you want to refresh it, just delete the file and a new one will be generated.

### Web interface

The interface consists of a small website. The sitemap is the following:

	/					          landing page, with the search bar
		/wait			          waiting page, when the library's loading
		/library			      a display of all the database
		/search/(query)			  playlist page
			/search/(query)?rand  playlist page with the songs shuffled

The interface is pretty intuitive I think, you should not be too lost.

### Exit

Be sure to exit the program (kill the process) at the end, so the server exits and releases the port.
Otherwise, you won't be able to re-open the program afterward. You'll get an error like:

	Exception in thread "main" java.net.BindException: Address already in use (Bind failed)

## development

All the code documentation is stored in `/doc/`. It is a javadoc, generated by Java. I hope it is complete enough.

