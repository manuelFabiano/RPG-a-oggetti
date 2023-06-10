package Locations;

import Oggetti.Arma;
import Personaggi.Cavaliere;
import Personaggi.Mercante;
import Personaggi.Orco;
import Personaggi.Strega;
import Server.Gioco;
import Server.InterfacciaGestoreClient;

import java.io.IOException;
import java.util.Random;

public class Caverna extends Locations {
    Random random;

    public Caverna(InterfacciaGestoreClient gestoreClient) {
        String descrizione = "Ti ritrovi in una caverna.\n" +
                "Questo luogo misterioso e avvolto nell'oscurità cela segreti e pericoli ineguagliabili.\n" +
                "Mentre entri nella caverna, l'aria si fa fredda e umida, e il suono del tuo respiro risuona nel buio.\n" +
                "La tua lanterna illumina appena i contorni delle pareti, lasciando molte zone ancora avvolte nell'ombra.";
        super.setDescrizione(descrizione);
        super.setGestoreClient(gestoreClient);
        random = new Random();
    }

    @Override
    public void esplora(Gioco gioco) throws IOException {
        while (true) {
            getGestoreClient().manda(getDescrizione());
            getGestoreClient().manda("1. Esplora la caverna");
            getGestoreClient().manda("2. Allontanati\nPASS");

            String scelta = getGestoreClient().ricevi();
            if (scelta.equals("1")) {
                while (true) {
                    getGestoreClient().manda("Dove vuoi andare?");
                    getGestoreClient().manda("1. Verso destra");
                    getGestoreClient().manda("2. Continui dritto");
                    getGestoreClient().manda("3. Verso sinistra\nPASS");

                    String avvicinamento = getGestoreClient().ricevi();
                    if (avvicinamento.equals("1")) {
                        getGestoreClient().manda("Scorgi degli altarini e all'improvviso vieni attaccato da una strega!");
                        // Inserire orco
                        Strega strega = new Strega(gioco.getGiocatore().getLivello());
                        gioco.Combattimento(strega);
                        break;
                    } else if (avvicinamento.equals("2")) {
                        getGestoreClient().manda("Prosegui verso una luce trovando e incontri qualcuno");
                        Cavaliere cavaliere = new Cavaliere(gioco.getGestoreClient(), gioco.getGiocatore().getLivello());
                        cavaliere.interagisci(gioco);

                        break;
                    } else if (avvicinamento.equals("3")) {
                        getGestoreClient().manda("Trovi un forziere con dentro un'ascia!");
                        Arma ascia = creaArma();
                        gioco.getGiocatore().nuovaArma(ascia);
                        break;
                    }else {
                        getGestoreClient().manda("Scelta non valida. Riprova.");
                    }
                }
                break;
            } else if (scelta.equals("2")) {
                getGestoreClient().manda("Ti allontani dalla caverna e continui la tua avventura.");
                break;
            } else {
                getGestoreClient().manda("Scelta non valida. Riprova.");
            }
        }
    }

    @Override
    protected Arma creaArma() {
        return null;
    }
}
