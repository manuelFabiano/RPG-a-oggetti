package Server;

import Locations.Accampamento;
import Locations.Bosco;
import Locations.Lago;
import Oggetti.Cibo;
import Personaggi.*;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import java.io.IOException;
import java.util.Random;

public class Gioco {
    private final InterfacciaGestoreClient gestoreClient;
    private final GestoreDb gestoreDb;
    private static final int ROUNDS = 50;
    private int roundCorrente;
    private final Giocatore giocatore;
    private final Random random;
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
        chiavePartita = gestoreDb.nuovaPartita(utente, giocatore, this);
    }

    //Continua la partita
    public Gioco(InterfacciaGestoreClient gestoreClient, GestoreDb gestoreDb, Document utente, Document partita, String chiavePartita){
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
        boolean esci = false;
        Random random = new Random();
        while (roundCorrente < ROUNDS && !esci){
            gestoreClient.manda("Round: "+ roundCorrente);
            gestoreClient.manda("Livello: "+ giocatore.getLivello());
            gestoreClient.manda("HP: "+ giocatore.getPuntiVita()+"/"+ giocatore.getMaxPuntiVita());
            //Se è il primo round stampo la storia iniziale
            if (roundCorrente == 1){
                stampaStoriaIniziale();
                premiPerContinuare();
            }
            //logica di gioco:
            tipoIncontro = random.nextInt(100)+1;
            //INCONTRO CASUALE
            if(tipoIncontro <= 60) {
                incontroCasuale();
            }
            else if (tipoIncontro <= 90) {
                luogoCasuale();
            }else{
                Mercante mercante = new Mercante(this);
                gestoreClient.manda("Vagando hai incontrato la tenda di " + mercante.getNome() + " il mercante!" );
                mercante.interagisci();
            }

            if(!giocatore.isVivo()) {
                break;
            }
            gestoreClient.manda("Round "+ roundCorrente +" terminato");
            String risposta;
            while(true){
                gestoreClient.manda("Cosa vuoi fare?");
                gestoreClient.manda("1. Continua\n" +
                        "2. Apri inventario\n" +
                        "3. Esci\nPASS");
                risposta = gestoreClient.ricevi();
                if(risposta.equals("1")) {
                    break;
                }
                else if (risposta.equals("2")) {
                    apriInventario();
                }else if (risposta.equals("3")){
                    esci = true;
                    break;
                }
            }

            roundCorrente += 1;
            gestoreDb.salvaPartita(utente, giocatore, this);
            System.out.println("gioco salvato");
        }
    }

    private void apriInventario()throws IOException {
        // Ottieni gli oggetti con quantità maggiore di 0
        String inventario;
        while (true) {
            inventario = gestoreDb.getInventarioStampabile(gestoreDb.getInventario(utente, chiavePartita));
            if (!inventario.isEmpty()) {
                gestoreClient.manda("Oggetti nell'inventario:");
                gestoreClient.manda(inventario);
                gestoreClient.manda("Cosa vuoi consumare? (Premi invio per tornare indietro)\nPASS");
                String risposta = gestoreClient.ricevi();
                System.out.println(risposta);
                if (risposta.isEmpty())
                    break;
                if (gestoreDb.decrementaQuantita(this, risposta.toLowerCase())) {
                    Cibo cibo = gestoreDb.getCibo(gestoreDb.getInventario(utente, chiavePartita), risposta.toLowerCase());
                    giocatore.mangia(cibo);
                }
            } else {
                gestoreClient.manda("Il tuo inventario è vuoto!");
                premiPerContinuare();
                break;
            }

        }
    }

    //Metodo che gestisce i combattimenti
    public void Combattimento(Nemico nemico) throws IOException{
        String clientMessage;
        int danniGiocatore;
        int danniNemico;
        int dropEsperienza;
        int puntiDifesaOriginali = giocatore.getPuntiDifesa();
        gestoreClient.manda("Inizia il combattimento con " + nemico.getNome() + "(" + nemico.getTipo() + ")");
        while (giocatore.isVivo() && nemico.isVivo()) {
            gestoreClient.manda("HP del nemico: "+ nemico.getPuntiVita());
            gestoreClient.manda("HP: "+ giocatore.getPuntiVita()+"/"+ giocatore.getMaxPuntiVita());
            //Chiedo all'utente cosa vuole fare
            if(((clientMessage = inputCombattimento()) != null)){
                System.out.println("Messaggio dal client: " + clientMessage);
                boolean giocatorePrimo = giocatore.getPuntiAgilità() > nemico.getPuntiAgilità();
                switch (clientMessage) {
                    case "1":
                        if (giocatorePrimo) {
                            //Attacca prima il giocatore
                            danniGiocatore = calcolaDanni(giocatore, nemico);
                            nemico.subisciDanni(danniGiocatore);
                            gestoreClient.manda("Hai attaccato il nemico e gli hai inflitto " + danniGiocatore + " danni!");
                            //Sleep di 1 secondo.
                            sleep();
                            if(!nemico.isVivo()) break;
                            //Adesso attacca il nemico
                            danniNemico = calcolaDanni(nemico, giocatore);
                            giocatore.subisciDanni(danniNemico);
                            gestoreClient.manda("Il nemico ti ha attaccato ed inflitto " + danniNemico + " danni!");
                            premiPerContinuare();
                        }else{
                            //Attacca prima il nemico
                            danniNemico = calcolaDanni(nemico, giocatore);
                            giocatore.subisciDanni(danniNemico);
                            gestoreClient.manda("Il nemico ti ha attaccato ed inflitto " + danniNemico + " danni!");
                            //Sleep di 1 secondo.
                            sleep();
                            if(!giocatore.isVivo()) break;
                            danniGiocatore = calcolaDanni(giocatore, nemico);
                            nemico.subisciDanni(danniGiocatore);
                            gestoreClient.manda("Hai attaccato il nemico e gli hai inflitto " + danniGiocatore + " danni!");
                            premiPerContinuare();
                        }
                        break;
                    case "2":
                        //Il giocatore ha scelto di aumentare la propria difesa, raddoppiando i punti di difesa
                        //Aumento la difesa
                        giocatore.setPuntiDifesa((giocatore.getPuntiDifesa())*2);
                        gestoreClient.manda("Ti metti in posizione di difesa!.");
                        danniNemico = calcolaDanni(nemico, giocatore);
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
                            danniNemico = calcolaDanni(nemico, giocatore);
                            giocatore.subisciDanni(danniNemico);
                            gestoreClient.manda("Il nemico ti ha attaccato ed inflitto " + danniNemico + " danni!");
                            premiPerContinuare();
                        }
                        break;
                    case "4":
                        //Apri inventario
                        apriInventario();
                        sleep();
                        //Il nemico attacca
                        danniNemico = calcolaDanni(nemico, giocatore);
                        giocatore.subisciDanni(danniNemico);
                        gestoreClient.manda("Il nemico ti ha attaccato ed inflitto " + danniNemico + " danni!");
                        premiPerContinuare();
                        break;
                    case "5":
                        gestoreClient.manda("Il giocatore fugge dal combattimento.");
                        return; // Termina il metodo e il combattimento

                }

            }
        }
        if (giocatore.isVivo()) {
            giocatore.setPuntiDifesa(puntiDifesaOriginali);
            gestoreClient.manda("Complimenti! Hai vinto il combattimento!");
            //drop oggetti
            gestoreClient.manda("Il nemico possedeva "+ nemico.getQuantitaDrop() +" "+ StringUtils.capitalize(nemico.getDropOggetto()));
            gestoreDb.incrementaQuantita(this, nemico.getDropOggetto(), nemico.getQuantitaDrop());
            sleep();
            //drop esperienza
            dropEsperienza = nemico.getDropEsperienza();
            gestoreClient.manda("Hai ottenuto " + dropEsperienza + " punti esperienza!");
            sleep();
            giocatore.aumentaEsperienza(dropEsperienza);
        } else {
            gestoreClient.manda("Sei morto, GAME OVER!");
        }
    }


    public void sleep(){
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
                "4. Inventario\n" +
                "5. Scappa\n" +
                "PASS");
        int input = -1;
        while (input < 0 || input > 5) {
            input = Integer.parseInt(gestoreClient.ricevi());
            if (input < 0 || input > 5) {
                gestoreClient.manda("Scelta non valida.");
            }
        }
        return Integer.toString(input);
    }

    private int calcolaDanni(PersonaggioCombattente attaccante, PersonaggioCombattente difensore) {
        int danni;
        if(attaccante instanceof Giocatore){
            danni = ((Giocatore) attaccante).getArma().getDanniBase() - difensore.getPuntiDifesa();
        }else {
            // Calcola i danni considerando i punti di attacco e difesa
            danni = attaccante.getPuntiAttacco() - difensore.getPuntiDifesa();
        }
        if (danni <= 0) {
            danni = 1; // I danni non possono essere negativi o nulli
        }
        danni += random.nextInt(Math.max(attaccante.getPuntiAttacco(), 1));

        return danni;
    }

    private void incontroCasuale()throws IOException{
        int nemico = random.nextInt(100)+1;
        if(nemico <= 25){
            Orco orco = new Orco(giocatore);
            Combattimento(orco);
        }else if(nemico <= 50){
            Goblin goblin = new Goblin(giocatore);
            Combattimento(goblin);
        }else if(nemico <= 75) {
            Cavaliere cavaliere = new Cavaliere(giocatore);
            cavaliere.interagisci(this);
        } else {
            Brigante brigante = new Brigante(giocatore);
            brigante.interagisci(this, this.getGiocatore());
        }

    }

    private void luogoCasuale()throws IOException{
        int luogo = random.nextInt(11);
        if (luogo > 6) {
            Bosco bosco = new Bosco(gestoreClient);
            bosco.esplora(this);
            premiPerContinuare();
        }else if(luogo > 2){
            Lago lago = new Lago(gestoreClient);
            lago.esplora(this);
            premiPerContinuare();
        }else{
            Accampamento accampamento = new Accampamento(gestoreClient);
            accampamento.esplora(this);
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

    public GestoreDb getGestoreDb() {
        return gestoreDb;
    }

    public Document getUtente() {
        return utente;
    }

    public void setUtente(Document utente) {
        this.utente = utente;
    }

    public InterfacciaGestoreClient getGestoreClient() {
        return gestoreClient;
    }
}
