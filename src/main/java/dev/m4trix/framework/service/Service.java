package dev.m4trix.framework.service;

public interface Service {

    void start();

    void stop();

    default String getName() {
        return getClass().getSimpleName();
    }

    default boolean isRunning() {
        return false;
    }
}