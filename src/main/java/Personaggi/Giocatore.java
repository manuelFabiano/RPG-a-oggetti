package Personaggi;


import Server.InterfacciaGestoreClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Giocatore extends Personaggio {
    private final int livello;
    private final int esperienza;


    //Costruttore personaggio del giocatore quando si inizia una nuova partita
    public Giocatore(InterfacciaGestoreClient gestoreClient) throws IOException {
        setGestoreClient(gestoreClient);
        setPuntiVita(20);
        livello = 1;
        esperienza = 0;
        while (true) {
            getGestoreClient().manda("Hai 15 punti a disposizione da assegnare alle tue abilità!\n"
                    + "- Attacco\n- Difesa\n- Agilità\n"
                    + "Quanti punti di Attacco vuoi assegnare?\nPASS");

            setPuntiAttacco(Integer.parseInt(getGestoreClient().ricevi()));

            if (getPuntiAttacco() < 1 || getPuntiAttacco() > 14) {
                getGestoreClient().manda("Puoi assegnare da 1 a 13 punti ad Attacco.");
                continue;
            }
            int puntiRimanenti = 15 - getPuntiAttacco();
            getGestoreClient().manda("Bene! Ti rimangono " + puntiRimanenti + " punti da assegnare alle tue abilità!\n"
                    + "- Difesa\n- Agilità\n"
                    + "Quanti punti di Difesa vuoi assegnare?\nPASS");

            setPuntiDifesa(Integer.parseInt(getGestoreClient().ricevi()));

            if (getPuntiDifesa() < 1 || getPuntiDifesa() > (puntiRimanenti-1)) {
                getGestoreClient().manda("Puoi assegnare da 1 a " + (puntiRimanenti-1) +" punti alla Difesa.");
                continue;
            }

            puntiRimanenti -= getPuntiDifesa();
            getGestoreClient().manda("Bene! Ti rimangono " + puntiRimanenti + " punti che verranno assegnati all' Agilità!");
            setPuntiAgilità(puntiRimanenti);
            break;
        }

        getGestoreClient().manda("Hai assegnato i punti correttamente:\n"
                + "Attacco: " + getPuntiAttacco() + "\n"
                + "Difesa: " + getPuntiDifesa() + "\n"
                + "Agilità: " + getPuntiAgilità());
    }

    public int getLivello() {
        return livello;
    }

    public int getEsperienza() {
        return esperienza;
    }
}
