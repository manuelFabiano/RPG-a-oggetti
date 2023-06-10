package Personaggi;

public abstract class PersonaggioCombattente extends Personaggio{

    private int maxPuntiVita = 0;
    private int puntiVita = 0;
    private int puntiAttacco = 0;
    private int puntiDifesa = 0;
    private int puntiAgilità = 0;
    private String status;

    public PersonaggioCombattente() {
        super();
        status = "";
    }

    public void subisciDanni(int danni){
        puntiVita -= danni;
    }
    public boolean isVivo(){
        return puntiVita > 0;
    }

    public int getPuntiAgilità() {
        return puntiAgilità;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void stampaStats(){
        System.out.println(getPuntiAttacco()+ " " + getPuntiDifesa() + " " +  getPuntiAgilità());
    }
}
