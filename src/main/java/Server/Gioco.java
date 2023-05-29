package Server;

import Locations.*;
import Personaggi.*;
import org.bson.Document;
import java.io.IOException;
import java.util.Random;

public class Gioco {
    private final InterfacciaGestoreClient gestoreClient;
    private final GestoreDb gestoreDb;
    private static final int ROUNDS = 50;
    private int roundCorrente;
    private Giocatore giocatore;
    private Random random;
    private final String chiavePartita;
    Document utente;

    //costruttore usato se si sta avviando una nuova partita
    public Gioco(InterfacciaGestoreClient gestoreClient, GestoreDb gestoreDb, Document utente) throws IOException {
        this.gestoreClient = gestoreClient;
        this.gestoreDb = gestoreDb;
        this.roundCorrente = 1;
        random = new Random();
        this.giocatore = new Giocatore(gestoreClient);
        this.utente = utente;
        chiavePartita = gestoreDb.nuovaPartita(utente, giocatore);
    }

    //Continua la partita
    public Gioco(InterfacciaGestoreClient gestoreClient, GestoreDb gestoreDb, Document utente, Document partita, String chiavePartita) throws IOException {
        this.gestoreClient = gestoreClient;
        this.gestoreDb = gestoreDb;
        this.roundCorrente = partita.getInteger("roundCorrente");
        random = new Random();
        this.giocatore = new Giocatore(gestoreClient, partita);
        this.utente = utente;
        this.chiavePartita = chiavePartita;
    }

    //inizia un loop per gestire ogni round del gioco
    public void loop()throws IOException{
        int tipoIncontro;
        boolean gameOver = false;
        Random random = new Random();
        while (roundCorrente < ROUNDS && !gameOver){
            //Se è il primo round stampo la storia iniziale
            if (roundCorrente == 1){
                stampaStoriaIniziale();
                premiPerContinuare();
            }
            //logica di gioco:
            tipoIncontro = random.nextInt(2);
            //INCONTRO CON UN NEMICO
            if(tipoIncontro == 0) {
                nemicoCasuale();
            }
            else if (tipoIncontro == 1) {
                luogoCasuale();
            }
            if(!giocatore.isVivo())
                gameOver = true;
        }
    }

    //Metodo che gestisce i combattimenti
    private void Combattimento(Nemico nemico) throws IOException{
        String clientMessage;
        int danniGiocatore;
        int danniNemico;
        int dropEsperienza;
        gestoreClient.manda("Inizia il combattimento con " + nemico.getNome() + "(" + nemico.getTipo() + ")");
        while (giocatore.isVivo() && nemico.isVivo()) {
            gestoreClient.manda("HP del nemico: "+ nemico.getPuntiVita());
            gestoreClient.manda("I tuoi HP: "+ giocatore.getPuntiVita());
            //Chiedo all'utente cosa vuole fare
            if(((clientMessage = inputCombattimento()) != null)){
                System.out.println("Messaggio dal client: " + clientMessage);
                boolean giocatorePrimo = giocatore.getPuntiAgilità() > nemico.getPuntiAgilità();
                switch (clientMessage) {
                    case "1":
                        if (giocatorePrimo) {
                            //Attacca prima il giocatore
                            danniGiocatore = calcolaDanni(giocatore.getPuntiAttacco(), nemico.getPuntiDifesa());
                            nemico.subisciDanni(danniGiocatore);
                            gestoreClient.manda("Hai attaccato il nemico e gli hai inflitto " + danniGiocatore + " danni!");
                            //Sleep di 1 secondo.
                            sleep();
                            if(!nemico.isVivo()) break;
                            //Adesso attacca il nemico
                            danniNemico = calcolaDanni(nemico.getPuntiAttacco(), giocatore.getPuntiDifesa());
                            giocatore.subisciDanni(danniNemico);
                            gestoreClient.manda("Il nemico ti ha attaccato ed inflitto " + danniNemico + " danni!");
                            premiPerContinuare();
                        }else{
                            //Attacca prima il nemico
                            danniNemico = calcolaDanni(nemico.getPuntiAttacco(), giocatore.getPuntiDifesa());
                            giocatore.subisciDanni(danniNemico);
                            gestoreClient.manda("Il nemico ti ha attaccato ed inflitto " + danniNemico + " danni!");
                            //Sleep di 1 secondo.
                            sleep();
                            if(!giocatore.isVivo()) break;
                            danniGiocatore = calcolaDanni(giocatore.getPuntiAttacco(), nemico.getPuntiDifesa());
                            nemico.subisciDanni(danniGiocatore);
                            gestoreClient.manda("Hai attaccato il nemico e gli hai inflitto " + danniGiocatore + " danni!");
                            premiPerContinuare();
                        }
                        break;
                    case "2":
                        //Il giocatore ha scelto di aumentare la propria difesa, raddoppiando i punti di difesa
                        gestoreClient.manda("Ti metti in posizione di difesa!.");
                        danniNemico = calcolaDanni(nemico.getPuntiAttacco(), giocatore.getPuntiDifesa() * 2);
                        giocatore.subisciDanni(danniNemico);
                        gestoreClient.manda("Il nemico ti ha attaccato ed inflitto " + danniNemico + " danni!");
                        premiPerContinuare();
                        break;
                    case "3":
                        //Il giocatore ha scelto di tentare di schivare l'attacco nemico
                        if(calcolaSchivata(giocatore.getPuntiAgilità())){
                            gestoreClient.manda("Complimenti! Sei riuscito a schivare l'attacco del nemico");
                            premiPerContinuare();
                        }else{
                            danniNemico = calcolaDanni(nemico.getPuntiAttacco(), giocatore.getPuntiDifesa());
                            giocatore.subisciDanni(danniNemico);
                            gestoreClient.manda("Il nemico ti ha attaccato ed inflitto " + danniNemico + " danni!");
                            premiPerContinuare();
                        }
                        break;
                    case "4":
                        gestoreClient.manda("Il giocatore fugge dal combattimento.");
                        return; // Termina il metodo e il combattimento
                }

            }
        }
        if (giocatore.isVivo()) {
            gestoreClient.manda("Complimenti! Hai vinto il combattimento!");
            roundCorrente += 1;
            dropEsperienza = nemico.getDropEsperienza();
            gestoreClient.manda("Hai ottenuto " + dropEsperienza + " punti esperienza!");
            sleep();
            giocatore.aumentaEsperienza(dropEsperienza);
            gestoreDb.salvaPartita(utente, giocatore, this);
        } else {
            gestoreClient.manda("Sei morto, GAME OVER!");
        }
    }


