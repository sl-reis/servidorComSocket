package org.example.framework;

import com.google.gson.Gson;
import org.example.framework.annotation.*;
import org.example.framework.http.*;

import java.lang.reflect.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;

public class Dispatcher {

    private final ConcurrentHashMap<String, Object> instances = new ConcurrentHashMap<>();

    public HttpResponse dispatch(ControllerMethodMatch found, HttpRequest req)
            throws Exception {

        Class<?> clazz = found.clazz();
        Method method = found.method();
        Matcher matcher = found.matcher();

        Object instance = instances.computeIfAbsent(
                clazz.getName(),
                k -> {
                    try {
                        Object obj = clazz.getDeclaredConstructor().newInstance();
                        createTreeFields(clazz, obj);
                        return obj;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        Object result;

        if (req.getMethod() == HttpMethod.GET) {
            result = invokeGet(instance, method, matcher);
            return HttpResponse.ok((String) result);
        } else if (req.getMethod() == HttpMethod.POST) {
            result = invokePost(instance, method, req);
            return HttpResponse.ok((String) result);
        } else if (req.getMethod() == HttpMethod.DELETE) {
            result = invokeDelete(instance, method, matcher);
            return HttpResponse.deleteSuccess((String) result);
        } else if (req.getMethod() == HttpMethod.PATCH) {
            result = invokePatch(instance, method, req, matcher);
            return HttpResponse.patchSuccess((String) result);
        } else {
            return HttpResponse.serverError("Ocorreu um erro ao processar a requisição");
        }
    }

    private void createTreeFields(Class<?> clazz, Object clazzInstance) throws Exception {
        for (Field field : clazz.getDeclaredFields()) {
            if (!field.isAnnotationPresent(Inject.class)) continue;

            Class<?> fieldType = field.getType();
            Object fieldInstance = instances.computeIfAbsent(
                    fieldType.getName(),
                    k -> {
                        try {
                            Object obj = fieldType.getDeclaredConstructor().newInstance();
                            createTreeFields(fieldType, obj);
                            return obj;
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
            );

            field.setAccessible(true);
            field.set(clazzInstance, fieldInstance);
            field.setAccessible(false);
        }
    }

    private Object invokeGet(Object instance, Method method, Matcher matcher)
            throws Exception {

        Parameter[] params = method.getParameters();

        if (params.length == 0) {
            return method.invoke(instance);
        }

        if (params.length == 1) {
            String raw = matcher.group(1);
            return method.invoke(instance, convert(raw, params[0].getType()));
        }

        throw new RuntimeException("GET suporta até 1 parâmetro");
    }

    private Object invokePost(Object instance, Method method, HttpRequest req)
            throws Exception {

        Parameter[] params = method.getParameters();

        if (params.length == 0) {
            return method.invoke(instance);
        }

        if (params.length == 1) {
            Object obj = new Gson().fromJson(req.getBody(), params[0].getType());
            return method.invoke(instance, obj);
        }

        throw new RuntimeException("POST suporta até 1 parâmetro");
    }

    private Object invokeDelete(Object instance, Method method, Matcher matcher)
            throws Exception {

        Parameter[] params = method.getParameters();

        if (params.length == 0) {
            return method.invoke(instance);
        }

        if (params.length == 1) {
            String raw = matcher.group(1);
            return method.invoke(instance, convert(raw, params[0].getType()));
        }

        throw new RuntimeException("DELETE suporta até 1 parâmetro");
    }

    private Object invokePatch(Object instance, Method method, HttpRequest req, Matcher matcher)
            throws Exception {

        Parameter[] params = method.getParameters();

        if (params.length == 0) {
            return method.invoke(instance);
        }

        if (params.length == 2) {
            String raw = matcher.group(1);
            Object param1 = convert(raw, params[0].getType());
            Object param2 = new Gson().fromJson(req.getBody(), params[1].getType());
            return method.invoke(instance, param1, param2);
        }

        throw new RuntimeException("PATCH suporta até 2 parâmetros. Um na URL e outro no corpo da requisição");
    }

    private Object convert(String raw, Class<?> type) {

        if (type == Integer.class || type == int.class) return Integer.valueOf(raw);
        if (type == Long.class || type == long.class) return Long.valueOf(raw);
        if (type == String.class) return raw;

        throw new RuntimeException("Tipo não suportado: " + type.getName());
    }
}