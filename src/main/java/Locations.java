import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Locations {
    private String nome;
    private String descrizione;

    // Costruttore
    public Locations(String nome, String descrizione) {
        this.nome = nome;
        this.descrizione = descrizione;
    }

    // Metodi getter
    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }
    List<Locations> listaLocations = new ArrayList<>();
listaLocations.add(new Locations("Casa", "Ti trovi nella tua accogliente casa."));
listaLocations.add(new Locations("Foresta", "Ti trovi in una fitta foresta."));
listaLocations.add(new Locations("Caverna", "Ti trovi in una buia caverna."));
listaLocations.add(new Locations("Lago", "Ti trovi in un lago ricco di pesci."));
// Aggiungi altre location alla lista

    Random random = new Random();
    int indiceCasuale = random.nextInt(listaLocations.size());
    Locations locationCasuale = listaLocations.get(indiceCasuale);

System.out.println("Sei a " + locationCasuale.getNome());
System.out.println(locationCasuale.getDescrizione());

}
