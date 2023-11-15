package com.annotator;


public class Main {
    public static void main(String[] args) {
        while (true) {
            System.out.println("Hello from worker!");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ignored) {}
        }
    }
}