# InfoProjekt Labyrinth
### Thorben ______ , Jara Hoffsommer
## Table of Contents
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

Mit den Tasten A,W,S,D bedient man die Spielfigur, die den Weg durch das Labyrinth sucht und das den Schwierigkeitsstufen entsprechende Ziel verfolgt. 
Zusätzlich kann mit dem Reset-Knopf das Labyrinth (und das Wort/ die Portale falls vorhanden)
neu und zufällig erstellt werden.

### Classes
####- Box Class:
  
####- GUI Class
 -> 

####- Maze Class
-> Mit der Move-Playerfunktion wird die Spielfigur mit den Tasten A, W, S, D bewegt. 
   Dabei wird geschaut ob wir uns auf einem Buchstaben befinden, falls ja wird dieser 
   eingesammelt
-> Mit der updateDisplayedWord Funktion wird das Label anhand der gesammelten Buchstaben auf den neuesten Stand gebracht
-> Die Methode reset setzt ein neues Labyrinth neu zusammen.
-> Die Methode addLetters verteilt die Buchstaben zufällig im Labyrinth

####- Player Class
  -> In dieser Klasse wird die Player-Figur verwaltet. Es wird auch getestet, ob der Player ein Item, also einen Buchstbaen berührt.

####- Items Class
  -> In dieser Klasse werden die Wörter verwaltet, die für die Schwierigkeitsstufen 0 und 1
  benötigt werden. Die Wörter werden in zwei verschiedenen Arrays gespeichert, um zwischen Schwereren (Längeren)
  und Leichteren (Kürzeren) Wörtern zu unterscheiden. 
