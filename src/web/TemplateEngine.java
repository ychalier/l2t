package web;

/**
 * 
 * A interface to setup view with a function
 * to process the template.
 * 
 * Usually, such an engine uses the model
 * referenced in the view to replace placeholder
 * in the template before returning it.
 * 
 * @author Yohan Chalier
 *
 */
public interface TemplateEngine {
	
	public String process(View view);

}
