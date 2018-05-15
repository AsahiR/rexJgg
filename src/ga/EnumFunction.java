package ga;

import com.sun.javafx.css.CssError.StylesheetParsingError;

import javafx.scene.shape.Sphere;

public enum EnumFunction {
	KTablet(1),
	Sphere(2);
	
	private final int id;
	
	private EnumFunction(int id) {
		this.id=id;
	}
	
	public double calc(TVector v) {
		switch(this) {
		case KTablet:
			return kTablet(v);
		case Sphere:
			return sphere(v);
		default:
			return 0;
		}
	}
	
  /**
   * @param vec vector
   * @return value
   */
  private  double kTablet(TVector vec) {
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
  
  /**
	 * sphere function
	 * @param v vector
	 * @return value
	 */
	public static double sphere(TVector v) {
		double result = 0.0;
		for (int i = 0; i < v.getDimension(); i++) {
			result += Math.pow(v.getElement(i), 2);
		}
		return result;
	}
}
