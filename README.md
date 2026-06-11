## Introduction
- This is an interactive command line Sudoku program written in Kotlin.

- The application supports puzzle generation, move validation, hints,
  rule checking and puzzle completion detection.

## Environment Requirements
JDK 21
Gradle
Command line terminal (Windows Command Prompt / Powershell / Bash)

## Instructions to Launch Application
### Windows Command Prompt / Powershell
.\gradlew.bat installDist
.\build\install\Sudoku\bin\sudoku.bat

### Bash
./gradlew installDist
./build/install/Sudoku/bin/sudoku

## Running Tests
### Windows Command Prompt / Powershell
.\gradlew.bat test

### Bash
./gradlew test

## Generate Windows Distributable Package
.\gradlew.bat distZip
Package path: build/distributions/Sudoku.zip

## Architectural Design
- This Sudoku application is designed around a clear separation of responsibilities 
  to keep the code simple, maintainable and testable.

- Test Driven Development (TDD) was used during development, with tests written
  before implementation where practical.

Components:

- SudokuBoard
    - Domain model representing the Sudoku grid and pre-filled cells.
  
- SudokuSolver
    - Service responsible for generating a valid Sudoku solution.
  
- SudokuGameController
    - Coordinates the game creation, user actions, validation, hints and game state.
  
- Main
    - Command line interface responsible for user interaction, input processing
      and board rendering.

## Assumptions
- SudokuBoard instances are treated as immutable. User actions create
  a new board state rather than modifying the existing board in place.

- The generated puzzle contains 30 pre-filled cells as required by the specification.

- Invalid user inputs are validated and rejected with the appropriate error messages.

- The application runs as a single user command line program.
