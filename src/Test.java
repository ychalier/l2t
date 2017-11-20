import java.io.IOException;

import scrapper.Connection;
import tools.Regex;

public class Test {

	public static void main(String[] args) throws IOException {
		
		String url = "https://soundcloud.com/watergaterecords/rodriguez-jr-1pm-sunrise-2";

		String html = new Connection(url).getResponse();
		
		int playCount = Integer.parseInt(
				Regex.parse(
						"<meta property=\"soundcloud:play_count\" content=\"([0-9]+)\">",
						html));
		int likeCount = Integer.parseInt(
				Regex.parse(
						"<meta property=\"soundcloud:like_count\" content=\"([0-9]+)\">",
						html));
		
		System.out.println(playCount);
		System.out.println(likeCount);
	}

}
