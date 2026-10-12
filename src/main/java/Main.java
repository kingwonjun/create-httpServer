import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {
        // You can use print statements as follows for debugging, they'll be visible when running tests.
        System.out.println("Logs from your program will appear here!");

        // TODO: Uncomment the code below to pass the first stage

        try {
            ServerSocket serverSocket = new ServerSocket(4221);


            serverSocket.setReuseAddress(true);
            Socket clientSocket = serverSocket.accept();

            InputStream input = clientSocket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
            OutputStream out = clientSocket.getOutputStream();
            String requestLine = reader.readLine();
            String path1 = "";
            String path2 = "";
            int start = requestLine.indexOf('/');
            int end = start + 1;
            while (true) {
                if (requestLine.charAt(end) == '/') {
                    path1 = requestLine.substring(start, end + 1);
                    start = end + 1;
                    end = requestLine.indexOf(' ', start);
                    path2 = requestLine.substring(start, end);
                    break;
                }
                if (requestLine.charAt(end) == ' ') {
                    path1 = requestLine.substring(start, end);
                    System.out.println(path1);
                    break;
                }
                end++;
            }

            if (path1.equals("/echo/")) {
                int pathSize = path2.length();
                out.write("HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length:".getBytes(StandardCharsets.UTF_8));
                out.write(String.valueOf(pathSize).getBytes(StandardCharsets.UTF_8));
                out.write("\r\n\r\n".getBytes(StandardCharsets.UTF_8));
                out.write(path2.getBytes(StandardCharsets.UTF_8));
            } else if(path1.equals("/user-agent")) {
                requestLine = reader.readLine();
                requestLine = reader.readLine();
                System.out.println(requestLine);
                start = 12;
                int idx = requestLine.length();
                out.write("HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length:".getBytes(StandardCharsets.UTF_8));
                out.write(String.valueOf(idx - start).getBytes(StandardCharsets.UTF_8));
                out.write("\r\n\r\n".getBytes(StandardCharsets.UTF_8));
                out.write(requestLine.substring(start, idx).getBytes(StandardCharsets.UTF_8));
            } else if (path1.equals("/")) {
                out.write("HTTP/1.1 200 OK\r\n\r\n".getBytes(StandardCharsets.UTF_8));
            } else {
                out.write("HTTP/1.1 404 Not Found\r\n\r\n".getBytes(StandardCharsets.UTF_8));
            }
            out.flush();

            System.out.println("accepted new connection");
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }
}
