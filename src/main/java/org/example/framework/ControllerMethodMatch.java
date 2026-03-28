
package org.example.framework;

import java.lang.reflect.Method;
import java.util.regex.Matcher;

public record ControllerMethodMatch(Class<?> clazz, Method method, Matcher matcher) {}
