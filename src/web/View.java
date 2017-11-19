package web;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class View {
	
	private final String         template;
	private final TemplateEngine engine;
	private Model          model;

	private List<String>   hierarchy;
	private String[]       query;
	
	
	public View(String filename, TemplateEngine engine) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		String line;
		StringBuffer buffer = new StringBuffer();
		while ((line = reader.readLine()) != null) buffer.append(line);
		reader.close();
		this.template = buffer.toString();
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
		this.hierarchy  = hierarchy;
		this.query = query;
	}
	

	public String getResponse() throws IOException {
		return engine.process(this);
	}
	
}
