package Oggetti;

public class Oggetto {
    private String nome;
    private String descrizione;
    private int prezzo;


    public Oggetto(String nome, String descrizione) {
        this.nome = nome;
        this.descrizione = descrizione;
    }

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

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public int getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(int prezzo) {
        this.prezzo = prezzo;
    }
}
