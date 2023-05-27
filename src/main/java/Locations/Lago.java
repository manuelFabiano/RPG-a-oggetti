package Locations;

import Server.InterfacciaGestoreClient;

import java.io.IOException;

public class Lago extends Locations {

    public Lago(InterfacciaGestoreClient gestoreClient) {
        String descrizione = "Davanti a te si estende un lago sereno, le sue acque calme e cristalline riflettono i raggi del sole.\n" +
                "Lungo la riva si erge la modesta casa di un pescatore, con il suo tetto di paglia e le pareti di legno scuro.\n" +
                "L'aria è impregnata di profumo di alghe e di salsedine, mentre i suoni della natura si mescolano al dolce canto degli uccelli acquatici.";
        super.setDescrizione(descrizione);
        super.setGestoreClient(gestoreClient);
    }

    @Override
    public void esplora() throws IOException {
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
                        getGestoreClient().manda("Entri nella casa del pescatore e trovi una spada!");
                        // Inserire la logica per assegnare la spada al giocatore
                        break;
                    } else if (avvicinamento.equals("2")) {
                        getGestoreClient().manda("Mentre ti avvicini alla riva del lago, incontri un goblin!");
                        // Inserire la logica per avviare un combattimento con il goblin
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
}