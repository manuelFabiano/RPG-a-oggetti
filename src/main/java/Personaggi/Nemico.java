package Personaggi;

import java.util.Random;

public abstract class Nemico extends Personaggio{
    private String nome;
    private String tipo;
    private String dropEsperienza;
    protected Random random;

    protected int generaPuntiVita(int livelloGiocatore, int hpMin, int hpMax) {
        random = new Random();
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
        return puntiDifesaGiocatore + puntiDifesaRandom;
    }
    protected int generaPuntiAgilità(int puntiAgilitàGiocatore){
        int puntiAgilitàRandom = random.nextInt(5) - 2; //genera un numero casuale da -2 a 2
        return puntiAgilitàGiocatore + puntiAgilitàRandom;
    }

    public void setNome() {
        this.nome = Nomi.getNomeRandom();
    }

    public String getNome() {
        return nome;
    }

    public String getDropEsperienza() {
        return dropEsperienza;
    }


    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
