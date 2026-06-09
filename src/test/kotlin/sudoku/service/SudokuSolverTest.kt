package sudoku.service

import org.junit.jupiter.api.Test
import sudoku.model.SudokuBoard
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SudokuSolverTest {
    private fun sampleGrid(): List<List<Int>> {
        return listOf(
            listOf(5, 3, 0, 0, 7, 0, 0, 0, 0),
            listOf(6, 0, 0, 1, 9, 5, 0, 0, 0),
            listOf(0, 9, 8, 0, 0, 0, 0, 6, 0),
            listOf(8, 0, 0, 0, 6, 0, 0, 0, 3),
            listOf(4, 0, 0, 8, 0, 3, 0, 0, 1),
            listOf(7, 0, 0, 0, 2, 0, 0, 0, 6),
            listOf(0, 6, 0, 0, 0, 0, 2, 8, 0),
            listOf(0, 0, 0, 4, 1, 9, 0, 0, 5),
            listOf(0, 0, 0, 0, 8, 0, 0, 7, 9)
        )
    }

    @Test
    fun `solve Sudoku puzzle from empty board`() {
        val sudokuSolver = SudokuSolver()
        val emptyBoard = SudokuBoard()

        val solvedBoard = sudokuSolver.solve(emptyBoard)

        assertNotNull(solvedBoard)
        assertValidBoard(solvedBoard)
    }

    @Test
    fun `solve Sudoku puzzle from sample board`() {
        val sudokuSolver = SudokuSolver()
        val validBoard = SudokuBoard(sampleGrid())

        val solvedBoard = sudokuSolver.solve(validBoard)

        assertNotNull(solvedBoard)
        assertValidBoard(solvedBoard)
    }

    @Test
    fun `return null for a puzzle unable to be solved`() {
        val sudokuSolver = SudokuSolver()
        val validBoard = SudokuBoard(sampleGrid())
        val invalidBoard = validBoard.copy(
            grid = validBoard.grid.toMutableList().apply {
                this[0] = this[0].toMutableList().apply {
                    this[2] = 3
                }
            }
        )

        val solvedBoard = sudokuSolver.solve(invalidBoard)

        assertNull(solvedBoard)
    }

    private fun assertValidBoard(board: SudokuBoard) {
        val grid = board.grid
        checkRows(grid)
        checkColumns(grid)
        checkSubGrids(grid)
    }

    private fun checkRows(grid: List<List<Int>>) {
        for (row in 0..8) {
            assertTrue(checkDuplicateNumbers(grid[row]))
        }
    }

    private fun checkColumns(grid: List<List<Int>>) {
        for (col in 0..8) {
            val colValues = grid.map { row ->
                row[col]
            }
            assertTrue(checkDuplicateNumbers(colValues))
        }
    }

    private fun checkSubGrids(grid: List<List<Int>>) {
        for (boxRow in 0..8 step 3) {
            for (boxCol in 0..8 step 3) {
                val subGridValues = mutableListOf<Int>()
                for (i in 0..2) {
                    for (j in 0..2) {
                        val num = grid[boxRow + i][boxCol + j]
                        subGridValues.add(num)
                    }
                }
                assertTrue(checkDuplicateNumbers(subGridValues))
            }
        }
    }

    private fun checkDuplicateNumbers(list: List<Int>): Boolean {
        val seen = BooleanArray(10)
        for (num in list) {
            if (num !in 1..9) {
                return false
            }

            if (seen[num]) {
                return false
            }
            seen[num] = true
        }
        return true
    }
}