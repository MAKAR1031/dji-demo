package ru.makar.demo.jdi.application;

import java.util.concurrent.TimeUnit;

public class Application {
    public static void main(String[] args) throws InterruptedException {
        String myVar = "Hello!";
        //noinspection InfiniteLoopStatement
        while (true) {
            System.out.println(myVar);
            TimeUnit.SECONDS.sleep(1);
        }
    }
}
