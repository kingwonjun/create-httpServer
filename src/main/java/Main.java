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
            for (int i = 0; i < requestLine.length(); i++) {
                if (requestLine.charAt(i) == '/' && requestLine.charAt(i + 1) == ' ') {
                    out.write("HTTP/1.1 200 OK\r\n\r\n".getBytes(StandardCharsets.UTF_8));
                    break;
                }
                if (i == requestLine.length() - 1) {
                    out.write("HTTP/1.1 404 Not Found\r\n\r\n".getBytes(StandardCharsets.UTF_8));
                    break;
                }
            }
            out.flush();

            System.out.println("accepted new connection");
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }
}
