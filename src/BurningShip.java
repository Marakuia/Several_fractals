import java.awt.geom.Rectangle2D;

public class BurningShip extends FractalGenerator{

    public static final int MAX_ITERATIONS = 2000; //максимальное количество иттераций

    public void getInitialRange(Rectangle2D.Double range) {

        range.setRect(-2, -2.5, 4, 4); //диапазон
    }

    public int numIterations(double x, double y) {
        int iter = 0;
        double zreal = 0;  //действительная часть
        double zimag = 0;  //мнимая часть
        while (iter <= MAX_ITERATIONS && zreal * zreal + zimag * zimag < 4){
            double zrealUpdated = zreal * zreal - zimag * zimag + x;
            double zimaginaryUpdated = 2 * Math.abs(zreal) * Math.abs(zimag) + y;
            zreal = zrealUpdated;
            zimag = zimaginaryUpdated;
            iter++;
            if(iter==MAX_ITERATIONS)
                return -1;
        }
        return iter;
    }
    public String toString(){
        return "BurningShip";
    }
}


