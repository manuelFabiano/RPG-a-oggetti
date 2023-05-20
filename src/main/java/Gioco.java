import org.bson.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Gioco {
    private BufferedReader input;
    private PrintWriter output;
    private GestoreDb gestoreDb;
    private static final int rounds = 50;
    private int roundCorrente;
    private Giocatore giocatore;

    //costruttore usato se si sta avviando una nuova partita
    public Gioco(BufferedReader input, PrintWriter output, GestoreDb gestoreDb, Document utente) throws IOException {
        this.input = input;
        this.output = output;
        this.gestoreDb = gestoreDb;
        this.roundCorrente = 1;
        this.giocatore = new Giocatore(input, output);
        gestoreDb.nuovaPartita(utente, giocatore);


    }
}
