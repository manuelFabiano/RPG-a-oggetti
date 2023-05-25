package Personaggi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Giocatore extends Personaggio {
    private int livello;

    public Giocatore(BufferedReader input, PrintWriter output) throws IOException {
        this.output = output;
        this.input = input;
        puntiVita = 20;
        livello = 1;
        while (true) {
            this.output.println("Hai 10 punti a disposizione da assegnare alle tue abilità!\n"
                    + "- Attacco\n- Difesa\n- Agilità\n"
                    + "Quanti punti di Attacco vuoi assegnare?\nPASS");

            puntiAttacco = Integer.parseInt(this.input.readLine());

            if (puntiAttacco < 1 || puntiAttacco > 9) {
                this.output.println("Puoi assegnare da 1 a 8 punti ad Attacco.");
                continue;
            }
            int puntiRimanenti = 10 - puntiAttacco;
            this.output.println("Bene! Ti rimangono " + puntiRimanenti + " punti da assegnare alle tue abilità!\n"
                    + "- Difesa\n- Agilità\n"
                    + "Quanti punti di Difesa vuoi assegnare?\nPASS");

            puntiDifesa = Integer.parseInt(this.input.readLine());

            if (puntiDifesa < 1 || puntiDifesa > (puntiRimanenti-1)) {
                this.output.println("Puoi assegnare da 1 a " + (puntiRimanenti-1) +" punti alla Difesa.");
                continue;
            }

            puntiRimanenti -= puntiDifesa;
            this.output.println("Bene! Ti rimangono " + puntiRimanenti + " punti che verranno assegnati all' Agilità!");
            puntiAgilità = puntiRimanenti;
            break;
        }

        this.output.println("Hai assegnato i punti correttamente:\n"
                + "Attacco: " + puntiAttacco + "\n"
                + "Difesa: " + puntiDifesa + "\n"
                + "Agilità: " + puntiAgilità);
    }

    public int getLivello() {
        return livello;
    }
}
