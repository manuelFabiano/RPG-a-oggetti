package Personaggi;

import Server.InterfacciaGestoreClient;

public abstract class Personaggio {
    private InterfacciaGestoreClient gestoreClient;
    private String nome;

    public void setNome() {
        this.nome = Nomi.getNomeRandom();
    }

    public String getNome() {
        return nome;
    }

    public void setGestoreClient(InterfacciaGestoreClient gestoreClient) {
        this.gestoreClient = gestoreClient;
    }

    public InterfacciaGestoreClient getGestoreClient() {
        return gestoreClient;
    }
}
