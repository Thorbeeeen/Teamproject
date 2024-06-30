package TeamProject;

import java.awt.*;
import java.util.Random;

public class Items {
    private double xpos;
    private double ypos;
    private String letter;  // or path to image?

    // Konstruktoren
    public Items(String letter, double xpos, double ypos){
       this.letter = letter;
       this.xpos = xpos;
       this.ypos = ypos;
    } // bin nicht sicher ob wir das brauchen

    public Items(){
    }

    // Getter and Setter

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

    public String[] getWoerter() {return woerter;}

    public String[] getEasyWords() {return easyWords;}
    public String[] getHardWords() {return hardWords;}

//    public String getWort(String[] array) {
//        Random random = new Random();
//        return array[random.nextInt(array.length)];
//    } // allgemein: funktioniert?

    public String getWort() {
        Random random = new Random();
        return woerter[random.nextInt(woerter.length)];
    }
    public String getEasyWord() {
        Random random = new Random();
        return easyWords[random.nextInt(easyWords.length)];
    }
    public String getHardWord() {
        Random random = new Random();
        return hardWords[random.nextInt(hardWords.length)];
    }

    // Arrays mit Wörtern

    public String[] woerter = new String[]{"Java", "Info", "Code", "Einstein", "Physik",};
    // (Test-Liste)

    public String[] easyWords = new String[] {
            "Algorithmus", "Bit", "Compiler", "Datenbank", "Energie", "Forschung", "Grafikkarte", "Hertz", "Information",
            "Java", "Konstanten", "Labor", "Mikroprozessor", "Neutron", "Optik", "Programmierer", "Quantenphysik",
             "Software", "Theorie", "Universität", "Veranstaltung", "Wissenschaft", "Xenon",
            "Yield", "Zustandsraum", "Elektron", "Frequenz", "Gravitationswellen", "Hardware", "Gauss"
    }; // Einfache Woerter

    public String[] hardWords = new String[] {
            "Abstraktionsprinzip", "Binomialverteilung", "Chaosforschung", "Differentialgleichung", "Entropie",
            "Fermion", "Gravitationskonstante", "Hypertexttransferprotokoll", "Informatiktheorie", "Joule-Thomson-Effekt",
            "KünstlicheIntelligenz", "Lagrange-Multiplikator", "Multiprocessing", "NeuronalesNetzwerk",
            "OrthogonaleTransformation", "Parallelverarbeitung", "Quantenelektrodynamik", "Relativitätsprinzip",
            "Schrödinger-Gleichung", "Turingmaschine", "Wellenfunktion", "Relativitätstheorie",
            "XOR-Gatter", "Yukawa-Wechselwirkung", "Zustandsautomat", "Elektronenspinresonanz", "Fibonacci-Folge",
            "Normalverteilung", "Brachthäuser", "Pons-Moll"
    }; // Schwere Woerter



}
