package Personaggi;


import Oggetti.Arma;
import Oggetti.Cibo;
import Oggetti.Pozione;
import Server.InterfacciaGestoreClient;
import org.bson.Document;

import java.io.IOException;

public class Giocatore extends PersonaggioCombattente {
    private final InterfacciaGestoreClient gestoreClient;
    private int livello;
    private int esperienza;
    private int soldi;
    private Arma arma;

    /**
     * Costruttore personaggio del giocatore quando si inizia una nuova partita
     */
    public Giocatore(InterfacciaGestoreClient gestoreClient) throws IOException {
        this.gestoreClient = gestoreClient;
        setMaxPuntiVita(20);
        setPuntiVita(20);
        livello = 1;
        esperienza = 0;
        soldi = 0;
        arma = new Arma("Spada di Legno","Una normale spada di legno", 5,"Nessuna");
        assegnaPunti(15);
    }

    /**
     * Costruttore personaggio del giocatore quando si continua una vecchia partita
     */
    public Giocatore(InterfacciaGestoreClient gestoreClient, Document partita){
        this.gestoreClient = gestoreClient;
        setPuntiVita(partita.getInteger("puntiVita"));
        setMaxPuntiVita(partita.getInteger("maxPuntiVita"));
        setPuntiAttacco(partita.getInteger("puntiAttacco"));
        setPuntiDifesa(partita.getInteger("puntiDifesa"));
        setPuntiAgilità(partita.getInteger("puntiAgilità"));
        livello = partita.getInteger("livello");
        esperienza = partita.getInteger("esperienza");
        soldi = partita.getInteger("soldi");
        Document documentArma = (Document) partita.get("arma");
        arma = new Arma(documentArma.getString("nome"),documentArma.getString("descrizione"),documentArma.getInteger("danniBase"),documentArma.getString("abilita"));
    }

    public void aumentaEsperienza(int esperienza)throws IOException{
        this.esperienza += esperienza;
        while(this.esperienza >= 1000){
            setMaxPuntiVita(getMaxPuntiVita()+2);
            setPuntiVita(getPuntiVita()+2);
            livello += 1;
            gestoreClient.manda("Sei aumentato di livello!\n");
            assegnaPunti(3);
            //resetto l'exp
            this.esperienza -= 1000;
        }
    }

    public int getLivello() {
        return livello;
    }

    public int getEsperienza() {
        return esperienza;
    }

    private void assegnaPunti(int puntiRimanenti) throws IOException {
        while (puntiRimanenti > 0) {
            gestoreClient.manda("Hai " + puntiRimanenti + " punti a disposizione da assegnare alle tue abilità!\n"
                    + "- Attacco\n- Difesa\n- Agilità\n"
                    + "In quale abilità vuoi assegnare i punti? (Attacco, Difesa, Agilità)\nPASS");

            String scelta = gestoreClient.ricevi().toLowerCase();

            switch (scelta) {
                case "attacco":
                    gestoreClient.manda("Quanti punti di Attacco vuoi assegnare?\nPASS");

                    int puntiAttacco = Integer.parseInt(gestoreClient.ricevi());

                    if (puntiAttacco < 1 || puntiAttacco > puntiRimanenti) {
                        gestoreClient.manda("Puoi assegnare da 1 a " + puntiRimanenti + " punti ad Attacco.");
                        continue;
                    }

                    setPuntiAttacco(getPuntiAttacco() + puntiAttacco);
                    puntiRimanenti -= puntiAttacco;
                    break;

                case "difesa":
                    gestoreClient.manda("Quanti punti di Difesa vuoi assegnare?\nPASS");

                    int puntiDifesa = Integer.parseInt(gestoreClient.ricevi());

                    if (puntiDifesa < 1 || puntiDifesa > puntiRimanenti) {
                        gestoreClient.manda("Puoi assegnare da 1 a " + puntiRimanenti + " punti alla Difesa.");
                        continue;
                    }

                    setPuntiDifesa(getPuntiDifesa() + puntiDifesa);
                    puntiRimanenti -= puntiDifesa;
                    break;

                case "agilità":
                    gestoreClient.manda("Quanti punti di Agilità vuoi assegnare?\nPASS");

                    int puntiAgilita = Integer.parseInt(gestoreClient.ricevi());

                    if (puntiAgilita < 1 || puntiAgilita > puntiRimanenti) {
                        gestoreClient.manda("Puoi assegnare da 1 a " + puntiRimanenti + " punti all'Agilità.");
                        continue;
                    }

                    setPuntiAgilità(getPuntiAgilità() + puntiAgilita);
                    puntiRimanenti -= puntiAgilita;
                    break;

                default:
                    gestoreClient.manda("Abilità non valida. Riprova.");
            }
        }

        gestoreClient.manda("Hai assegnato i punti correttamente:\n"
                + "Attacco: " + getPuntiAttacco() + "\n"
                + "Difesa: " + getPuntiDifesa() + "\n"
                + "Agilità: " + getPuntiAgilità());
    }

