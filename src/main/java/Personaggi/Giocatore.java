package Personaggi;


import Server.InterfacciaGestoreClient;
import org.bson.Document;

import java.io.IOException;

public class Giocatore extends Personaggio {
    private int livello;
    private int esperienza;


    //Costruttore personaggio del giocatore quando si inizia una nuova partita
    public Giocatore(InterfacciaGestoreClient gestoreClient) throws IOException {
        setGestoreClient(gestoreClient);
        setPuntiVita(20);
        livello = 1;
        esperienza = 0;
        assegnaPunti(15);
    }

    public Giocatore(InterfacciaGestoreClient gestoreClient, Document partita){
        setGestoreClient(gestoreClient);
        setPuntiVita(partita.getInteger("puntiVita"));
        setPuntiAttacco(partita.getInteger("puntiAttacco"));
        setPuntiDifesa(partita.getInteger("puntiDifesa"));
        setPuntiAgilità(partita.getInteger("puntiAgilità"));
        livello = partita.getInteger("livello");
        esperienza = partita.getInteger("esperienza");
    }

    public void aumentaEsperienza(int esperienza)throws IOException{
        this.esperienza += esperienza;
        while(this.esperienza > 1000){
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

}
