## Introduction
This is an interactive command line Sudoku program written in Kotlin.

## Environment Requirements
JDK 21
CLI Terminal eg. Windows Command Line / Powershell / Bash

## Instructions to Launch Application
# Windows Command Line / Powershell
.\gradlew.bat installDist
.\build\install\Sudoku\bin\sudoku.bat

### Bash
./gradlew installDist
./build/install/Sudoku/bin/sudoku

## Generate Windows Distributable Package
.\gradlew.bat distZip
Package path: build/distributions/Sudoku.zip

## Architectural Design
This Sudoku application is designed around a clear separation of logic to keep code simple and highly testable.
Test Driven Development (TDD) was used during the development where the tests were first written before the implementation.
The codebase decouples the presentation loop from the core game logic to maximize simplicity, stability, and testability.

SudokuBoard defines the domain data model containing the board grid and pre-filled cells for the puzzle.
SudokuSolver is the service layer containing the logic to fill the board.
SudokuGameController as the controller layer coordinates the game states of puzzle creation, user actions and validations.
Main is the command line interface layer which manages the console terminal user input and output rendering and command routing.

## Assumptions
An immutable board state is assumed, such that the board can never be changed once it is created.
User actions such as cell clearing creates a distinct board copy to ensure memory safety 
and prevents unpredictable bugs during active gameplay.

User inputs are also assumed to be formatted correctly and these alphanumeric characters for the cell coordinates 
and user actions are validated and invalid inputs provided will be rejected.   
