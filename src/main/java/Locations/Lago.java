package Locations;

import Oggetti.Arma;
import Personaggi.Goblin;
import Server.Gioco;
import Server.InterfacciaGestoreClient;

import java.io.IOException;
import java.util.Random;

public class Lago extends Locations {

    public Lago(InterfacciaGestoreClient gestoreClient) {
        String descrizione = "Davanti a te si estende un lago sereno, le sue acque calme e cristalline riflettono i raggi del sole.\n" +
                "Lungo la riva si erge la modesta casa di un pescatore, con il suo tetto di paglia e le pareti di legno scuro.\n" +
                "L'aria è impregnata di profumo di alghe e di salsedine, mentre i suoni della natura si mescolano al dolce canto degli uccelli acquatici.";
        super.setDescrizione(descrizione);
        super.setGestoreClient(gestoreClient);
    }

    @Override
    public void esplora(Gioco gioco) throws IOException {
        while (true) {
            getGestoreClient().manda(getDescrizione());
            getGestoreClient().manda("1. Esplora il lago");
            getGestoreClient().manda("2. Allontanati\nPASS");

            String scelta = getGestoreClient().ricevi();
            if (scelta.equals("1")) {
                while (true) {
                    getGestoreClient().manda("Dove vuoi avvicinarti?");
                    getGestoreClient().manda("1. Casa del pescatore");
                    getGestoreClient().manda("2. Riva del lago\nPASS");

                    String avvicinamento = getGestoreClient().ricevi();
                    if (avvicinamento.equals("1")) {
                        getGestoreClient().manda("Entri nella casa del pescatore e trovi un'ascia!");
                        Arma ascia = creaArma();
                        gioco.getGiocatore().nuovaArma(ascia);
                        break;
                    } else if (avvicinamento.equals("2")) {
                        getGestoreClient().manda("Mentre ti avvicini alla riva del lago, incontri un goblin!");
                        Goblin goblin = new Goblin(gioco.getGiocatore().getLivello());
                        gioco.Combattimento(goblin);
                        break;
                    } else {
                        getGestoreClient().manda("Scelta non valida. Riprova.");
                    }
                }
                break;
            } else if (scelta.equals("2")) {
                getGestoreClient().manda("Ti allontani dal lago e continui la tua avventura.");
                break;
            } else {
                getGestoreClient().manda("Scelta non valida. Riprova.");
            }
        }
    }

    @Override
    protected Arma creaArma(){
        String descrizioneArma = "Un'ascia affilata, forgiata con maestria e incisa con simboli runici.";
        Random random = new Random();
        int danniBase = 5 + random.nextInt(11);
        return new Arma("Ascia affilata", descrizioneArma , danniBase, "Nessuna");
    }
}
