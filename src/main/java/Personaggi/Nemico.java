package Personaggi;

import java.util.Random;

public abstract class Nemico extends PersonaggioCombattente {
    private final int dropEsperienza;
    private final int dropSoldi;
    private final String dropOggetto;
    private int quantitaDrop;
    private final String tipo;
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
        // HP minimi dei nemici al livello 1 del giocatore
        // HP massimi dei nemici al livello 1 del giocatore
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

    public String getTipo() {
        return tipo;
    }

    protected String generaDrop() {
        String[] opzioni = {"mela", "pane", "cioccolato", "banana", "pollo", "manzo"};
        int indiceCasuale = random.nextInt(opzioni.length);
        return opzioni[indiceCasuale];
    }

    protected int generaQuantitaDrop(){
        return random.nextInt(2)+1;
    }

    public String getDropOggetto() {
        return dropOggetto;
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

    /**
     * Restituisce la flase detta dal nemico a inizio combattimento
     */
    public abstract String getFrase();
}
