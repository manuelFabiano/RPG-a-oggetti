package Personaggi;

import Server.Gioco;
import Server.InterfacciaGestoreClient;

import java.io.IOException;

public class Brigante extends Umano{
    public Brigante(InterfacciaGestoreClient gestoreClient, int livelloGiocatore) {
        super(gestoreClient, "Brigante", livelloGiocatore);
        //stats
        setPuntiVita(generaPuntiVita(livelloGiocatore, 8, 16));

    }

    @Override
    public void interagisci(Gioco gioco) throws IOException {
        while (true) {
            gioco.getGestoreClient().manda("Ti ritrovi davanti a una periferia cittadina. Un brigante ti si avvicina.\n" +
                    "Brigante: Ue ue, uaglio'! Dove vai così di fretta?\n");
            gioco.getGestoreClient().manda("Cosa rispondi?");
            gioco.getGestoreClient().manda("1. Non ho una meta precisa");
            gioco.getGestoreClient().manda("2. Non sono affari tuoi\nPASS");

            String scelta = gioco.getGestoreClient().ricevi();
            if (scelta.equals("1")) {
                gioco.getGestoreClient().manda("Brigante: Fai un'offerta per la festa del villaggio.\n");
                gioco.sleep();

                while(true) {
                    gioco.getGestoreClient().manda("Il brigante vuole rubarti dei soldi!");
                    gioco.getGestoreClient().manda("Cosa rispondi?");
                    gioco.getGestoreClient().manda("1. Non ho monete.");
                    gioco.getGestoreClient().manda("2. Tieni.\nPASS");
                    String nuova_scelta = gioco.getGestoreClient().ricevi();
                    if (nuova_scelta.equals("1")) {
                        gioco.getGestoreClient().manda("Il brigante non ti crede e ti attacca\n");
                        gioco.Combattimento(this);
                    } else {
                        int monete = random.nextInt(18)+2; //da 2 a 20 monete
                        if (gioco.getGiocatore().getSoldi() >= monete) {
                            gioco.getGestoreClient().manda("Hai dato " + monete + " monete al brigante!");
                            gioco.getGiocatore().decrementaSoldi(monete);
                            gioco.sleep();
                            gioco.getGestoreClient().manda("Brigante: hai fatto la scelta giusta, arrivederci!");
                        }else{
                            gioco.getGestoreClient().manda("Ma non hai niente, sei più povero di me!\n");
                            gioco.Combattimento(this);
                        }
                    }
                    break;
                }
                break;
            } else if (scelta.equals("2")) {
                //inizia il combattimento
                gioco.Combattimento(this);
                break;
            } else {
                gioco.getGestoreClient().manda("Scelta non valida. Riprova.");
            }
        }
    }


    @Override
    public String getFrase() {
        return getNome() + ": Qui non sei il benvenuto. Muori!";
    }
}

