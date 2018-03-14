# Battleship
---
## Summary
This is a web-based multiplayer implementation of the game Battleship.

## Stack/Architecture
Frontend: Plain JS/HTML/CSS with jQuery

Backend: Java, Spring Boot, JPAA/Hibernate, and Postgres

## Project Layout
Frontend code can be found in the [resources/static](src/main/resources/static) folder. Backend routes are defined in the [controller](src/main/java/emma/battleship/controller) folder and schema definitions are in the [model](src/main/java/emma/battleship/model) folder.

![placing_ships](placing_ships.png)
Players can click beginning and end points of ships to place them.

![mid_game](mid_game_their_turn.png)
Players take turns making moves. The player board lights up with your opponent's correct and incorrect hits, and the attack board can be clicked to make moves.

## Future Work
This project was implemented over the course of 3 days, and was structured around having a simple frontend.

If I were to continue working on it, I would expand the schema by creating a player model in order to allow players to save progress in the game. I would also alter the game schema in order to save the board more efficiently.
