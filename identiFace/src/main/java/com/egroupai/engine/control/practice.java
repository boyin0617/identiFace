package com.egroupai.engine.control;

import java.util.Scanner;

public class practice {

 /**
  * @param args
  */
 public static void main(String[] args) {
  // TODO Auto-generated method stub
  double w = 60, h =1.7;
  double x = bmi(w,h);
  System.out.println("w = "+w);
  System.out.println("h = "+h);
  System.out.println("bmi = " +x);
  }
  static double bmi(double w, double h){
  return w / (h*h);
  }

 }