import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        String serverAddress = "localhost"; // Indirizzo IP del server
        int portNumber = 1234; // Porta del server

        try (Socket socket = new Socket(serverAddress, portNumber)) {
            System.out.println("Connessione al server riuscita...");

            // Creazione dei lettori e scrittori per comunicare con il server
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner scanner = new Scanner(System.in);

            String serverMessage;
            while ((serverMessage = input.readLine()) != null) {
                if(!serverMessage.equals("PASS")) {
                    System.out.println(serverMessage);
                }else{
                // Richiesta di input all'utente per rispondere al server
                String clientResponse = scanner.nextLine();

                // Invio della risposta al server
                output.println(clientResponse);
                svuotaSchermo();
                }
            }
            // Chiusura delle risorse
            output.close();
            input.close();
            scanner.close();
            System.out.println("risorse chiuse");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void svuotaSchermo() {
        String os = System.getProperty("os.name").toLowerCase();

        try {
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
