package Personaggi;


import Oggetti.Arma;
import Oggetti.Cibo;
import Server.InterfacciaGestoreClient;
import org.bson.Document;

import java.io.IOException;

public class Giocatore extends Personaggio {
    private int livello;
    private int esperienza;
    private Arma arma;

    //Costruttore personaggio del giocatore quando si inizia una nuova partita
    public Giocatore(InterfacciaGestoreClient gestoreClient) throws IOException {
        setGestoreClient(gestoreClient);
        setMaxPuntiVita(20);
        setPuntiVita(20);
        livello = 1;
        esperienza = 0;
        arma = new Arma("Spada di Legno","Una normale spada di legno", 2,"Nessuna");
        assegnaPunti(15);
    }

    //Continua partita
    public Giocatore(InterfacciaGestoreClient gestoreClient, Document partita){
        setGestoreClient(gestoreClient);
        setPuntiVita(partita.getInteger("puntiVita"));
        setMaxPuntiVita(partita.getInteger("maxPuntiVita"));
        setPuntiAttacco(partita.getInteger("puntiAttacco"));
        setPuntiDifesa(partita.getInteger("puntiDifesa"));
        setPuntiAgilità(partita.getInteger("puntiAgilità"));
        livello = partita.getInteger("livello");
        esperienza = partita.getInteger("esperienza");
        Document documentArma = (Document) partita.get("arma");
        arma = new Arma(documentArma.getString("nome"),documentArma.getString("descrizione"),documentArma.getInteger("danniBase"),documentArma.getString("abilita"));
    }

    public void aumentaEsperienza(int esperienza)throws IOException{
        this.esperienza += esperienza;
        while(this.esperienza >= 1000){
            setMaxPuntiVita(getMaxPuntiVita()+2);
            setPuntiVita(getPuntiVita()+2);
            livello += 1;
            getGestoreClient().manda("Sei aumentato di livello!\n");
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
            getGestoreClient().manda("Hai " + puntiRimanenti + " punti a disposizione da assegnare alle tue abilità!\n"
                    + "- Attacco\n- Difesa\n- Agilità\n"
                    + "In quale abilità vuoi assegnare i punti? (Attacco, Difesa, Agilità)\nPASS");

            String scelta = getGestoreClient().ricevi().toLowerCase();

            switch (scelta) {
                case "attacco":
                    getGestoreClient().manda("Quanti punti di Attacco vuoi assegnare?\nPASS");

                    int puntiAttacco = Integer.parseInt(getGestoreClient().ricevi());

                    if (puntiAttacco < 1 || puntiAttacco > puntiRimanenti) {
                        getGestoreClient().manda("Puoi assegnare da 1 a " + puntiRimanenti + " punti ad Attacco.");
                        continue;
                    }

                    setPuntiAttacco(getPuntiAttacco() + puntiAttacco);
                    puntiRimanenti -= puntiAttacco;
                    break;

                case "difesa":
                    getGestoreClient().manda("Quanti punti di Difesa vuoi assegnare?\nPASS");

                    int puntiDifesa = Integer.parseInt(getGestoreClient().ricevi());

                    if (puntiDifesa < 1 || puntiDifesa > puntiRimanenti) {
                        getGestoreClient().manda("Puoi assegnare da 1 a " + puntiRimanenti + " punti alla Difesa.");
                        continue;
                    }

                    setPuntiDifesa(getPuntiDifesa() + puntiDifesa);
                    puntiRimanenti -= puntiDifesa;
                    break;

                case "agilità":
                    getGestoreClient().manda("Quanti punti di Agilità vuoi assegnare?\nPASS");

                    int puntiAgilita = Integer.parseInt(getGestoreClient().ricevi());

                    if (puntiAgilita < 1 || puntiAgilita > puntiRimanenti) {
                        getGestoreClient().manda("Puoi assegnare da 1 a " + puntiRimanenti + " punti all'Agilità.");
                        continue;
                    }

                    setPuntiAgilità(getPuntiAgilità() + puntiAgilita);
                    puntiRimanenti -= puntiAgilita;
                    break;

                default:
                    getGestoreClient().manda("Abilità non valida. Riprova.");
            }
        }

        getGestoreClient().manda("Hai assegnato i punti correttamente:\n"
                + "Attacco: " + getPuntiAttacco() + "\n"
                + "Difesa: " + getPuntiDifesa() + "\n"
                + "Agilità: " + getPuntiAgilità());
    }

    public void nuovaArma(Arma arma) throws IOException{
        while (true){
            getGestoreClient().manda("Hai trovato una nuova arma:");
            getGestoreClient().manda(arma.getArma());
            getGestoreClient().manda("\nQuesta è la tua attuale arma:");
            getGestoreClient().manda(this.arma.getArma());
            getGestoreClient().manda("\nVuoi equipaggiare l'arma che hai appena trovato?\n" +
                    "1. Si\n" +
                    "2. No\nPASS");
            String risposta = getGestoreClient().ricevi();
            if(risposta.equals("1")){
                setArma(arma);
                getGestoreClient().manda("Hai equipaggiato la nuova arma!");
                break;
            }else if(risposta.equals("2")){
                getGestoreClient().manda("Hai lasciato a terra la nuova arma...");
                break;
            }else{
                getGestoreClient().manda("Opzione non valida!");
            }
        }
    }

    public void mangia(Cibo cibo){
        int nuovaVita = getPuntiVita() + cibo.getPuntiVita();
        setPuntiVita(Math.min(nuovaVita, getMaxPuntiVita()));
        getGestoreClient().manda("Hai mangiato 1 " + cibo.getNome() + " e hai recuperato "+ cibo.getPuntiVita() +" HP!");
    }

    public Arma getArma() {
        return arma;
    }

    public void setArma(Arma arma) {
        this.arma = arma;
    }
}
