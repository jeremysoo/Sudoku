package sudoku.controller

import kotlin.random.Random
import sudoku.model.SudokuBoard
import sudoku.service.SudokuSolver

sealed class MoveResult {
    data class Success(val message: String = "Move accepted.") : MoveResult()
    data class Failure(val errorMessage: String) : MoveResult()
}

class SudokuGameController(private val solver: SudokuSolver = SudokuSolver()) {
    var currentBoard = SudokuBoard()
    var solutionBoard = SudokuBoard()

    fun createNewGame() {
        val filledGrid = Array(9) { IntArray(9) { 0 } }
        for (i in 0 until 9 step 3) {
            val numbers = (1..9).shuffled()
            var currentIndex = 0
            for (r in 0..2) {
                for (c in 0..2) { filledGrid[i + r][i + c] = numbers[currentIndex++] }
            }
        }

        val originalBoard = SudokuBoard(grid = filledGrid.map { it.toList() })
        solutionBoard = solver.solve(originalBoard)
            ?: throw IllegalStateException("Unable to solve original board.")

        var emptyCellsCreated = 0
        val workingGrid = solutionBoard.grid.map { it.toMutableList() }.toMutableList()
        val random = Random.Default

        while (emptyCellsCreated < 51) {
            val r = random.nextInt(9)
            val c = random.nextInt(9)
            if (workingGrid[r][c] != 0) {
                workingGrid[r][c] = 0
                emptyCellsCreated++
            }
        }

        val finalGrid = workingGrid.map { it.toList() }
        val mask = mutableListOf<List<Boolean>>()
        for (r in 0..8) {
            val maskRow = mutableListOf<Boolean>()
            for (c in 0..8) {
                maskRow.add(finalGrid[r][c] != 0)
            }
            mask.add(maskRow.toList())
        }

        currentBoard = SudokuBoard(finalGrid, mask)
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