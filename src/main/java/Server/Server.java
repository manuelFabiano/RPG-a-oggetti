package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 1234;
    public static void main(String[] args){
        try {
            GestoreDb gestoreDb = new GestoreDb("mongodb://localhost:27017", "RPG", "Players");

            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server.Server avviato. In attesa di connessioni...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nuova connessione da: " + clientSocket.getInetAddress());

                // Crea un nuovo thread per gestire la connessione del client
                GestoreClient gestoreClient = new GestoreClient(clientSocket, gestoreDb);
                Thread thread = new Thread(gestoreClient);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
