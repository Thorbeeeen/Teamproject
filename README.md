# InfoProjekt Labyrinth
#### von Thorben Pfeffer und Jara Hoffsommer
## Inhaltsverzeichnis
1. [General-Overview](#general-overview)
2. [Classes](#classes)

### General Overview
Wir haben ein Labyrinth-Spiel erstellt, bei dem 5 verschiedene Schwierigkeitsstufen 
eingestellt werden können:

- Difficulty 0 (Kurze Woerter):
  Das Ziel ist es, die Buchstaben eines kurzen Wortes zu sammeln.
- Difficulty 1(Lange Woerter):
   Das Ziel ist es, die Buchstaben eines kurzen Wortes zu sammeln.
- Difficulty 2 (Normal):
  Das Ziel ist, das Labyrinth zu durchlaufen.
- Difficulty 3 (Portal):
  Das Ziel ist, das Labyrinth zu durchlaufen. Dabei können Portale benutzt werden.
- Difficulty 4 (Nebel):
  Das Ziel ist, das Labyrinth zu durchlaufen. Dabei ist nur begrenzte Sichtbarkeit vorhanden.

Mit den Tasten W, A, S, D bedient man die Spielfigur, die den Weg durch das Labyrinth sucht und das den Schwierigkeitsstufen entsprechende Ziel verfolgt. 
Die Tasten N und M können benutzt werden um zu sprinten bzw. durch die Portale zu gehen.
Zusätzlich kann mit dem Reset-Knopf das Labyrinth (und das Wort / die Portale, falls vorhanden)
neu und zufällig erstellt werden.

### Classes
#### Box Class:
Repräsentiert eine Box des Labyrinths und speichert wichtige Eigenschafte. Damit kann eine Instanz der Klasse als Teil eines Graphen verwendet werden, der die Verbindungen der Boxen speichert.
  
#### GUI Class
Stellt eine Instanz von Maze durch den Aufruf von runMaze grafisch dar und gibt die Eingaben des Spielers an die Instanz von Maze weiter.

#### Maze Class
Hauptklasse unseres Projekts. Hier wird der Zustand des gesamten Labyrinths gespeichert und die Eingaben des Spielers bearbeiten.

Die wichtigsten Methoden werden im Folgenden beschrieben.

Mit der movePlayer Methode wird der Spieler bewegt, falls das durch die Wände zugelassen wird.
Dabei wird geschaut, ob wir uns auf einem Buchstaben befinden, falls das der Fall ist, wird dieser 
eingesammelt.
Mit der updateDisplayedWord Methode wird das Label anhand der gesammelten Buchstaben auf den neuesten Stand gebracht.
Die Methode reset setzt ein neues Labyrinth neu zusammen.
Die Methode teleportPlayer wird aufgerufen, wenn der Spieler versuch sich durch ein Portal zu begeben. 

#### Player Class
In dieser Klasse wird die Spielfigur verwaltet. Es wird auch getestet, ob die Spielfigur ein Item, also einen Buchstaben berührt. 
Hier werden auch viele Eigenschaften der Spielfigur gespeichert. 

#### Items Class
In dieser Klasse werden die Wörter verwaltet, die für die Schwierigkeitsstufen 0 und 1
benötigt werden. Die Wörter werden in zwei verschiedenen Arrays gespeichert, um zwischen Schwereren (Längeren)
und Leichteren (Kürzeren) Wörtern zu unterscheiden. 
