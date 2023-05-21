import java.util.ArrayList;
import java.util.List;

public class BorsaOggetti {
    private List<Oggetti> oggetti;

    public BorsaOggetti() {
        oggetti = new ArrayList<>();
    }

    public void aggiungiOggetto(Oggetti oggetto) {
        oggetti.add(oggetto);
    }

    public void rimuoviOggetto(Oggetti oggetto) {
        oggetti.remove(oggetto);
    }

    public void stampaContenuto() {
        for (Oggetti oggetto : oggetti) {
            System.out.println("Oggetto: " + oggetto.getNome() + ", Peso: " + oggetto.getQuantita());
        }
    }
}

