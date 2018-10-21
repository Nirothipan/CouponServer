package org.acardo.coupon;

import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.net.httpserver.HttpServer;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;

// The main class which starts the HTTP Server.
public class CouponServer {

    public static void main(String[] args) throws IOException {

        HttpServer couponServer = createHttpServer();
        couponServer.start();
        System.out.println("Coupon Server Successfully !!!  WADL available at " + getURI() + "application.wadl");

    }

    private static HttpServer createHttpServer() throws IOException {
        ResourceConfig resourceConfig = new PackagesResourceConfig("org.acardo.coupon");
        return HttpServerFactory.create(getURI(), resourceConfig);
    }

    private static URI getURI() {
        return UriBuilder.fromUri("http://" + getHostName() + "/").port(8080).build();
    }

    private static String getHostName() {
        String hostName = "localhost";
        try {
            hostName = InetAddress.getLocalHost().getCanonicalHostName();
        } catch (UnknownHostException e) {
            // ideally we should use log in production.
            e.printStackTrace();
        }
        return hostName;
    }
}