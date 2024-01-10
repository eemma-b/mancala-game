# Mancala Game

This is a 2-player Mancala game with a graphical user interface (GUI) that supports two rule sets: the Kalah rule set and the Ayoayo rule set. Even while games are in progress, users can quit an unfinished game, start a new one, or exit the application. The application allows you to save an ongoing game to a file and load a previously saved game file. It also tracks each user's game activity, including wins, losses, and games played. Players have the ability to save and load their personal user profile files. 

## Description

Both rule sets start with four pieces in each pit and end when one side is empty.   

In Kalah, players distribute pieces counter-clockwise from any of their pits. A free turn is granted if the last piece lands in the player's store, and a capture occurs if it lands in an empty pit on their side.  

In Ayoayo, the starting pit is excluded during seed sowing. Stones are redistributed from pits with remaining stones after a turn, until the last seed falls into an empty pit. Captures occur when the last stone lands in an empty pit on the player's side and the opposite pit contains stones.  

## Getting Started

### Dependencies

This application should run on any operating systems that has the JVM.  This Mancala game depends on a package named mancala and ui to run the game.

### Executing program

How to build and run the program:
- Gradle build to create jar file
- Go to your computer terminal and cd to where the jar file is, it is named "GraphicalUI.jar"
- To run the program use the jar file: java -jar GraphicalUI.jar

Expected output:  
- The GUI, you should first be prompted with a message asking which rule set you want to play for your Mancala Game

## Acknowledgments

Inspiration, code snippets, etc.
* [awesome-readme](https://github.com/matiassingers/awesome-readme)
* [simple-readme] (https://gist.githubusercontent.com/DomPizzie/7a5ff55ffa9081f2de27c315f5018afc/raw/d59043abbb123089ad6602aba571121b71d91d7f/README-Template.md)