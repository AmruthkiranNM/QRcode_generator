import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 5000);
        System.out.println("Connected to server.");

        // Input and output streams
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        // Thread to receive messages from server
        Thread receiveThread = new Thread(() -> {
            String message;
            try {
                while ((message = in.readLine()) != null) {
                    if (message.equalsIgnoreCase("exit")) {
                        System.out.println("Server has disconnected.");
                        break;
                    }
                    System.out.println("Server: " + message);
                }
            } catch (IOException e) {
                System.out.println("Connection closed.");
            }
        });

        // Thread to send messages to server
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

