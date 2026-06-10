package sudoku.controller

import sudoku.model.SudokuBoard
import sudoku.service.SudokuSolver

sealed class MoveResult {
    data class Success(val message: String = "Move accepted.") : MoveResult()
    data class Failure(val errorMessage: String) : MoveResult()
}

private const val BOARD_ROWS = 9
private const val BOARD_COLS = 9
private const val PREFILLED_CELLS = 30
private const val EMPTY_CELLS = BOARD_ROWS * BOARD_COLS - PREFILLED_CELLS

class SudokuGameController(private val solver: SudokuSolver = SudokuSolver()) {
    var currentBoard = SudokuBoard()
    var solutionBoard = SudokuBoard()

    fun createNewGame() {
        solutionBoard = solver.solve(SudokuBoard()) ?: throw IllegalStateException("Failed to generate Sudoku solution")

        val puzzleGrid = solutionBoard.grid.map {
            it.toMutableList()
        }.toMutableList()

        val preFilledCells = MutableList(9) {
            MutableList(9) { true }
        }

        var emptyCellsCreated = 0

        while (emptyCellsCreated < EMPTY_CELLS) {
            val row = (0..8).random()
            val col = (0..8).random()

            if (puzzleGrid[row][col] != 0) {
                puzzleGrid[row][col] = 0
                preFilledCells[row][col] = false
                emptyCellsCreated++
            }
        }

        currentBoard = SudokuBoard(
            grid = puzzleGrid.map { it.toList() },
            isPreFilled = preFilledCells.map { it.toList() }
        )
    }

    fun makeMove(row: Int, col: Int, value: Int, cellName: String): MoveResult {
        if (value !in 1..9) {
            return MoveResult.Failure("Invalid number input.")
        }
        if (currentBoard.isPreFilled[row][col]) {
            return MoveResult.Failure("Invalid move. $cellName is pre-filled.")
        }

        val workingGrid = currentBoard.grid.map { it.toMutableList() }.toMutableList()
        workingGrid[row][col] = value

        currentBoard = currentBoard.copy(grid = workingGrid.map { it.toList() })
        return MoveResult.Success("Move accepted.")
    }

    fun clearCell(row: Int, col: Int, cellName: String): MoveResult {
        if (currentBoard.isPreFilled[row][col]) {
            return MoveResult.Failure("Invalid move. $cellName is pre-filled.")
        }

        val workingGrid = currentBoard.grid.map { it.toMutableList() }.toMutableList()
        workingGrid[row][col] = 0

        currentBoard = currentBoard.copy(grid = workingGrid.map { it.toList() })
        return MoveResult.Success("$cellName is cleared.")
    }

    fun checkCurrentValidity(): String {
        val grid = currentBoard.grid

        for (row in 0..8) {
            val seen = IntArray(10)
            for (col in 0..8) {
                val num = grid[row][col]
                if (num != 0) {
                    if (seen[num] > 0) {
                        return "Number $num already exists in Row ${('A' + row)}."
                    }
                    seen[num]++
                }
            }
        }

        for (col in 0..8) {
            val seen = IntArray(10)
            for (row in 0..8) {
                val num = grid[row][col]
                if (num != 0) {
                    if (seen[num] > 0) {
                        return "Number $num already exists in Column ${col + 1}."
                    }
                    seen[num]++
                }
            }
        }

        for (boxRow in 0..8 step 3) {
            for (boxCol in 0..8 step 3) {
                val seen = IntArray(10)
                for (i in 0..2) {
                    for (j in 0..2) {
                        val num = grid[boxRow + i][boxCol + j]
                        if (num != 0) {
                            if (seen[num] > 0) {
                                return "Number $num already exists in the same 3×3 subgrid."
                            }
                            seen[num]++
                        }
                    }
                }
            }
        }

        return "No rule violations detected."
    }

    fun provideHint(): String {
        val targets = mutableListOf<Pair<Int, Int>>()
        for (row in 0..8) {
            for (col in 0..8) {
                if (!currentBoard.isPreFilled[row][col] && currentBoard.grid[row][col] != solutionBoard.grid[row][col]) {
                    targets.add(Pair(row, col))
                }
            }
        }

        if (targets.isEmpty()) {
            return "No hints available."
        }

        val (row, col) = targets.random()
        val correctValue = solutionBoard.grid[row][col]
        val rowChar = ('A' + row)
        val colChar = (col + 1)

        return "Hint: Cell $rowChar$colChar = $correctValue"
    }

    fun isGameOver(): Boolean {
        return currentBoard.grid == solutionBoard.grid
    }
}