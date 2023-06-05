package Personaggi;

import java.util.Random;

public abstract class Nemico extends PersonaggioCombattente {
    private int dropEsperienza;
    private final int dropSoldi;
    private String dropOggetto;
    private int quantitaDrop;
    private String tipo;
    public Random random = new Random();

    public Nemico(String tipo, int livelloGiocatore) {
        super();
        this.dropEsperienza = generaDropEsperienza();
        this.dropSoldi = generaDropSoldi();
        this.dropOggetto = generaDrop();
        this.quantitaDrop = generaQuantitaDrop();
        this.tipo = tipo;
        //Stats
        setPuntiAttacco(generaPuntiAttacco(livelloGiocatore));
        setPuntiDifesa(generaPuntiDifesa(livelloGiocatore));
        setPuntiAgilità(generaPuntiAgilità(livelloGiocatore));
    }

    protected int generaPuntiVita(int livelloGiocatore, int hpMin, int hpMax) {
        // Formula per determinare gli HP dei nemici in base al livello del giocatore
        // HP minimi dei nemici a livello 1 del giocatore
        // HP massimi dei nemici a livello 1 del giocatore
        int hpBase = hpMin + random.nextInt(hpMax-hpMin);

        int hpLivello= (random.nextInt(2)+1) * (livelloGiocatore-1); //aggiunge da 1 a 2 hp per il livello del giocaotore a partire dal livello 2

        return hpBase + hpLivello;
    }

    protected int generaPuntiAttacco(int livelloGiocatore){
        int puntiBase = 5 + (random.nextInt(4)-2);
        return puntiBase + livelloGiocatore;
    }
    protected int generaPuntiDifesa(int livelloGiocatore){
        int puntiBase = 5 + (random.nextInt(4)-2);
        return puntiBase + livelloGiocatore;
    }
    protected int generaPuntiAgilità(int livelloGiocatore){
        int puntiBase = 5 + (random.nextInt(4)-2);
        return puntiBase + livelloGiocatore;
    }
    protected int generaDropEsperienza(){
        int dropBase = 500;
        return dropBase + (random.nextInt(501));
    }

    protected int generaDropSoldi(){
        return random.nextInt(15)+1;
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

    protected int generaQuantitaDrop(){
        return random.nextInt(2)+1;
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

    public int getDropSoldi() {
        return dropSoldi;
    }

    //Stampa la flase detta dal nemico a inizio combattimento
    public abstract String getFrase();
}
