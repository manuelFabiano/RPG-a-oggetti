package Locations;

public class Lago extends Locations {

    public Lago(String nome, String descrizione) {
        super(nome, descrizione);
    }

    @Override
    public String getNome() {
        return "Lago";
    }
    @Override
    public String getDescrizione() {
        return "Ti trovi in un lago con sulla riva la casa di un pescatore.";
    }
}
