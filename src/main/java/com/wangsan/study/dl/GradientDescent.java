package com.wangsan.study.dl;

/**
 * created by wangsan on 2020/3/16 in project of example .
 *
 * @author wangsan
 * @date 2020/3/16
 * @see <a href="https://blog.csdn.net/qq_33612918/article/details/88117304">js</a>
 */
public class GradientDescent {
    double a;
    double b;
    double[] xs;
    double[] ys;
    double step;
    double inaccuracy;
    int count;

    public static void main(String[] args) {
        GradientDescent gd = new GradientDescent();
        gd.a = 1;
        gd.b = 1;
        gd.xs = new double[] {1.0, 2.3, 3.6};
        gd.ys = new double[] {7.0, 9.3, 11.2};
        gd.step = 0.01;
        gd.inaccuracy = 1e-3;
        gd.count = 1000;

        // normalize

        gd.train();
        System.out.println(String.format("y=%sx+%s", gd.a, gd.b));
    }

    public void train() {
        for (int i = 0; i < count; i++) {
            double da = da(a, b, xs, ys);
            double db = db(a, b, xs, ys);
            System.out.println("da: " + da);
            System.out.println("db: " + da);
            a = a - step * da;
            b = b - step * db;
            double cost = cost(a, b, xs, ys);
            System.out.println("cost: " + cost + " a: " + a + " b: " + b);

            if (Math.abs(da) < inaccuracy && Math.abs(db) < inaccuracy) {
                System.out.println("count: " + count);
                return;
            }

            if (Math.abs(cost) < inaccuracy) {
                System.out.println("count: " + count);
                return;
            }

        }
    }

    public double cost(double a, double b, double[] xs, double[] ys) {
        double sum = 0.0;
        for (int i = 0; i < xs.length; i++) {
            sum += (a * xs[i] + b - ys[i]) * (a * xs[i] + b - ys[i]);
        }
        return sum;
    }

    public double da(double a, double b, double[] xs, double[] ys) {
        double sum = 0.0;
        for (int i = 0; i < xs.length; i++) {
            sum += (a * xs[i] + b - ys[i]) * xs[i];
        }
        return sum;
    }

    public double db(double a, double b, double[] xs, double[] ys) {
        double sum = 0.0;
        for (int i = 0; i < xs.length; i++) {
            sum += (a * xs[i] + b - ys[i]);
        }
        return sum;
    }
}
