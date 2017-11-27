function search(){
    var query = $("input").val().replace(" ", "-");
    window.location.href = "http://localhost:8080/search/" + query;
  }

  $(".searchbar").bind("keypress", function(e){
    if (e.keyCode == 13){
      search();
    }
  });

  $("#search-btn").click(search);



  $(".suggestions").html("");
  $(".suggestions").css("opacity", "0");
  $(".suggestions").css("top", $(".searchbar").css("bottom"));

  var input = document.getElementById("search-bar");

  input.onkeyup = function(){
    var txt = this.value;

    if(!txt){
      $(".suggestions").css("opacity", "0");
	    return;
    }

    var suggestions = 0;
    var maxSuggestions = 10;
    var frag = document.createDocumentFragment();

    for(var i = 0; i < possibilities.length; i++){
      if(suggestions < maxSuggestions && new RegExp("^"+txt,"i").test(possibilities[i])){
				var word = document.createElement("li");
				frag.append(word);
				word.innerHTML = possibilities[i].replace(new RegExp("^("+txt+")","i"),"<strong>$1</strong>");
				word.w = possibilities[i];
				word.onmousedown = function(){
					input.focus();
					input.value = this.w;
					$(".suggestions").css("opacity", "0");
					search();
					return false;
				};
				suggestions++;
			}
    }

    if(suggestions){
			$(".suggestions").html("");
			$(".suggestions").append(frag);
	  $(".suggestions").css("width", $(".searchbar").width());	
      $(".suggestions").css("opacity", "1");
		}
		else {
			$(".suggestions").css("opacity", "0");
		}

  }

  input.onblur = function(){
		$(".suggestions").css("opacity", "0");
	};