package Oggetti;

public class Oggetto {
    private String nome;
    private String descrizione;
    private int prezzo;

    /**
     * Costruttore usato dalle sottoclassi
     * */
    public Oggetto(String nome, String descrizione) {
        this.nome = nome;
        this.descrizione = descrizione;
    }

    /**
     * Costruttore usato nella classe Mercante
     */
    public Oggetto(String nome, int prezzo) {
        this.nome = nome;
        this.prezzo = prezzo;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getDescrizione() {
        return descrizione;
    }
    public int getPrezzo() {
        return prezzo;
    }

}
