import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Giocatore {
    private BufferedReader input;
    private PrintWriter output;
    private int puntiVita;
    private int puntiAttacco;
    private int puntiDifesa;
    private int livello;

    public Giocatore(BufferedReader input, PrintWriter output) throws IOException {
        this.output = output;
        this.input = input;
        puntiVita = 20;
        livello = 1;
        this.output.println("Hai 10 punti a disposizione da assegnare alle tue abilità!\n"+
                "Quanti punti di Attacco vuoi assegnare?\nPASS");
        puntiAttacco = Integer.parseInt(this.input.readLine());
        puntiDifesa = 10 - puntiAttacco;
        this.output.println("Bene! Ti rimangono " + puntiDifesa + " punti che verranno assegnati ai punti di Difesa!\nPASS");
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

    public int getLivello() {
        return livello;
    }

    public void setLivello(int livello) {
        this.livello = livello;
    }
}
