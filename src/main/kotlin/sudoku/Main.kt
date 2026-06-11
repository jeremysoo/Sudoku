package sudoku

import java.util.Scanner
import sudoku.controller.SudokuGameController
import sudoku.controller.MoveResult
import sudoku.model.SudokuBoard

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            println("Welcome to Sudoku!\n")

            val controller = SudokuGameController()
            val scanner = Scanner(System.`in`)

            controller.createNewGame()

            while (!controller.isGameOver()) {
                printGrid(controller.currentBoard)
                println("Enter command (e.g., A3 4, C5 clear, hint, check, quit):")
                val input = scanner.nextLine().trim()
                if (input.isEmpty()) continue

                when (input) {
                    "quit" -> {
                        println("Game Over!")
                        println("Press Enter to exit...")
                        scanner.nextLine()
                        return
                    }
                    "hint" -> {
                        println(controller.provideHint())
                    }
                    "check" -> {
                        println(controller.checkCurrentValidity())
                    }
                    else -> {
                        processGameplayCommand(input, controller)
                    }
                }
            }

            printGrid(controller.currentBoard)
            println("You have successfully completed the Sudoku puzzle!")
            println("Press Enter to exit...")
            scanner.nextLine()
        }

        private fun processGameplayCommand(input: String, controller: SudokuGameController) {
            val inputPair = input.split(" ")
            if (inputPair.size != 2) {
                println("Invalid command. Syntax error.")
                return
            }

            val (cellInput, actionInput) = inputPair
            if (cellInput.length != 2 || cellInput[0] !in 'A'..'I' || cellInput[1] !in '1'..'9') {
                println("Invalid cell input provided.")
                return
            }

            val row = cellInput[0] - 'A'
            val col = cellInput[1] - '1'

            val result = if (actionInput == "clear") {
                controller.clearCell(row, col, cellInput)
            } else {
                val numericValue = actionInput.toIntOrNull()
                if (numericValue == null) {
                    println("Invalid action input provided.")
                    return
                }
                controller.makeMove(row, col, numericValue, cellInput)
            }

            when (result) {
                is MoveResult.Success -> println(result.message)
                is MoveResult.Failure -> println(result.errorMessage)
            }
        }

        private fun printGrid(board: SudokuBoard) {
            println("Here is your puzzle:")
            println("    1 2 3 4 5 6 7 8 9")
            for (row in 0..8) {
                print("  ${('A' + row)} ")
                for (col in 0..8) {
                    val num = board.grid[row][col]
                    val symbol = if (num == 0) {
                        "_"
                    } else {
                        num.toString()
                    }
                    print("$symbol ")
                }
                println()
            }
            println()
        }
    }
}