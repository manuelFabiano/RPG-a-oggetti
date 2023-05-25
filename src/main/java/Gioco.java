import Personaggi.Giocatore;
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

    //inizia un loop per gestire ogni round del gioco
    public void loop(){
        while (roundCorrente < rounds){
            //Se è il primo round stampo la storia iniziale
            if (roundCorrente == 1){
                stampaStoriaIniziale();
                premiPerContinuare();
            }
            //logica di gioco:

        }
    }

    private void premiPerContinuare(){
        output.println("Premi invio per coninuare\nPASS");
        try {
            input.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stampaStoriaIniziale(){
        output.println("Nel cuore di un bosco avvolto dall'ombra, ti risvegli senza alcuna memoria.\n" +
                "La luce danza attraverso gli alberi, mentre il vento sussurra segreti antichi.\n" +
                "Sola e sperduta, la tua anima si nutre di un'insaziabile sete di verità\n" +
                "Ogni passo rivela una nuova sfida e ogni incontro svela un pezzo del puzzle perduto...\n" +
                "PASS");
    }
}
