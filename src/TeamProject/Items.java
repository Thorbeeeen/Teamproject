package TeamProject;

import java.util.Random;

/**
 * Klasse mit zwei Hauptfunktionen:
 * Einerseits die zufällige Auswahl eines Wortes mit passender Schwierigkeit
 * und andererseits die Speicherung eines letters an einer Position.
 */
public class Items {


    // Arrays mit Wörtern:
    // 1. Test-Liste:
    private final String[] DEFAULT_TEST_WORDS = new String[] {
            "Java", "Info", "Code", "Einstein", "Physik"
    };

    // 2. Einfache Wörter
    private final String[] DEFAULT_EASY_WORDS = new String[] {
            "Algorithmus", "Bit", "Compiler", "Datenbank", "Energie", "Forschung", "Grafikkarte", "Hertz", "Information",
            "Java", "Konstanten", "Labor", "Prozessor", "Neutron", "Optik", "Programmierer", "Quanten",
            "Software", "Theorie", "Universität", "Veranstaltung", "Wissenschaft", "Xenon",
            "Yield", "Zustand", "Elektron", "Frequenz", "Gravitation", "Hardware", "Gauss"
    };

    // 3. Schwere Wörter:
    private final String[] DEFAULT_HARD_WORDS = new String[] {
            "Abstraktionsprinzip", "Binomialverteilung", "Chaosforschung", "Differentialgleichung", "Entropie",
            "Fermion", "Gravitationskonstante", "Hypertexttransferprotokoll", "Informatiktheorie", "Joule-Thomson-Effekt",
            "Künstliche-Intelligenz", "Lagrange-Multiplikator", "Multiprocessing", "NeuronalesNetzwerk",
            "Orthogonale-Transformation", "Parallelverarbeitung", "Quantenelektrodynamik", "Relativitätsprinzip",
            "Schrödinger-Gleichung", "Turingmaschine", "Wellenfunktion", "Relativitätstheorie",
            "XOR-Gatter", "Yukawa-Wechselwirkung", "Zustandsautomat", "Fibonacci-Folge",
            "Normalverteilung", "Aharonov-Bohm Effekt"
    };



    private double xpos;
    private double ypos;
    private String letter;



    public Items(String letter, double xpos, double ypos){
       this.letter = letter;
       this.xpos = xpos;
       this.ypos = ypos;
    }

    public Items() {
    }



    public String getLetter() {
        return letter;
    }

    public double getXPos() {
        return xpos;
    }

    public void setXPos(double xpos) {
        this.xpos = xpos;
    }

    public double getYPos() {
        return ypos;
    }

    public void setYPos(double ypos) {
        this.ypos = ypos;
    }



    /**
     * Methode, die zufälligen String aus DEFAULT_TEST_WORDS zurückgibt.
     * Die Methode wurde für Testzwecke implementiert und erfüllt keinen Zweck im Moment.
     */
    public String getWord() {
        Random random = new Random();
        return DEFAULT_TEST_WORDS[random.nextInt(DEFAULT_TEST_WORDS.length)];
    }

    /**
     * Methode, die zufälligen String aus DEFAULT_EASY_WORDS zurückgibt
     */
    public String getEasyWord() {
        Random random = new Random();
        return DEFAULT_EASY_WORDS[random.nextInt(DEFAULT_EASY_WORDS.length)];
    }

    /**
     * Methode, die zufälligen String aus DEFAULT_HARD_WORDS zurückgibt
     */
    public String getHardWord() {
        Random random = new Random();
        return DEFAULT_HARD_WORDS[random.nextInt(DEFAULT_HARD_WORDS.length)];
    }
}