    private void sleep(){
        try {
            Thread.sleep(1000); // Ritardo di 1 secondo
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private boolean calcolaSchivata(int puntiAgilità) {
        Random random = new Random();
        double probabilitaSchivata = puntiAgilità * 0.1; // 10% di probabilità di schivata per ogni punto di agilità
        return random.nextDouble() < probabilitaSchivata;
    }
    private String inputCombattimento() throws IOException{
        gestoreClient.manda("Cosa vuoi fare?\n" +
                "Scegli un'opzione:\n" +
                "1. Attacca\n" +
                "2. Difenditi\n" +
                "3. Schiva\n" +
                "4. Scappa\n" +
                "PASS");
        int input = -1;
        while (input < 0 || input > 4) {
            input = Integer.parseInt(gestoreClient.ricevi());
            if (input < 0 || input > 4) {
                gestoreClient.manda("Scelta non valida.");
            }
        }
        return Integer.toString(input);
    }

    private int calcolaDanni(int puntiAttacco, int puntiDifesa) {
        // Calcola i danni considerando i punti di attacco e difesa
        int danni = puntiAttacco - puntiDifesa;
        if (danni <= 0) {
            danni = 1; // I danni non possono essere negativi
        }
        danni += random.nextInt(puntiAttacco);
        return danni;
    }

    private void nemicoCasuale()throws IOException{
        int nemico = random.nextInt(11);
        if(nemico > 7){
            Orco orco = new Orco(giocatore);
            Combattimento(orco);
        }else{
            Goblin goblin = new Goblin(giocatore);
            Combattimento(goblin);
        }

    }

    private void luogoCasuale()throws IOException{
        int luogo = random.nextInt(2);
        if (luogo == 0) {
            Bosco bosco = new Bosco(gestoreClient);
            bosco.esplora(this);
            premiPerContinuare();
        }else{
            Lago lago = new Lago(gestoreClient);
            lago.esplora(this);
            premiPerContinuare();
        }


    }


    private void premiPerContinuare(){
        gestoreClient.manda("Premi invio per coninuare\nPASS");
        try {
            gestoreClient.ricevi();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void stampaStoriaIniziale(){
        gestoreClient.manda("Nel cuore di un bosco avvolto dall'ombra, ti risvegli senza alcuna memoria.\n" +
                "La luce danza attraverso gli alberi, mentre il vento sussurra segreti antichi.\n" +
                "Sola e sperduta, la tua anima si nutre di un'insaziabile sete di verità\n" +
                "Ogni passo rivela una nuova sfida e ogni incontro svela un pezzo del puzzle perduto...\n");
    }

    public int getRoundCorrente() {
        return roundCorrente;
    }

    public String getChiavePartita() {
        return chiavePartita;
    }

    public Giocatore getGiocatore() {
        return giocatore;
    }

    public void setGiocatore(Giocatore giocatore) {
        this.giocatore = giocatore;
    }
}
