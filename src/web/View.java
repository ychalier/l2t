package web;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 
 * Implements the view from the MVC schema.
 * 
 * Uses a template to structure the response.
 * Uses a TemplateEngine to process this template
 * with regards to the model.
 * 
 * @author Yohan Chalier
 *
 */
public class View {
	
	private final String         template;
	private final TemplateEngine engine;
	private       Model          model;
	private       List<String>   hierarchy;
	private       String[]       query;
	
	/**
	 * The constructor reads the template and
	 * stores it for future uses, as it is final.
	 * 
	 * @param filename
	 * @param engine
	 * @throws IOException
	 */
	public View(String filename, TemplateEngine engine) throws IOException {
		InputStreamReader reader = new InputStreamReader(
				getClass().getResourceAsStream(filename),
				StandardCharsets.UTF_8);
		int c;
		StringBuilder builder = new StringBuilder();
		while ((c = reader.read()) != -1) builder.append((char) c);
		reader.close();
		this.template = builder.toString();
		this.engine   = engine;
	}
	
	
	public String getTemplate() {
		return template;
	}
	
	
	public String[] getQuery(){
		return query;
	}
	
	
	public Model getModel() {
		return model;
	}
	
	
	public void setModel(Model model) {
		this.model = model;
	}
	
	
	public List<String> getHierarchy(){
		return hierarchy;
	}
	
	
	public void setRequest(List<String> hierarchy, String[] query) {
		this.hierarchy = hierarchy;
		this.query = query;
	}
	
	/**
	 * Calls the TemplateEngine to process the template.
	 * 
	 * @return The HTTP response given by the view
	 * @throws IOException
	 */
	public String getResponse() throws IOException {
		return engine.process(this);
	}
	
}