    public void nuovaArma(Arma arma) throws IOException{
        while (true){
            gestoreClient.manda("Hai trovato una nuova arma:");
            gestoreClient.manda(arma.getArma());
            gestoreClient.manda("\nQuesta è la tua attuale arma:");
            gestoreClient.manda(this.arma.getArma());
            gestoreClient.manda("\nVuoi equipaggiare l'arma che hai appena trovato?\n" +
                    "1. Si\n" +
                    "2. No\nPASS");
            String risposta = gestoreClient.ricevi();
            if(risposta.equals("1")){
                setArma(arma);
                gestoreClient.manda("Hai equipaggiato la nuova arma!");
                break;
            }else if(risposta.equals("2")){
                gestoreClient.manda("Hai lasciato a terra la nuova arma...");
                break;
            }else{
                gestoreClient.manda("Opzione non valida!");
            }
        }
    }

    public void mangia(Cibo cibo){
        int nuovaVita = getPuntiVita() + cibo.getPuntiVita();
        setPuntiVita(Math.min(nuovaVita, getMaxPuntiVita()));
        gestoreClient.manda("Hai mangiato 1 " + cibo.getNome() + " e hai recuperato "+ cibo.getPuntiVita() +" HP!");
        gestoreClient.manda("HP:"+getPuntiVita()+"/"+getMaxPuntiVita());
    }

    public void bevi(Pozione pozione){
        String tipo = pozione.getTipo();
        switch (tipo) {
            case "Cura":
                int nuovaVita = getPuntiVita() + pozione.getPunti();
                setPuntiVita(Math.min(nuovaVita, getMaxPuntiVita()));
                gestoreClient.manda("Hai bevuto 1 " + pozione.getNome() + " e hai recuperato " + pozione.getPunti() + " HP!");
                gestoreClient.manda("HP:" + getPuntiVita() + "/" + getMaxPuntiVita());
                break;
            case "Attacco":
                int nuovoAttacco = getPuntiAttacco() + pozione.getPunti();
                setPuntiAttacco(nuovoAttacco);
                gestoreClient.manda("Hai bevuto 1 " + pozione.getNome() + " e adesso hai " + nuovoAttacco + " punti attacco!");
                break;
            case "Difesa":
                int nuovaDifesa = getPuntiDifesa() + pozione.getPunti();
                setPuntiDifesa(nuovaDifesa);
                gestoreClient.manda("Hai bevuto 1 " + pozione.getNome() + " e adesso hai " + nuovaDifesa + " punti difesa!");
                break;
            case "Fuoco":
                setStatus("Infuocato");
                gestoreClient.manda("Hai bevuto 1 " + pozione.getNome() + " e adesso il fuoco ti ricopre tutto il corpo!");
                break;
        }
    }

    public Arma getArma() {
        return arma;
    }

    public void setArma(Arma arma) {
        this.arma = arma;
    }

    public int getSoldi() {
        return soldi;
    }

    public void decrementaSoldi(int quantita) {
        soldi -= quantita;
    }
    public void aumentaSoldi(int quantita) { soldi += quantita; }

    public InterfacciaGestoreClient getGestoreClient() {
        return gestoreClient;
    }
}
