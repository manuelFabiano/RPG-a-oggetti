package Personaggi;

import Server.InterfacciaGestoreClient;

public abstract class Personaggio {
    private InterfacciaGestoreClient gestoreClient;
    private int maxPuntiVita = 0;
    private int puntiVita = 0;
    private int puntiAttacco = 0;
    private int puntiDifesa = 0;
    private int puntiAgilità = 0;


    public void subisciDanni(int danni){
        puntiVita -= danni;
    }
    public boolean isVivo(){
        return puntiVita > 0;
    }

    public int getPuntiAgilità() {
        return puntiAgilità;
    }

    public InterfacciaGestoreClient getGestoreClient() {
        return gestoreClient;
    }

    public void setGestoreClient(InterfacciaGestoreClient gestoreClient) {
        this.gestoreClient = gestoreClient;
    }

    public void setPuntiAgilità(int puntiAgilità) {
        this.puntiAgilità = puntiAgilità;
    }

    public int getPuntiVita() {
        return puntiVita;
    }

    public void setPuntiVita(int puntiVita) {
        this.puntiVita = puntiVita;
    }

    public int getPuntiAttacco() {
        return puntiAttacco;
    }

    public void setPuntiAttacco(int puntiAttacco) {
        this.puntiAttacco = puntiAttacco;
    }

    public int getPuntiDifesa() {
        return puntiDifesa;
    }

    public void setPuntiDifesa(int puntiDifesa) {
        this.puntiDifesa = puntiDifesa;
    }

    public int getMaxPuntiVita() {
        return maxPuntiVita;
    }

    public void setMaxPuntiVita(int maxPuntiVita) {
        this.maxPuntiVita = maxPuntiVita;
    }
}
