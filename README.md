# Uno Game - COMP2033 Project 1

**Author:** Andrew Nell
**Student ID:** 110450836
**Email ID:** NELAY007

## Project Overview

This project is an implementation of the card game Uno in Java. It demonstrates core object-oriented programming principles, effective use of Java data structures, and a clear separation of concerns through design patterns like Strategy. The game supports 2-4 players, including one human player and 1-3 basic AI players.

The primary reference text for design justifications and data structure theory is "Data Structures: Abstraction and Design Using Java" by Koffman and Wolfgang (Third Edition).

## AI Tools Used

*   **GitHub Copilot:** Utilized for assistance with boilerplate code generation (e.g., constructors, getters, setters), Javadoc comment completion, and suggesting standard implementations for `equals()` and `hashCode()` methods.
*   **ChatGPT:** Consulted for documentation formatting assistance and refining explanatory text (primarily for README and comments, not core logic).

**Note:** The core architectural design, class hierarchy decisions, choice of data structures, sorting strategy, game-specific logic, and algorithm implementations were human-developed.

## Core Features Implemented

*   **Card Representation:** Abstract `Card` class with concrete subclasses `NumberCard`, `ActionCard`, and `WildCard`.
*   **Game Components:**
    *   `Deck`: Manages the draw pile, including initialization and shuffling.
    *   `Pile`: Manages the discard pile.
    *   `Hand`: Represents a player's hand, kept sorted according to card natural order.
*   **Player Management:**
    *   `Player` class: Represents a player with a name, hand, and strategy.
    *   `PlayerStrategy` interface: Defines how a player makes decisions.
    *   `HumanStrategy`: Implements `PlayerStrategy` for human input via console.
    *   `BasicAIStrategy`: Implements `PlayerStrategy` with simple AI logic.
*   **Game Flow:**
    *   `UnoGame` class: Orchestrates the game, manages turns, applies card effects, and checks for win conditions.
    *   Supports 2-4 players.
    *   Correctly handles special card effects (Skip, Reverse, Draw Two, Wild, Wild Draw Four), including special 2-player Reverse rule.
    *   Manages deck reshuffling when the draw pile is empty.
*   **Sorting:** Player hands are maintained in a sorted order using `Collections.sort()` which relies on the `Card` class implementing `Comparable`.
*   **Input/Output:** Console-based interaction for the human player and game state display.
*   **Seedable Randomness:** The deck shuffle can be seeded for reproducible game scenarios, aiding in testing and debugging.

## Design Justifications (Highlights)

*   **Object-Oriented Principles:**
    *   **Abstraction:** `Card` class abstracts common card features.
    *   **Inheritance:** `NumberCard`, `ActionCard`, `WildCard` inherit from `Card`.
    *   **Polymorphism:** `applyEffect()` method in `Card` subclasses is called polymorphically by the `UnoGame` controller.
    *   **Encapsulation:** Data fields are generally private, accessed via public methods.
*   **Data Structures (Referencing Koffman & Wolfgang - K&W):**
    *   **`Deck` (`ArrayList<Card>`):** Chosen for efficient card drawing from the end (amortized O(1)) and effective shuffling with `Collections.shuffle()` (K&W, Ch 2.2, 2.4).
    *   **`Pile` (`ArrayDeque<Card>`):** Used for LIFO (Last-In, First-Out) operations typical of a discard pile; `addFirst()` and `peekFirst()` are O(1) (K&W, Ch 4.8).
    *   **`Hand` (`ArrayList<Card>`):** Supports dynamic resizing and efficient iteration. Sorting is explicitly handled using `Collections.sort()` after additions.
        *   **Sorting Strategy:** Relies on `Card implements Comparable` and Java's Timsort (O(N log N) via `Collections.sort()`) for optimal performance and clarity (K&W, Ch 8.1, 8.7). `TreeSet` was rejected due to its inability to store duplicate cards, common in Uno.
    *   **`BasicAIStrategy` (`HashMap<Color, Integer>`):** Efficiently counts card colors (O(1) average for `put`/`getOrDefault`) for Wild color selection (K&W, Ch 7.2, 7.3).
*   **Strategy Pattern (`PlayerStrategy`):** Decouples player decision-making logic from the `Player` class, allowing for flexible addition of new AI or UI strategies (K&W, Ch 1.1, 1.8).

## File Structure

*   `src/projectone/`
    *   `ActionCard.java`
    *   `BasicAIStrategy.java`
    *   `Card.java`
    *   `Color.java` (enum)
    *   `Deck.java`
    *   `Hand.java`
    *   `HumanStrategy.java`
    *   `NumberCard.java`
    *   `Pile.java`
    *   `Player.java`
    *   `PlayerStrategy.java` (interface)
    *   `UnoGame.java` (contains `main` method)
    *   `Value.java` (enum)
    *   `WildCard.java`

## How to Compile and Run

1.  **Prerequisites:** Java Development Kit (JDK) 8 or higher installed.
2.  **Compilation:**
    Navigate to the `src` directory in your terminal.
    Compile all Java files:
    ```bash
    javac projectone/*.java
    ```
3.  **Running the Game:**
    From the `src` directory, run the `UnoGame` class:
    ```bash
    java projectone.UnoGame
    ```
    The game will prompt for the number of players, player names, and an optional seed for the deck shuffle.

## Testing Strategy

A comprehensive testing strategy would typically involve JUnit tests for individual class functionalities and their interactions. Key areas for testing include:

*   `Card` logic: `canPlayOn`, `compareTo`, `equals`, `hashCode`, and polymorphic `applyEffect`.
*   `Deck` operations: Initialization, shuffle (with fixed seeds for determinism), draw, isEmpty.
*   `Pile` operations: `addCard`, `getTopCard`, `takeCardsForNewDeck`.
*   `Hand` operations: `addCard`, `removeCard`, `findValidCards`, and ensuring sorted order is maintained.
*   `Player` strategy delegation.
*   `UnoGame` core mechanics: Initial deal, turn progression, special card effects, win conditions, deck reshuffling.
