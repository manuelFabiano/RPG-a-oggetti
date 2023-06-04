package Personaggi;

import Server.Gioco;

import java.io.IOException;

public class Brigante extends Umano{
    public Brigante(Giocatore giocatore) {
        setTipo("Brigante");
        setNome();
        //stats
        setPuntiVita(generaPuntiVita(giocatore.getLivello(), 5, 12));
        setPuntiAttacco(generaPuntiAttacco(giocatore.getPuntiAttacco()));
        setPuntiDifesa(generaPuntiDifesa(giocatore.getPuntiDifesa()));
        setPuntiAttacco(generaPuntiAgilità(giocatore.getPuntiAgilità()));
        //drop esperienza
        setDropEsperienza(generaDropEsperienza());
        //drop oggetti
        setDropOggetto(generaDrop());
        setQuantitaDrop(random.nextInt(2)+1);

    }

    public void interagisci(Gioco gioco, Giocatore giocatore) throws IOException {
        while (true) {
            gioco.getGestoreClient().manda("Ti ritrovi davanti a una periferia cittadina. Un brigante ti si avvicina.\n" +
                    "Brigante: Ue ue, uaglio'! Dove vai così di fretta?\n");
            gioco.getGestoreClient().manda("Cosa rispondi?");
            gioco.getGestoreClient().manda("1. Non ho una meta precisa");
            gioco.getGestoreClient().manda("2. Sto perlustrando la zona");
            gioco.getGestoreClient().manda("3. Non sono affari tuoi\nPASS");

            String scelta = gioco.getGestoreClient().ricevi();
            if (scelta.equals("1")) {
                gioco.getGestoreClient().manda("Brigante: Fai un'offerta per la festa del villaggio.\n");
                gioco.sleep();
                gioco.getGestoreClient().manda("Il brigante ti estorce i soldi!");
                gioco.getGestoreClient().manda("Cosa rispondi?");
                gioco.getGestoreClient().manda("1. Non ho monete.");
                gioco.getGestoreClient().manda("2. Tieni.");
                String nuova_scelta = gioco.getGestoreClient().ricevi();
                if (nuova_scelta.equals("1")) {
                    gioco.getGestoreClient().manda("Il brigante non ti crede e ti attacca");
                    gioco.Combattimento(this);
                } else {
                    gioco.getGestoreClient().manda("Dai 20 monete al brigante.");
                    giocatore.decrementaSoldi(20);
                }
                //perdi 20 soldi
                break;
            } else if (scelta.equals("2")) {
                gioco.getGestoreClient().manda("Brigante: Fai un'offerta ai nostri Orfani!\n" +
                        "Muoiono di fame!");
                gioco.sleep();
                int azione = random.nextInt(100)+1;
                if(azione < 50) {
                    gioco.getGestoreClient().manda("Il brigante ti ruba del cibo");
                    gioco.getGestoreDb().decrementaQuantita(gioco, "Cioccolato");
                } else {
                    gioco.getGestoreClient().manda("Il brigante non trova il cibo e ti attacca");
                    gioco.Combattimento(this);
                }
                break;
            } else if (scelta.equals("3")) {
                gioco.getGestoreClient().manda("Brigante: Non sei il bevenuto. Muori!");
                //inizia il combattimento
                gioco.Combattimento(this);
                break;
            } else {
                gioco.getGestoreClient().manda("Scelta non valida. Riprova.");
            }
        }
    }


}

