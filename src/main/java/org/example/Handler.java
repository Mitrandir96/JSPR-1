package org.example;

import java.io.BufferedOutputStream;

public interface Handler {

    void handle (Request request, BufferedOutputStream bufferedOutputStream);
}
