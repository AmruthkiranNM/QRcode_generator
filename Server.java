import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("Server started. Waiting for client...");

        Socket socket = serverSocket.accept();
        System.out.println("Client connected: " + socket.getInetAddress());

        // Input and output streams
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        // Thread to read messages from client
        Thread receiveThread = new Thread(() -> {
            String message;
            try {
                while ((message = in.readLine()) != null) {
                    if (message.equalsIgnoreCase("exit")) {
                        System.out.println("Client has disconnected.");
                        break;
                    }
                    System.out.println("Client: " + message);
                }
            } catch (IOException e) {
                System.out.println("Connection closed.");
            }
        });

        // Thread to send messages to client
        Thread sendThread = new Thread(() -> {
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
            String message;
            try {
                while ((message = console.readLine()) != null) {
                    out.println(message);
                    if (message.equalsIgnoreCase("exit")) {
                        socket.close();
                        break;
                    }
                }
            } catch (IOException e) {
                System.out.println("Error sending message.");
            }
        });

        receiveThread.start();
        sendThread.start();
    }
}

