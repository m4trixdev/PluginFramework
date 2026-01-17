package dev.m4trix.framework.util;

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

public final class ValidationUtil {

    private ValidationUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static <T> T requireNonNull(T obj, String name) {
        if (obj == null) {
            throw new IllegalArgumentException(name + " cannot be null");
        }
        return obj;
    }

    public static String requireNonEmpty(String str, String name) {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException(name + " cannot be null or empty");
        }
        return str;
    }

    public static <T> Collection<T> requireNonEmpty(Collection<T> collection, String name) {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException(name + " cannot be null or empty");
        }
        return collection;
    }

    public static void requireTrue(boolean condition, String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void requireTrue(boolean condition, Supplier<String> messageSupplier) {
        if (!condition) {
            throw new IllegalArgumentException(messageSupplier.get());
        }
    }

    public static void requireFalse(boolean condition, String message) {
        if (condition) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void requireInRange(int value, int min, int max, String name) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(
                name + " must be between " + min + " and " + max + ", got " + value
            );
        }
    }

    public static void requireInRange(long value, long min, long max, String name) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(
                name + " must be between " + min + " and " + max + ", got " + value
            );
        }
    }

    public static void requirePositive(int value, String name) {
        if (value <= 0) {
            throw new IllegalArgumentException(name + " must be positive, got " + value);
        }
    }

    public static void requirePositive(long value, String name) {
        if (value <= 0) {
            throw new IllegalArgumentException(name + " must be positive, got " + value);
        }
    }

    public static void requireNonNegative(int value, String name) {
        if (value < 0) {
            throw new IllegalArgumentException(name + " cannot be negative, got " + value);
        }
    }

    public static void requireNonNegative(long value, String name) {
        if (value < 0) {
            throw new IllegalArgumentException(name + " cannot be negative, got " + value);
        }
    }

    public static <K, V> Map<K, V> requireNonEmpty(Map<K, V> map, String name) {
        if (map == null || map.isEmpty()) {
            throw new IllegalArgumentException(name + " cannot be null or empty");
        }
        return map;
    }
}