import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GestoreClient implements Runnable {
    private Socket clientSocket;
    private GestoreDb gestoreDb;
    private BufferedReader input;
    private PrintWriter output;
    int choice;

    public GestoreClient(Socket clientSocket, GestoreDb gestoreDb) {
        this.clientSocket = clientSocket;
        this.gestoreDb = gestoreDb;
        try {
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            output = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            // Logica di gestione della comunicazione con il client
            String clientMessage;
            while ((clientMessage = input.readLine()) != null) {
                System.out.println("Messaggio dal client: " + clientMessage);
                switch (clientMessage) {
                    case "LOGIN":
                        break;
                    case "REGISTER":
                        gestoreRegister();
                        break;
                    default:
                        break;
                }
            }

            // Chiudi le risorse
            input.close();
            output.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void gestoreRegister() throws IOException{
        String username = input.readLine();
        System.out.println(username);
        String password = input.readLine();
        System.out.println(password);

        gestoreDb.inserisciUtente(username,password);
    }
}
