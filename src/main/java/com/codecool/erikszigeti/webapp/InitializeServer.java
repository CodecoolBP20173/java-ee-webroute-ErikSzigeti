package com.codecool.erikszigeti.webapp;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpServer;

public class InitializeServer {

    public static void main(String[] args) throws Exception {
        Class<RequestHandler> requestHandlerClass = RequestHandler.class;
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        for (Method method : requestHandlerClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(WebRoute.class)) {
                Annotation annotation = method.getAnnotation(WebRoute.class);
                WebRoute webRoute = (WebRoute) annotation;
                server.createContext(webRoute.route(), new RequestHandler(webRoute.route()));
            }
        }
        server.setExecutor(Executors.newFixedThreadPool(6));
        server.start();
    }
}
