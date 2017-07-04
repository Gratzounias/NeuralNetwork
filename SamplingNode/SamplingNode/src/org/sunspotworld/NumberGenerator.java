/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sunspotworld;

import java.util.Random;

/**
 *
 * @author admin
 */
public class NumberGenerator {

    private Random random = new Random();

    // http://www.duanqu.tech/questions/657027/how-to-calculate-logarithm-in-java-me
    private static double pow(double base, int exp) {
        if (exp == 0) {
            return 1;
        }
        double res = base;
        for (; exp > 1; --exp) {
            res *= base;
        }
        return res;
    }

    // http://www.duanqu.tech/questions/657027/how-to-calculate-logarithm-in-java-me
    public static double log(double x) {
        long l = Double.doubleToLongBits(x);
        long exp = ((0x7ff0000000000000L & l) >> 52) - 1023;
        double man = (0x000fffffffffffffL & l) / (double) 0x10000000000000L + 1.0;
        double lnm = 0.0;
        double a = (man - 1) / (man + 1);
        for (int n = 1; n < 7; n += 2) {
            lnm += pow(a, n) / n;
        }
        return 2 * lnm + exp * 0.69314718055994530941723212145818;
    }

    public double generateMinusOneOne() { // uniform [-1,1)
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

            double c = u*Math.sqrt(-2 * log(r) / r);
            return c;
        }
    }

    //https://stackoverflow.com/questions/19944111/creating-a-gaussian-random-generator-with-a-mean-and-standard-deviation
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
