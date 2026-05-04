# Treasure Hunt AI Game (Java)

## Overview

Java client application for a turn-based strategy game where two autonomous agents explore a shared map, locate objectives, and compete to win.

The client communicates with a central game server via REST API and makes decisions based on map state using pathfinding and exploration strategies under partial information.

## Preview

<img src="images/ui-screenshot.jpg" width="800"/>

## Game Model

Two autonomous clients compete on a shared map:
- Each client generates part of the map and exchanges it with the server
- The agent must locate its treasure and then capture the opponent’s castle
- The game is turn-based and state-driven

## Systems

#### Game Logic
- Turn-based gameplay and state progression
- Decision-making based on current map state and found objectives

#### Client–Server Interaction
- REST communication using Spring WebClient
- Manages game sessions, actions, and server responses

#### Map Generation
- Procedural generation with terrain constraints
- Flood-fill validation to ensure connectivity

#### AI
- Pathfinding using Dijkstra’s algorithm
- Exploration strategy prioritizing regions with high information gain

## Tech

- Java 17
- Spring WebClient
- JUnit 5, Mockito
- SLF4J, Logback
