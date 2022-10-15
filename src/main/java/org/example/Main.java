package org.example;
import java.io.IOException;



public class Main {
    public static void main(String[] args) {
        final var server = new Server();


        server.addHandler("GET", "/messages", (request, out) -> {
            var message = "Hello from GET messages";
            try {
                out.write((
                        "HTTP/1.1 200 OK\r\n" +
                                "Content-Type: " + "text/plain" + "\r\n" +
                                "Content-Length: " + message.length() + "\r\n" +
                                "Connection: close\r\n" +
                                "\r\n" +
                                message


                ).getBytes());

                out.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
       server.addHandler("POST", "/messages", (request, out) -> {
           var message = "Hello from POST messages";
           try {
               out.write((
                       "HTTP/1.1 200 OK\r\n" +
                               "Content-Type: " + "text/plain" + "\r\n" +
                               "Content-Length: " + message.length() + "\r\n" +
                               "Connection: close\r\n" +
                               "\r\n" +
                               message


               ).getBytes());

               out.flush();
           } catch (IOException e) {
               throw new RuntimeException(e);
           }
       });

        server.listen(9999);

    }
}