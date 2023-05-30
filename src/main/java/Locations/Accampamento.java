package Locations;

import Oggetti.Arma;
import Server.Gioco;
import Server.InterfacciaGestoreClient;

import java.io.IOException;
import java.util.Random;

public class Accampamento extends Locations {

    public Accampamento(InterfacciaGestoreClient gestoreClient) {
        String descrizione = "Ti trovi in un accampamento solitario. Un fuoco da campo brucia lentamente e una piccola tenda ti attende per riposare.\n" +
                "L'atmosfera è tranquilla e rilassante, perfetta per una pausa rigenerante.";
        super.setDescrizione(descrizione);
        super.setGestoreClient(gestoreClient);
    }

    @Override
    public void esplora(Gioco gioco) throws IOException {
        while (true) {
            getGestoreClient().manda(getDescrizione());
            getGestoreClient().manda("1. Dormi");
            getGestoreClient().manda("2. Esplora l'area circostante");
            getGestoreClient().manda("3. Allontanati\nPASS");

            String scelta = getGestoreClient().ricevi();
            if (scelta.equals("1")) {
                // Recupera tutta la vita del giocatore
                gioco.getGiocatore().setPuntiVita(gioco.getGiocatore().getMaxPuntiVita());
                getGestoreClient().manda("Hai dormito e recuperato tutta la vita!");
                break;
            } else if (scelta.equals("2")) {
                // Esplorazione dell'area circostante
                getGestoreClient().manda("Esplori l'area circostante dell'accampamento...");
                gioco.sleep();
                getGestoreClient().manda("Con grande sorpresa trovi una spada dietro a una grande quercia.");
                Arma spada = creaArma();
                gioco.getGiocatore().nuovaArma(spada);
                break;
            } else if (scelta.equals("3")) {
                getGestoreClient().manda("Ti allontani dall'accampamento e continui la tua avventura.");
                break;
            } else {
                getGestoreClient().manda("Scelta non valida. Riprova.");
            }
        }
    }

    @Override
    protected Arma creaArma() {
        String descrizioneArma = "Un'ardente spada infusa di potere elementale, le sue lame fiammeggianti danzano nel vento come lingue di fuoco assetate di battaglia.";
        Random random = new Random();
        int danniBase = 8 + random.nextInt(11);
        return new Arma("Spada ardente", descrizioneArma , danniBase, "Fuoco");
    }
}

