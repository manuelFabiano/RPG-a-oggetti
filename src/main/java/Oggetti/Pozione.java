package Oggetti;

public class Pozione extends Oggetto{
    private final String tipo;
    private final int punti;

    public Pozione(String nome, String descrizione, String tipo, int punti) {
        super(nome, descrizione);
        this.tipo = tipo;
        this.punti = punti;
    }
    public String getTipo() {
        return tipo;
    }
    public int getPunti() {
        return punti;
    }
}
