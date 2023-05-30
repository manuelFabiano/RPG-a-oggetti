package Locations;

import Oggetti.Arma;
import Personaggi.Orco;
import Server.Gioco;
import Server.InterfacciaGestoreClient;

import java.io.IOException;
import java.util.Random;

public class Bosco extends Locations {
    Random random;

    public Bosco(InterfacciaGestoreClient gestoreClient) {
        String descrizione = "Ti ritrovi di fronte a un bosco tetro e spaventoso.\n" +
                "L'aria è densa di un odore terroso e umido, mescolato con un vago sentore di decomposizione.\n" +
                "Ogni tanto, un ramo si spezza improvvisamente, rompendo il silenzio e facendoti sobbalzare.";
        super.setDescrizione(descrizione);
        super.setGestoreClient(gestoreClient);
        random = new Random();
    }

    @Override
    public void esplora(Gioco gioco) throws IOException {
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
                        Orco orco = new Orco(gioco.getGiocatore());
                        gioco.Combattimento(orco);
                        break;
                    } else if (avvicinamento.equals("2")) {
                        getGestoreClient().manda("Prosegui verso una luce trovando l'uscita del bosco!");
                        int mele = random.nextInt(3)+1;
                        getGestoreClient().manda("Camminando verso l'uscita hai raccolto " + mele + " Mele!");
                        gioco.getGestoreDb().incrementaQuantita(gioco.getUtente(), gioco.getChiavePartita(), "mela", mele);


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

    @Override
    protected Arma creaArma() {
        return null;
    }
}