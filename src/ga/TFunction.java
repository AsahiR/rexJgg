package ga;

public class TFunction {
	
	public static double evaluate(String str,TVector vec) {
		switch(str) {
		case "kTablet":
			return kTablet(vec);
		default:
			return Double.NaN;
		}
		
	}
	public static double kTablet(TVector vec) {
		int limit=vec.getDimension()/4;
		double result=0.0;
		for(int i=0;i<vec.getDimension();i++) {
			double tmp=Math.pow(vec.getElement(i),2);
			if(i<limit) {
				result+=tmp;
			}else {
				result+=10000.0*tmp;
			}
		}
		return result;
	}
}

