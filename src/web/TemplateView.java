package web;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * 
 * Implements the view from the MVC schema.
 * 
 * Uses a template to structure the response.
 * Uses a ViewEngine to process this template
 * with regards to the model.
 * 
 * @author Yohan Chalier
 *
 */
public class TemplateView extends View{
	
	private final String         template;
	
	/**
	 * The constructor reads the template and
	 * stores it for future uses, as it is final.
	 * 
	 * @param filename
	 * @param engine
	 * @throws IOException
	 */
	public TemplateView(String filename, ViewEngine engine) throws IOException {
		
		super(engine);
		
		InputStreamReader reader = new InputStreamReader(
				getClass().getResourceAsStream(filename),
				StandardCharsets.UTF_8);
		int c;
		StringBuilder builder = new StringBuilder();
		while ((c = reader.read()) != -1) builder.append((char) c);
		reader.close();
		this.template = builder.toString();
	}
	
	
	public String getTemplate() {
		return template;
	}
	
}
