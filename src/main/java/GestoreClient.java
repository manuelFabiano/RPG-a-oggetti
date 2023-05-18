import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GestoreClient implements Runnable {
    private final Socket clientSocket;
    private final GestoreDb gestoreDb;
    private BufferedReader input;
    private PrintWriter output;

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
            stampaMenuLogin();
            // Logica di gestione della comunicazione con il client
            String clientMessage;
            while ((clientMessage = input.readLine()) != null) {
                System.out.println("Messaggio dal client: " + clientMessage);
                switch (clientMessage) {
                    case "1":
                        gestioneLogin();
                        break;
                    case "2":
                        gestioneRegister();
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

    private void MenuGioco() throws IOException{
        stampaMenuGioco();
        String clientMessage;
        while ((clientMessage = input.readLine()) != null) {
            System.out.println("Messaggio dal client: " + clientMessage);
            switch (clientMessage) {
                case "1":
                    //nuovapartita
                    break;
                case "2":
                    //continua
                    break;
                case "3":
                    //classifica
                    break;
                case "4":
                    //esci
                    break;
                default:
                    break;
            }
        }

    }

    private void gestioneLogin() throws IOException{
        output.println("Inserisci l'username:\nPASS");
        String username = input.readLine();
        System.out.println(username);
        output.println("Inserisci una password\nPASS");
        String password = input.readLine();
        System.out.println(password);

        if(gestoreDb.login(username, password)){
            //stampamenuprincipale
            System.out.println("menu");
        }
    }

    private void gestioneRegister() throws IOException{
        output.println("Inserisci l'username:\nPASS");
        String username = input.readLine();
        System.out.println(username);
        output.println("Inserisci una password\nPASS");
        String password = input.readLine();
        System.out.println(password);

        int res = gestoreDb.registraUtente(username, password);
        if (res == 1){
            //stampamenuprincipale
            System.out.println("menu");
        }
    }

    private void stampaMenuLogin() throws IOException{
        output.println("Benvenuto a RPG a OGGETTI!\n" +
                "Scegli un'opzione:\n" +
                "1. Accedi\n" +
                "2. Registrati\n" +
                "3. Esci\n"+
                "PASS");
    }

    private void stampaMenuGioco(){
        output.println("Cosa vuoi fare?\n" +
                "Scegli un'opzione:\n" +
                "1. Nuova partita\n" +
                "2. Continua\n" +
                "3. Classifica\n" +
                "4. Esci\n" +
                "PASS");
    }
}
