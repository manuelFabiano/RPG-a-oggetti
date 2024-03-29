package Server;

import org.bson.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class GestoreClient implements Runnable, InterfacciaGestoreClient {
    private final Socket clientSocket;
    private final GestoreDb gestoreDb;
    private BufferedReader input;
    private PrintWriter output;
    private Document utente;

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
            String clientMessage;
            boolean exit = false;
            while(!exit) {
                stampaMenuLogin();
                // Logica di gestione della comunicazione con il client
                clientMessage = ricevi();
                switch (clientMessage) {
                    case "1":
                        gestioneLogin();
                        break;
                    case "2":
                        gestioneRegister();
                        break;
                    case "3":
                        exit = true;
                        break;
                    default:
                        break;
                    }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // Chiudi le risorse
                input.close();
                output.close();
                clientSocket.close();
                System.out.println("Connessione conclusa con " + clientSocket.getInetAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void MenuGioco() throws IOException{
        boolean exit = false;
        String clientMessage;
        Gioco gioco;
        while(!exit) {
            stampaMenuGioco();
            clientMessage = ricevi();
                System.out.println("Messaggio dal client: " + clientMessage);
                switch (clientMessage) {
                    case "1":
                        gioco = new Gioco(this, gestoreDb, utente);
                        gioco.loop();
                        break;
                    case "2":
                        Document partita = null;
                        String chiavePartita;
                        do {
                            manda("Questi sono i tuoi salvataggi:");
                            manda(gestoreDb.stampaPartite(utente));
                            manda("Seleziona una partita da continuare(premi invio per tornare indietro):\nPASS");
                            chiavePartita = ricevi();
                            if (chiavePartita.isEmpty())
                                break;
                            partita = gestoreDb.trovaPartita(utente, chiavePartita);
                            if (partita == null)
                                manda("La partita che hai inserito non è valida");
                        } while (partita == null);
                        if(partita != null) {
                            gioco = new Gioco(this, gestoreDb, utente, partita, chiavePartita);
                            gioco.loop();
                        }
                        break;
                    case "3":
                        List<Document> classifica = gestoreDb.generaClassifica();
                        manda(gestoreDb.stampaClassifica(classifica));
                        break;
                    case "4":
                        exit = true;
                        break;
                    default:
                        break;
                }
            //aggiorno utente
            utente = gestoreDb.aggiornaUtente(utente);
        }
    }

    private void gestioneLogin() throws IOException{
        manda("Inserisci l'username:\nPASS");
        String username = ricevi();
        System.out.println(username);
        manda("Inserisci la tua password:\nPASS");
        String password = ricevi();
        System.out.println(password);
        this.utente = gestoreDb.login(username, password);
        if (this.utente != null) {
            MenuGioco();
        } else {
            manda("Username o password non validi.");
        }
    }

    private void gestioneRegister() throws IOException{
        manda("Inserisci l'username:\nPASS");
        String username = ricevi();
        System.out.println(username);
        manda("Inserisci una password\nPASS");
        String password = ricevi();
        System.out.println(password);

        int res = gestoreDb.registraUtente(username, password);
        if (res == 1){
            this.utente = gestoreDb.login(username, password);
            MenuGioco();
        } else if (res == 2) {
            manda("Siamo spiacenti, esiste già un utente con questo username!");
        } else {
            manda("Si è verificato un errore");
        }
    }

    private void stampaMenuLogin(){
        manda("Benvenuto a RPG a OGGETTI!\n" +
                "Cosa vuoi fare?\n" +
                "1. Accedi\n" +
                "2. Registrati\n" +
                "3. Esci\n"+
                "PASS");
    }

    private void stampaMenuGioco(){
        manda("Cosa vuoi fare?\n" +
                "1. Nuova partita\n" +
                "2. Continua\n" +
                "3. Classifica\n" +
                "4. Esci\n" +
                "PASS");
    }
    @Override
    public void manda(String string){
        output.println(string);
    }
    @Override
    public String ricevi()throws IOException{
        String clientInput = input.readLine();
        if (clientInput == null)
            throw new IOException("Client disconnesso");
        else return clientInput;
    }


}
