package org.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Request {

    private final Map<String, List<String>> query;


    private final String method;

    private String path;

    public Request(String method) {
        this.method = method;
        this.query = new HashMap<>();
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getQueryParam(String name) {
        var list = this.query.get(name);
        return list.toString();
    }

    public String getQueryParams() {
        return this.query.toString();
    }

    public static Request createRequest(String method, String url) {
        Request req = new Request(method);
        req.parsePath(url);
        req.parseQuery(url);
        return req;
    }

    private void parseQuery(String url) {

        if (url.indexOf('?') == -1) {
            return;
        }
        var splittedPath = url.split("\\?")[1];
        var query = splittedPath.split("#")[0];
        var parameters = query.split("&");
        for (int i = 0; i < parameters.length; i++) {
            var keyValue = parameters[i].split("=");
            if (this.query.containsKey(keyValue[0])) {
                this.query.get(keyValue[0]).add(keyValue[1]);
            } else {
                List<String> list = new ArrayList<>();
                list.add(keyValue[1]);
                this.query.put(keyValue[0], list);
            }
        }
    }

    private void parsePath(String url) {
        var splittedPath = url.split("\\?")[0];
        this.path = splittedPath;
    }
}
