package org.example;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Request {

    private final String method;
    private String path;
    private final String url;
    private List<NameValuePair> queryParams;


    public Request(String method, String url) {
        this.method = method;
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getQueryParam(String name) {
        var builder = new StringBuilder();
        for (NameValuePair pair : this.queryParams) {
            if (pair.getName().equals(name)) {
                builder.append(pair + " ");
            }

        }
        return builder.toString();

    }

    public String getQueryParams() {
        return this.queryParams.toString();
    }

    public static Request createRequest(String method, String url) {
        Request req = new Request(method, url);
        req.parsePath(url);
        req.parseQuery(url);
        return req;
    }

    private void parseQuery(String url) {

        if (url.indexOf('?') == -1) {
            return;
        }
        var splittedPath = url.split("\\?")[1];
        var unparsedQuery = splittedPath.split("#")[0];
        this.queryParams = URLEncodedUtils.parse(unparsedQuery, StandardCharsets.UTF_8);
    }

    private void parsePath(String url) {
        var splittedPath = url.split("\\?")[0];
        this.path = splittedPath;


    }



}
