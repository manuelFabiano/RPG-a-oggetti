package Personaggi;

import Server.InterfacciaGestoreClient;
import java.io.BufferedReader;
import java.io.PrintWriter;

public abstract class Personaggio {
    private InterfacciaGestoreClient gestoreClient;
    private int puntiVita;
    private int puntiAttacco;
    private int puntiDifesa;
    private int puntiAgilità;


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


}
