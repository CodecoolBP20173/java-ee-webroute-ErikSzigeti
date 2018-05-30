package com.codecool.erikszigeti.webapp;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RequestHandler implements HttpHandler {
    private String route;

    public RequestHandler() {
    }

    RequestHandler(String route) {
        this.route = route;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Class<RequestHandler> requestHandlerClass = RequestHandler.class;
        for (Method method : requestHandlerClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(WebRoute.class)) {
                Annotation annotation = method.getAnnotation(WebRoute.class);
                WebRoute webRoute = (WebRoute) annotation;
                if (webRoute.route().equals(route)) {
                    try {
                        method.invoke(requestHandlerClass.newInstance(), httpExchange);
                    } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @WebRoute(route = "/main")
    private void handleMainRoute(HttpExchange requestData) throws IOException {
        String responseString = "This is the main page";
        requestData.sendResponseHeaders(200, responseString.getBytes().length);
        OutputStream os = requestData.getResponseBody();
        os.write(responseString.getBytes());
        os.close();
    }

    @WebRoute(route = "/test")
    private void handleTestRoute(HttpExchange requestData) throws IOException {
        String responseString = "This is the test page";
        requestData.sendResponseHeaders(200, responseString.getBytes().length);
        OutputStream os = requestData.getResponseBody();
        os.write(responseString.getBytes());
        os.close();
    }


}
