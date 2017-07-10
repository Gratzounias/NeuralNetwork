/*
Georgios-Xristos Litsas
*/
package org.sunspotworld;

import java.util.Random;
/**
 *
 * @author admin
 */
public class GaussianGenerator {

    private Random random = new Random();
    private UnsupportedMath mth= new UnsupportedMath();

    

    public double generateMinusOneOne() { 
        while (true) {
            double dice = random.nextInt(200);
            if (dice == 0) {
                continue;
            }
            double r = dice * 0.01 - 1;
            return r;
        }
    }

    public double generateGaussianValue() {
        while (true) {
            double u = generateMinusOneOne();
            double v = generateMinusOneOne();
            double r = u * u + v * v;

            if (r == 0 || r >= 1) {
                continue;
            }
            double c = u*Math.sqrt(-2 *  mth.log(r)/ r);
            return c;
        }
    }

    //creat-a-gaussian-random-generator-with-a-mean-and-standard-deviation
    public double generateGaussianValue(double mean, double stddev) {
        double x = generateGaussianValue();  // (0,1)
        double result = x * stddev + mean;
        return result;
    }
    
    public double generateUniformValue() {
        double x = random.nextInt(100);
        return x;
    }
}
