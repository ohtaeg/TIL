package org.example.thread.performance.throughput;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Main {

    private static final String INPUT_FILE = "./multi-thread/src/main/resources/war_and_peace.txt";
    private static final int POOL_SIZE = 12;

    public static void main(String[] args) throws IOException {
        String text = new String(Files.readAllBytes(Paths.get(INPUT_FILE)));
        
        startServer(text);
    }

    private static void startServer(String text) throws IOException {
        // 고정된 스레드 수를 가진 풀 생성
        Executor executor = Executors.newFixedThreadPool(POOL_SIZE);
        // 요청 대기열 크기가 0인 HTTP 서버 생성, 모든 요청이 스레드 풀 대기열에 들어가기 위함
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.setExecutor(executor);

        // 검색 요청 핸들링
        server.createContext("/search", new WordCountHandler(text));

        server.start();
    }

    private static class WordCountHandler implements HttpHandler {
        private String text;

        public WordCountHandler(String text) {
            this.text = text;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();
            String[] keyValues =  query.split("=");
            String action = keyValues[0];
            String word = keyValues[1];

            if (!action.equals("word")) {
                exchange.sendResponseHeaders(400, 0);
                return;
            }

            long count = countWord(word);

            byte[] response = Long.toString(count).getBytes();
            exchange.sendResponseHeaders(200, response.length);
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(response);
            outputStream.close();
        }

        private long countWord(String word) {
            long count = 0;
            // 단어의 위치
            int index = 0;

            while (index >= 0) {
                index = text.indexOf(word, index);

                if (index >= 0) {
                    count++;
                    index++;
                }
            }
            return count;
        }
    }
}
