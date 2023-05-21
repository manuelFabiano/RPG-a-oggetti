import java.util.Random;

public enum NomeNemico { Artorias, Ayla, Cedric, Elara, Farin, Giselle, Hadrian, Isolde, Jareth, Kira, Lysander, Morgana, Naelle, Oberon, Persephone, Quinlan, Rowena, Seraphina, Tristan, Vivienne, Alaric, Brielle, Caelan, Darius, Elysia, Finnian, Genevieve, Helena, Ignatius, Juliana, Kael, Leandra, Maximus, Niamh, Orion, Rosalind, Silas, Theodora, Valerian, Xanthe;
    private static final NomeNemico[] VALUES = values();
    private static final int SIZE = VALUES.length;
    private static final Random RANDOM = new Random();
    public static String getNomeRandom() {
        return VALUES[RANDOM.nextInt(SIZE)].toString();
    }
}

