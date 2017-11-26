package web;

/**
 * 
 * A default template engine to serve static files.
 * Just returns the template without any alteration.
 * 
 * @author Yohan Chalier
 *
 */
public class StaticEngine implements ViewEngine {

	@Override
	public String process(View view) {
		return ((TemplateView) view).getTemplate();
	}

}
