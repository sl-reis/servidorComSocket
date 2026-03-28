package org.example.framework;

import java.lang.reflect.Method;

public record ControllerMethod(Class<?> clazz, Method method) {
}
