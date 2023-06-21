package Server;

import Locations.Accampamento;
import Locations.Bosco;
import Locations.Caverna;
import Locations.Lago;
import Oggetti.Cibo;
import Oggetti.Pozione;
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
        chiavePartita = gestoreDb.nuovaPartita(this);
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
            if(tipoIncontro <= 60) { //60%
                incontroCasuale();
            }
            else if (tipoIncontro <= 85) { //25%
                luogoCasuale();
            }else{
                Mercante mercante = new Mercante(this); //15%
                gestoreClient.manda("Vagando hai incontrato la tenda di " + mercante.getNome() + " il mercante!" );
                mercante.interagisci();
            }

            if(!giocatore.isVivo()) {
                gestoreDb.salvaPartita(this);
                break;
            }
            gestoreClient.manda("\nRound "+ roundCorrente +" terminato");
            String risposta;
            label:
            while(true){
                gestoreClient.manda("\nCosa vuoi fare?");
                gestoreClient.manda("1. Continua\n" +
                        "2. Apri inventario\n" +
                        "3. Esci\nPASS");
                risposta = gestoreClient.ricevi();
                switch (risposta) {
                    case "1":
                        break label;
                    case "2":
                        apriInventario(false);
                        break;
                    case "3":
                        esci = true;
                        break label;
                }
            }

            roundCorrente += 1;
            gestoreDb.salvaPartita(this);
            System.out.println("gioco salvato");
        }
    }


    /**
     * type == true se il metodo viene chiamato durante il combattimento, altrimenti type == false
     */
    private void apriInventario(boolean type)throws IOException {
        // Ottieni gli oggetti con quantità maggiore di 0
        String inventario;
        while (true) {
            Document documentInventario = gestoreDb.getInventario(utente, chiavePartita);
            inventario = gestoreDb.getInventarioStampabile(documentInventario);
            if (!inventario.isEmpty()) {
                gestoreClient.manda("Oggetti nell'inventario:");
                gestoreClient.manda(inventario);
                gestoreClient.manda("Cosa vuoi consumare? (Premi invio per tornare indietro)\nPASS");
                String risposta = gestoreClient.ricevi();
                System.out.println(risposta);
                if (risposta.isEmpty())
                    break;
                if(type && risposta.contains("pozione") && gestoreDb.decrementaQuantita(this, risposta.toLowerCase())){
                    Pozione pozione = gestoreDb.getPozione(documentInventario, risposta.toLowerCase());
                    giocatore.bevi(pozione);
                } else if (risposta.contains("pozione")) {
                    gestoreClient.manda("Puoi usare le pozioni solo durante il combattimento!");
                }else if (gestoreDb.decrementaQuantita(this, risposta.toLowerCase())) {
                    Cibo cibo = gestoreDb.getCibo(documentInventario, risposta.toLowerCase());
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
        int dropEsperienza;
        int puntiAttaccoOriginali = giocatore.getPuntiAttacco();
        int puntiDifesaOriginali = giocatore.getPuntiDifesa();
        gestoreClient.manda(nemico.getFrase());
        gestoreClient.manda("Inizia il combattimento con " + nemico.getNome() + "(" + nemico.getTipo() + ")");
        while (giocatore.isVivo() && nemico.isVivo()) {
            gestoreClient.manda("\nHP del nemico: "+ nemico.getPuntiVita());
            gestoreClient.manda("HP: "+ giocatore.getPuntiVita()+"/"+ giocatore.getMaxPuntiVita());
            //Chiedo all'utente cosa vuole fare
            clientMessage = inputCombattimento();
            boolean giocatorePrimo = giocatore.getPuntiAgilità() > nemico.getPuntiAgilità();
            switch (clientMessage) {
                case "1":
                    if (giocatorePrimo) {
                        //Attacca prima il giocatore
                        attacco(giocatore,nemico);
                        //Sleep di 1 secondo.
                        sleep();
                        if(!nemico.isVivo()) break;
                        //Adesso attacca il nemico
                        attacco(nemico,giocatore);
                    }else{
                        //Attacca prima il nemico
                        attacco(nemico,giocatore);
                        //Sleep di 1 secondo.
                        sleep();
                        if(!giocatore.isVivo()) break;
                        //Adesso attacca il giocatore
                        attacco(giocatore,nemico);
                    }
                    premiPerContinuare();
                    break;
                case "2":
                    //Il giocatore ha scelto di aumentare la propria difesa, raddoppiando i punti di difesa
                    //Aumento la difesa
                    giocatore.setPuntiDifesa((giocatore.getPuntiDifesa())*2);
                    gestoreClient.manda("Ti metti in posizione di difesa!.");
                    attacco(nemico,giocatore);
                    premiPerContinuare();
                    break;
                case "3":
                    //Il giocatore ha scelto di tentare di schivare l'attacco nemico
                    if(calcolaSchivata(giocatore.getPuntiAgilità())){
                        gestoreClient.manda("Complimenti! Sei riuscito a schivare l'attacco del nemico");
                    }else{
                        attacco(nemico,giocatore);
                    }
                    premiPerContinuare();
                    break;
                case "4":
                    //Apri inventario
                    apriInventario(true);
                    sleep();
                    //Il nemico attacca
                    attacco(nemico,giocatore);
                    premiPerContinuare();
                    break;
                case "5":
                    if(calcolaFuga(giocatore.getPuntiAgilità())){
                        gestoreClient.manda("Sei riuscito a fuggire dal combattimento.");
                        return; // Termina il metodo e il combattimento
                    }else{
                        attacco(nemico,giocatore);
                        premiPerContinuare();
                        break;
                    }
                }
        }
        if (giocatore.isVivo()) {
            giocatore.setStatus("Nessuno");
            giocatore.setPuntiAttacco(puntiAttaccoOriginali);
            giocatore.setPuntiDifesa(puntiDifesaOriginali);
            gestoreClient.manda("Complimenti! Hai vinto il combattimento!");
            //drop oggetti
            gestoreClient.manda("Il nemico possedeva "+ nemico.getQuantitaDrop() +" "+ StringUtils.capitalize(nemico.getDropOggetto()));
            gestoreDb.incrementaQuantita(this, nemico.getDropOggetto(), nemico.getQuantitaDrop());
            sleep();
            //drop soldi
            gestoreClient.manda("Il nemico possedeva " + nemico.getDropSoldi() + " monete");
            giocatore.aumentaSoldi(nemico.getDropSoldi());
            sleep();
            //drop esperienza
            dropEsperienza = nemico.getDropEsperienza();
            gestoreClient.manda("Hai ottenuto " + dropEsperienza + " punti esperienza!");
            sleep();
            giocatore.aumentaEsperienza(dropEsperienza);
        } else {
            gestoreClient.manda("Sei morto, GAME OVER!\n\n");
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

    private boolean calcolaFuga(int puntiAgilità) {
        Random random = new Random();
        double probabilitaSchivata = puntiAgilità * 0.025; // 2.5% di probabilità di schivata per ogni punto di agilità
        return random.nextDouble() < probabilitaSchivata;
    }

    private String inputCombattimento() throws IOException{
        int input = -1;
        while (input < 0 || input > 5) {
            gestoreClient.manda("\nCosa vuoi fare?\n" +
                    "1. Attacca\n" +
                    "2. Difenditi\n" +
                    "3. Schiva\n" +
                    "4. Inventario\n" +
                    "5. Scappa\n" +
                    "PASS");
            try {
                input = Integer.parseInt(gestoreClient.ricevi());
                if (input < 0 || input > 5) {
                    gestoreClient.manda("Scelta non valida.");
                }
            }catch (NumberFormatException e){
                gestoreClient.manda("Scelta non valida.");
            }
        }
        return Integer.toString(input);
    }

    private int calcolaDanni(PersonaggioCombattente attaccante, PersonaggioCombattente difensore) {
        int danni;
        if(attaccante instanceof Giocatore){
            //Danni se ad attaccare è il giocatore
            danni = giocatore.getArma().getDanniBase() - difensore.getPuntiDifesa();
            danni += random.nextInt(Math.max(attaccante.getPuntiAttacco(), 1));
            //Abilità speciali
            //Fuoco
            if((giocatore.getArma().getAbilita().equals("Fuoco") || giocatore.getStatus().equals("Infuocato")) && (random.nextInt(10) >= 6)){
                difensore.setStatus("Scottato");
            }
            //Ghiaccio
            if(giocatore.getArma().getAbilita().equals("Ghiaccio") && (random.nextInt(10) >= 6)){
                difensore.setStatus("Congelato");
                System.out.println("Congelato:" + difensore.getStatus());
            }
        }else {
            // Calcola i danni considerando i punti di attacco e difesa
            danni = attaccante.getPuntiAttacco() - difensore.getPuntiDifesa();
            danni += random.nextInt(Math.max(attaccante.getPuntiAttacco()/2, 1));
        }
        if (danni < 0) {
            danni = 1; // I danni non possono essere negativi o nulli
        }

        return danni;
    }

    private void attacco(PersonaggioCombattente attaccante, PersonaggioCombattente difensore){
        if (attaccante instanceof Giocatore){
            int danniGiocatore = calcolaDanni(attaccante, difensore);
            difensore.subisciDanni(danniGiocatore);
            gestoreClient.manda("Hai attaccato il nemico e gli hai inflitto " + danniGiocatore + " danni!");
            if(difensore.isVivo() && difensore.getStatus().equals("Scottato")){
                int dannoScottatura = random.nextInt(4)+1;
                gestoreClient.manda("Il nemico subisce un danno di " + dannoScottatura + " HP per scottatura!");
                difensore.subisciDanni(dannoScottatura);
            }
        }else{
            if(attaccante.getStatus().equals("Congelato") && random.nextInt(10)>=6){
                gestoreClient.manda("Il nemico è congelato e non può attaccarti");
            }else {
                int danniNemico = calcolaDanni(attaccante, difensore);
                difensore.subisciDanni(danniNemico);
                gestoreClient.manda("Il nemico ti ha attaccato ed inflitto " + danniNemico + " danni!");
            }
        }
    }

    private void incontroCasuale()throws IOException{
        int nemico = random.nextInt(100)+1;
        if(nemico <= 20){
            Orco orco = new Orco(giocatore.getLivello()); //20%
            orco.stampaStats();
            Combattimento(orco);
        }else if(nemico <= 60) {
            Goblin goblin = new Goblin(giocatore.getLivello()); //40%
            goblin.stampaStats();
            Combattimento(goblin);
        }else if (nemico <= 75 ){
            Strega strega = new Strega(giocatore.getLivello()); //15%
            strega.stampaStats();
            Combattimento(strega);
        }else if (nemico <= 80){
            Drago drago = new Drago(giocatore.getLivello()); //5%
            drago.stampaStats();
            Combattimento(drago);
        }else if(nemico <= 90) {
            Cavaliere cavaliere = new Cavaliere(gestoreClient, giocatore.getLivello()); //10%
            cavaliere.interagisci(this);
        } else {
            Brigante brigante = new Brigante(gestoreClient, giocatore.getLivello()); //10%
            brigante.interagisci(this);
        }

    }

    private void luogoCasuale()throws IOException{
        int luogo = random.nextInt(100)+1;
        if (luogo <= 25) {
            Bosco bosco = new Bosco(gestoreClient);
            bosco.esplora(this);
            premiPerContinuare();
        }else if(luogo <= 50){
            Lago lago = new Lago(gestoreClient);
            lago.esplora(this);
            premiPerContinuare();
        }else if(luogo <= 75){
            Accampamento accampamento = new Accampamento(gestoreClient);
            accampamento.esplora(this);
            premiPerContinuare();
        } else{
            Caverna caverna = new Caverna(gestoreClient);
            caverna.esplora(this);
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
