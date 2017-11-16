package web;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class View {
	
	private String template;
	
	public View(String template) {
		this.template = template;
	}

	public String getResponse(String route) throws IOException {
		
		BufferedReader reader = new BufferedReader(new FileReader(template));
		String line;
		StringBuffer buffer = new StringBuffer();
		while ((line = reader.readLine()) != null) buffer.append(line);
		reader.close();
		
		return buffer.toString();
	}
	
}
