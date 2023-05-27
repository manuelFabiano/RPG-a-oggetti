package Locations;

import Server.InterfacciaGestoreClient;

import java.io.IOException;

public class Bosco extends Locations {

    public Bosco(InterfacciaGestoreClient gestoreClient) {
        String descrizione = "Ti ritrovi di fronte a un bosco tetro e spaventoso.\n" +
                "L'aria è densa di un odore terroso e umido, mescolato con un vago sentore di decomposizione.\n" +
                "Ogni tanto, un ramo si spezza improvvisamente, rompendo il silenzio e facendoti sobbalzare.";
        super.setDescrizione(descrizione);
        super.setGestoreClient(gestoreClient);
    }

    @Override
    public void esplora() throws IOException {
        while (true) {
            getGestoreClient().manda(getDescrizione());
            getGestoreClient().manda("1. Esplora il bosco");
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
                        getGestoreClient().manda("Trovi dei cadaveri ridotti a poltiglia e vieni attaccato da un orco!");
                        // Inserire orco
                        break;
                    } else if (avvicinamento.equals("2")) {
                        getGestoreClient().manda("Prosegui verso una luce trovando l'uscita del bosco!");
                        // continua il loop con il prossimo livello
                        break;
                    } else if (avvicinamento.equals("3")) {
                        getGestoreClient().manda("Trovi una tenda al cui interno risiede un mercante");
                        // Inserire mercante
                    }else {
                        getGestoreClient().manda("Scelta non valida. Riprova.");
                    }
                }
                break;
            } else if (scelta.equals("2")) {
                getGestoreClient().manda("Ti allontani dal bosco e continui la tua avventura.");
                break;
            } else {
                getGestoreClient().manda("Scelta non valida. Riprova.");
            }
        }
    }
}