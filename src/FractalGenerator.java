
    import java.awt.geom.Rectangle2D;


    /**
     * This class provides the common interface and operations for fractal
     * generators that can be viewed in the Fractal Explorer.
     */
    public abstract class FractalGenerator {

        /**
         * This static helper function takes an integer coordinate and converts it
         * into a double-precision value corresponding to a specific range.  It is
         * used to convert pixel coordinates into double-precision values for
         * computing fractals, etc.
         *
         * @param rangeMin the minimum value of the floating-point range
         * @param rangeMax the maximum value of the floating-point range
         *
         * @param size the size of the dimension that the pixel coordinate is from.
         *        For example, this might be the image width, or the image height.
         *
         * @param coord the coordinate to compute the double-precision value for.
         *        The coordinate should fall in the range [0, size].
         */
        public static double getCoord(double rangeMin, double rangeMax,
                                      int size, int coord) {

            assert size > 0;  //проверка размера
            assert coord >= 0 && coord < size;  //проверка координат

            double range = rangeMax - rangeMin;
            return rangeMin + (range * (double) coord / (double) size);
        }


        /**
         * Устанавливает указанный прямоугольник, чтобы он содержал начальный диапазон, подходящий для
         *  генерируемого фрактала.
         */
        public abstract void getInitialRange(Rectangle2D.Double range);


        /**
         * Обновляет текущий диапазон, чтобы он был центрирован по указанным координатам,
         *   и для увеличения или уменьшения с указанным коэффициентом масштабирования.
         */
        public void recenterAndZoomRange(Rectangle2D.Double range,
                                         double centerX, double centerY, double scale) {

            double newWidth = range.width * scale;
            double newHeight = range.height * scale;

            range.x = centerX - newWidth / 2;
            range.y = centerY - newHeight / 2;
            range.width = newWidth;
            range.height = newHeight;
        }


        /**
         * Учитывая координату <em> x </em> + <em> iy </em> на комплексной плоскости,
         *  вычисляет и возвращает количество итераций перед фракталом
         *  функция выходит за пределы ограничивающей области для этой точки. Дело в том, что
         *  не исчезает, пока не будет указан предел итераций
         *      * с результатом -1.
         */
        public abstract int numIterations(double x, double y);
    }

