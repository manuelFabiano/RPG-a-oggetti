package Personaggi;

import java.util.Random;

public abstract class Nemico extends PersonaggioCombattente {
    private int dropEsperienza;
    private String dropOggetto;
    private int quantitaDrop;
    private String tipo;
    public Random random = new Random();


    protected int generaPuntiVita(int livelloGiocatore, int hpMin, int hpMax) {
        // Formula per determinare gli HP dei nemici in base al livello del giocatore
        //Sfrutta una proporzione di aumento degli HP per ogni livello acquisito dal giocatore e aggiunge un fattore casuale
        // HP minimi dei nemici a livello 1 del giocatore
        // HP massimi dei nemici a livello 1 del giocatore
        double proporzione = 0.2;  // Proporzione di aumento degli HP per ogni livello

        int hpBase = hpMin + (int) (proporzione * livelloGiocatore * (hpMax - hpMin));
        int hpRandom = random.nextInt(hpBase / 2) + 1;  // Aggiunge un fattore casuale ai HP

        return hpBase + hpRandom;
    }

    protected int generaPuntiAttacco(int puntiAttaccoGiocatore){
        int puntiAttaccoRandom = random.nextInt(5) - 2; //genera un numero casuale da -2 a 2
        return puntiAttaccoGiocatore + puntiAttaccoRandom;
    }
    protected int generaPuntiDifesa(int puntiDifesaGiocatore){
        int puntiDifesaRandom = random.nextInt(5) - 2; //genera un numero casuale da -2 a 2
        int puntiDifesa = puntiDifesaGiocatore + puntiDifesaRandom;
        return Math.max(puntiDifesa, 0);
    }
    protected int generaPuntiAgilità(int puntiAgilitàGiocatore){
        int puntiAgilitàRandom = random.nextInt(5) - 2; //genera un numero casuale da -2 a 2
        return puntiAgilitàGiocatore + puntiAgilitàRandom;
    }
    protected int generaDropEsperienza(){
        int dropBase = 500;
        return dropBase + (random.nextInt(501));
    }

    public int getDropEsperienza() {
        return dropEsperienza;
    }

    public void setDropEsperienza(int dropEsperienza) {
        this.dropEsperienza = dropEsperienza;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    protected String generaDrop() {
        int numeroRandom = random.nextInt(6);
        switch (numeroRandom) {
            case 0:
                return "mela";
            case 1:
                return "pane";
            case 2:
                return "cioccolato";
            case 3:
                return "banana";
            case 4:
                return "pollo";
            case 5:
                return "manzo";
        }
        return null;
    }

    public String getDropOggetto() {
        return dropOggetto;
    }

    public void setDropOggetto(String dropOggetto) {
        this.dropOggetto = dropOggetto;
    }

    public int getQuantitaDrop() {
        return quantitaDrop;
    }

    public void setQuantitaDrop(int quantitaDrop) {
        this.quantitaDrop = quantitaDrop;
    }
}
