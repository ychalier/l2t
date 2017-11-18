package web;

public class StaticEngine implements TemplateEngine {

	@Override
	public String process(View view) {
		return view.getTemplate();
	}

}
