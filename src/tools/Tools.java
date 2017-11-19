package tools;

public class Tools {
	
	public static String doubleToStr(double d) {
		if(d < 0.01 && d > 0) return "0.0-";
		return Double.toString(d).substring(0, Math.min(4, Double.toString(d).length()));
	}
	
}
