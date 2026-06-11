package sudoku.service

import sudoku.model.SudokuBoard

class SudokuSolver {
    fun solve(board: SudokuBoard): SudokuBoard? {
        val workingGrid = board.grid.map {
            it.toMutableList()
        }.toMutableList()

        return if (!solveGrid(workingGrid)) {
            null
        } else {
            board.copy(
                grid = workingGrid.map { it.toList() }
            )
        }
    }

    private fun solveGrid(grid: MutableList<MutableList<Int>>): Boolean {
        val emptyCell = findEmptyCell(grid) ?: return true

        val (row, col) = emptyCell

        for (num in (1..9).shuffled()) {
            if (isValidPlacement(grid, row, col, num)) {
                grid[row][col] = num

                if (solveGrid(grid)) {
                    return true
                }

                grid[row][col] = 0
            }
        }

        return false
    }

    private fun findEmptyCell(
        grid: List<List<Int>>
    ): Pair<Int, Int>? {
        for (row in 0..8) {
            for (col in 0..8) {
                if (grid[row][col] == 0) {
                    return Pair(row, col)
                }
            }
        }

        return null
    }

    private fun isValidPlacement(
        grid: List<List<Int>>,
        row: Int,
        col: Int,
        num: Int
    ): Boolean {
        return isRowValid(grid, row, num) &&
                isColumnValid(grid, col, num) &&
                isSubGridValid(grid, row, col, num)
    }

    private fun isRowValid(grid: List<List<Int>>, row: Int, num: Int): Boolean {
        for (currentCol in 0..8) {
            if (grid[row][currentCol] == num) {
                return false
            }
        }
        return true
    }

    private fun isColumnValid(grid: List<List<Int>>, col: Int, num: Int): Boolean {
        for (currentRow in 0..8) {
            if (grid[currentRow][col] == num) {
                return false
            }
        }
        return true
    }

    private fun isSubGridValid(grid: List<List<Int>>, row: Int, col: Int, num: Int): Boolean {
        val startRow = row - row % 3
        val startCol = col - col % 3
        for (i in 0..2) {
            for (j in 0..2) {
                val targetRow = startRow + i
                val targetCol = startCol + j
                if (targetRow == row && targetCol == col) continue
                if (grid[targetRow][targetCol] == num) return false
            }
        }
        return true
    }
}