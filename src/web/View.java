package web;

import java.io.IOException;
import java.util.List;

public class View {
	
	private final ViewEngine     engine;
	private       Model          model;
	private       List<String>   hierarchy;
	private       String[]       query;
	
	
	public View(ViewEngine engine)
			throws IOException {
		this.engine   = engine;
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
