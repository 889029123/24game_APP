package tw.com.game;

import java.util.StringTokenizer;

/**
 * Created by ASUS on 2016/9/23.
 */
class Calculator {
    int numerator;  // 分子
    int denominator; // 分母

    Calculator() {
    }

    Calculator(int a, int b) {
        if (a == 0) {
            numerator = 0;
            denominator = 1;
        } else {
            setNumeratorAndDenominator(a, b);
        }
    }

    void setNumeratorAndDenominator(int a, int b) {  // 設置分子和分母
        int c = f(Math.abs(a), Math.abs(b));         // 計算最大公因數
        numerator = a / c;
        denominator = b / c;
        if (numerator < 0 && denominator < 0) {
            numerator = -numerator;
            denominator = -denominator;
        }
    }

    int getNumerator() {
        return numerator;
    }
    int getDenominator() {
        return denominator;
    }
    int f(int a, int b) {  // 求a和b的最大公因數
        if (a < b) {
            int c = a;
            a = b;
            b = c;
        }
        int r = a % b;
        while (r != 0) {
            a = b;
            b = r;
            r = a % b;
        }
        return b;
    }

    Calculator add(Calculator r) {  // 加法運算
        int a = r.getNumerator();
        int b = r.getDenominator();
        int newNumerator = numerator * b + denominator * a;
        int newDenominator = denominator * b;
        Calculator result = new Calculator(newNumerator, newDenominator);
        return result;
    }

    Calculator sub(Calculator r) {  // 减法運算
        int a = r.getNumerator();
        int b = r.getDenominator();
        int newNumerator = numerator * b - denominator * a;
        int newDenominator = denominator * b;
        Calculator result = new Calculator(newNumerator, newDenominator);
        return result;
    }

    Calculator muti(Calculator r) { // 乘法運算
        int a = r.getNumerator();
        int b = r.getDenominator();
        int newNumerator = numerator * a;
        int newDenominator = denominator * b;
        Calculator result = new Calculator(newNumerator, newDenominator);
        return result;
    }

    Calculator div(Calculator r) {  // 除法運算
        int a = r.getNumerator();
        int b = r.getDenominator();
        int newNumerator = numerator * b;
        int newDenominator = denominator * a;
        Calculator result = new Calculator(newNumerator, newDenominator);
        return result;
    }
    public String compute(String data1, String operation, String data2) {
        StringTokenizer fenxi = new StringTokenizer(data1, "/");
        int data1_1 = Integer.parseInt(fenxi.nextToken());
        int data1_2 = Integer.parseInt(fenxi.nextToken());
        fenxi = new StringTokenizer(data2, "/");
        int data2_1 = Integer.parseInt(fenxi.nextToken());
        int data2_2 = Integer.parseInt(fenxi.nextToken());

        Calculator r1 = new Calculator(data1_1, data1_2);
        Calculator r2 = new Calculator(data2_1, data2_2);

        Calculator result;
        int a = 0, b = 0;
        if (operation.equals("+")) {
            result = r1.add(r2);
            a = result.getNumerator();
            b = result.getDenominator();
        }

        if (operation.equals("-")) {
            result = r1.sub(r2);
            a = result.getNumerator();
            b = result.getDenominator();
        }

        if (operation.equals("*")) {
            result = r1.muti(r2);
            a = result.getNumerator();
            b = result.getDenominator();
        }

        if (operation.equals("/")) {
            result = r1.div(r2);
            a = result.getNumerator();
            b = result.getDenominator();
        }

        return a + "/" + b;
    }
}
