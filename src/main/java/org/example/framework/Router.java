
package org.example.framework;

import org.example.framework.annotation.*;
import org.example.framework.http.*;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.regex.*;

public class Router {

    public ControllerMethodMatch find(HttpRequest request) {

        Reflections reflections = new Reflections("org.example");
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(MyController.class);

        Class<? extends Annotation> mappingAnnotation;
        if (request.getMethod() == HttpMethod.GET) {
            mappingAnnotation = GetMapping.class;
        } else if (request.getMethod() == HttpMethod.POST) {
            mappingAnnotation = PostMapping.class;
        } else if (request.getMethod() == HttpMethod.DELETE) {
            mappingAnnotation = DeleteMapping.class;
        } else if (request.getMethod() == HttpMethod.PATCH) {
            mappingAnnotation = PatchMapping.class;
        } else {
            throw new RuntimeException("Método HTTP não suportado: " + request.getMethod());
        }

        return controllers.stream()
                .flatMap(c -> Arrays.stream(c.getDeclaredMethods())
                        .filter(m -> m.isAnnotationPresent(mappingAnnotation))
                        .map(m -> new ControllerMethod(c, m)))
                .map(cm -> mapControllerMethod(cm, request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Rota não encontrada: " + request.getPath()));
    }

    private ControllerMethodMatch mapControllerMethod(ControllerMethod cm, HttpRequest request) {

        String base = cm.clazz().getAnnotation(MyController.class).path();
        String methodPath;

        if (cm.method().isAnnotationPresent(GetMapping.class)) {
            methodPath = cm.method().getAnnotation(GetMapping.class).path();
        } else if (cm.method().isAnnotationPresent(PostMapping.class)) {
            methodPath = cm.method().getAnnotation(PostMapping.class).path();
        } else if (cm.method().isAnnotationPresent(DeleteMapping.class)) {
            methodPath = cm.method().getAnnotation(DeleteMapping.class).path();
        } else if (cm.method().isAnnotationPresent(PatchMapping.class)) {
            methodPath = cm.method().getAnnotation(PatchMapping.class).path();
        } else {
            throw new RuntimeException("Método sem anotação de mapeamento: " + cm.method().getName());
        }

        String fullPath = base + methodPath;

        String regex = fullPath.replaceAll("\\{[^/]+}", "([^/]+)");
        regex = "^" + regex + "$";

        Matcher matcher = Pattern.compile(regex).matcher(request.getPath());

        if (matcher.matches()) {
            return new ControllerMethodMatch(cm.clazz(), cm.method(), matcher);
        }

        return null;
    }
}