package org.example;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.util.Dictionary;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class Server {



    ConcurrentHashMap<String, ConcurrentHashMap<String, Handler>> handlers = new ConcurrentHashMap<>();



    final List<String> validPaths = List.of("/index.html", "/spring.svg", "/spring.png", "/resources.html", "/styles.css", "/app.js", "/links.html", "/forms.html", "/classic.html", "/events.html", "/events.js");

    public void listen(int port)  {

        try (var serverSocket = new ServerSocket(port)) {
            ExecutorService executorService = Executors.newFixedThreadPool(64);

            System.out.print("Server starting\n");

            while (true) {
                System.out.print("Accepting client\n");
                Socket socket = serverSocket.accept();
                executorService.submit(() -> requestProcess(socket));
            }
        } catch (Exception e) {
            handle(e);
        }
    }

    private void requestProcess(Socket socket) {
        try (
                final var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                final var out = new BufferedOutputStream(socket.getOutputStream());
        ) {
            System.out.println(Thread.currentThread().getName());

            final var requestLine = in.readLine();
            final var parts = requestLine.split(" ");

            if (parts.length != 3) {
                badRequest(out);
                return;
            }

            final var request = Request.createRequest(parts[0], parts[1]);
//            System.out.println(request.getQueryParam("id"));
            System.out.println(request.getQueryParams());
            System.out.println(request.getQueryParam("id"));

            if(!handlers.containsKey(request.getMethod())) {
                notFound(out);
                return;
            }

            var methodHandlers = handlers.get(request.getMethod());

            if (!methodHandlers.containsKey(request.getPath())) {
                notFound(out);
                return;
            }

            var handler = methodHandlers.get(request.getPath());

            if (handler == null) {
                notFound(out);
                return;
            }

            handler.handle(request, out);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void notFound(BufferedOutputStream out) throws  IOException{
        out.write((
                "HTTP/1.1 404 Not Found\r\n" +
                        "Content-Length: 0\r\n" +
                        "Connection: close\r\n" +
                        "\r\n"
        ).getBytes());
        out.flush();
    }

    private void badRequest(BufferedOutputStream out) throws  IOException{
        out.write((
                "HTTP/1.1 400 Bad Request\r\n" +
                        "Content-Length: 0\r\n" +
                        "Connection: close\r\n" +
                        "\r\n"
        ).getBytes());
        out.flush();
    }

    private void handle(Exception e) {
        if (!(e instanceof SocketException)) {
            e.printStackTrace();
        }
    }

    public void addHandler(String method, String path, Handler handler) {

        handlers.putIfAbsent(method, new ConcurrentHashMap<>());
        handlers.get(method).put(path, handler);
    }
}

