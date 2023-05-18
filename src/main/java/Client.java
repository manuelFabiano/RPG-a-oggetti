import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private Scanner scanner;

    public static void main(String[] args) {
        String serverAddress = "localhost"; // Indirizzo del server
        int port = 1234; // Porta del server

        Client client = new Client(serverAddress, port);
        client.start();
    }

    public Client(String serverAddress, int port) {
        try {
            socket = new Socket(serverAddress, port);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            scanner = new Scanner(System.in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        try {
            System.out.println("Benvenuto a RPG a OGGETTI1!!!");
            System.out.println("Scegli un'opzione:");
            System.out.println("1. Accedi");
            System.out.println("2. Registrati");
            System.out.println("3. Esci");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    accedi();
                    break;
                case 2:
                    registrati();
                    break;
                case 3:
                    esci();
                    break;
                default:
                    System.out.println("Opzione non valida.");
                    start();
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void accedi() throws IOException {
        //Mando al server che voglio accedere
        output.println("LOGIN");

        System.out.println("Hai scelto di accedere.");
        // Logica per l'accesso


    }

    private void registrati() throws IOException {
        //Mando al server che voglio registrarmi
        output.println("REGISTER");
        System.out.println("Hai scelto di registrarti.");
        // Logica per la registrazione
        System.out.println("Inserisci il tuo username:");
        String username = scanner.nextLine();

        System.out.println("Inserisci una password:");
        String password = scanner.nextLine();

        //Mando username e password al server
        output.println(username);
        output.println(password);
    }

    private void esci() {
        System.out.println("Grazie per aver giocato a RPG a OGGETTI. Arrivederci!");
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }


}
