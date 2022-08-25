package com.utils;

import com.model.annotations.MyAutowired;
import com.model.annotations.MySingleton;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class SingletonUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(SingletonUtil.class);
    private static final List<Object> CACHE = new ArrayList<>();

    private SingletonUtil() {
    }

    public static List<Object> getCache(@NonNull final String packageName) {
        final Reflections reflections = new Reflections(packageName);
        final Set<Class<?>> setSingletonClasses = reflections.getTypesAnnotatedWith(MySingleton.class);
        setSingletonClasses.stream()
                .flatMap(x -> Arrays.stream(x.getDeclaredConstructors()))
                .forEach(SingletonUtil::createObject);
        return CACHE.stream()
                .peek(SingletonUtil::setInstance)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    public static void printCache(@NonNull final List<Object> cache) {
        for (Object object : cache) {
            for (Constructor<?> constructor : object.getClass().getDeclaredConstructors()) {
                constructor.setAccessible(true);
                if (constructor.isAnnotationPresent(MyAutowired.class) && constructor.getParameterCount() == 1) {
                    final Class<?> classes = constructor.getAnnotation(MyAutowired.class).value();
                    final Field[] fields = classes.getDeclaredFields();
                    for (Field field : fields) {
                        if (field.getName().equals("instance")) {
                            field.setAccessible(true);
                            LOGGER.info("SERVICE: {} REPOSITORY: {}", object, field.get(classes));
                        }
                    }
                } else {
                    LOGGER.info("REPOSITORY: {}", object);
                }
            }
        }
    }

    @SneakyThrows
    private static void createObject(@NonNull final Constructor<?> constructor) {
        constructor.setAccessible(true);
        if (constructor.getParameterCount() == 0) {
            for (Object object : CACHE) {
                if (object.getClass().equals(constructor.getDeclaringClass())) {
                    return;
                }
            }
            CACHE.add(constructor.newInstance());
        } else {
            for (Object object : CACHE) {
                if (constructor.getAnnotation(MyAutowired.class).value().equals(object.getClass())) {
                    CACHE.add(constructor.newInstance(object));
                    return;
                }
            }
            final Constructor<?> innerConstructor = constructor.getAnnotation(MyAutowired.class).value().getDeclaredConstructor();
            innerConstructor.setAccessible(true);
            final Object repository = innerConstructor.newInstance();
            CACHE.add(constructor.newInstance(repository));
            CACHE.add(repository);
        }
    }

    @SneakyThrows
    private static void setInstance(@NonNull final Object object) {
        for (Field field : object.getClass().getDeclaredFields()) {
            if (field.getName().equals("instance")) {
                field.setAccessible(true);
                field.set(object.getClass(), object);
            }
        }
    }
}