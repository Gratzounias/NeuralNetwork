/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sunspotworld;

/**
 *
 * @author admin
 */
public class UnsupportedMath {
    
    // how-to-calculate-pow-in-java-me
    private double pow(double base, int exp) {
        if (exp == 0) {
            return 1;
        }
        double res = base;
        for (; exp > 1; --exp) {
            res *= base;
        }
        return res;
    }

    // how-to-calculate-logarithm-in-java-me
    public double log(double x) {
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
    
    
    
}
