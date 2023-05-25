package Personaggi;

import java.io.BufferedReader;
import java.io.PrintWriter;

public abstract class Personaggio {
    protected BufferedReader input;
    protected PrintWriter output;
    protected int puntiVita;
    protected int puntiAttacco;
    protected int puntiDifesa;
    protected int puntiAgilità;

    public BufferedReader getInput() {
        return input;
    }

    public PrintWriter getOutput() {
        return output;
    }

    public int getPuntiAgilità() {
        return puntiAgilità;
    }

    public void setInput(BufferedReader input) {
        this.input = input;
    }

    public void setOutput(PrintWriter output) {
        this.output = output;
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
