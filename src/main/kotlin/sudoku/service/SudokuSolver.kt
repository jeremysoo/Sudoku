package sudoku.service

import sudoku.model.SudokuBoard

class SudokuSolver {
    fun solve(board: SudokuBoard): SudokuBoard? {
        if (!isValidBoard(board.grid)) {
            return null
        }

        val emptyCells = mutableListOf<Pair<Int, Int>>()
        for (row in 0..8) {
            for (col in 0..8) {
                if (board.grid[row][col] == 0) emptyCells.add(Pair(row, col))
            }
        }
        if (emptyCells.isEmpty()) return board

        val workingGrid = board.grid.map { it.toMutableList() }.toMutableList()

        var cellIndex = 0
        while (cellIndex < emptyCells.size) {
            val (row, col) = emptyCells[cellIndex]
            val currentVal = workingGrid[row][col]

            var foundValidValue = false
            for (num in (currentVal + 1)..9) {
                if (isValidPlacement(workingGrid, row, col, num)) {
                    workingGrid[row][col] = num
                    foundValidValue = true
                    break
                }
            }

            if (foundValidValue) {
                cellIndex++
            } else {
                workingGrid[row][col] = 0
                cellIndex--
                if (cellIndex < 0) {
                    return null
                }
            }
        }

        val finalGrid = workingGrid.map { it.toList() }
        return SudokuBoard(finalGrid, board.isPreFilled)
    }

    private fun isValidBoard(grid: List<List<Int>>): Boolean {
        for (row in 0..8) {
            for (col in 0..8) {
                val num = grid[row][col]
                if (num != 0) {
                    if (!isValidPlacement(grid, row, col, num)) {
                        return false
                    }
                }
            }
        }
        return true
    }

    private fun isValidPlacement(
        grid: List<List<Int>>,
        row: Int,
        col: Int,
        num: Int
    ): Boolean {
        return isRowValid(grid, row, col, num) &&
                isColumnValid(grid, row, col, num) &&
                isSubGridValid(grid, row, col, num)
    }

    private fun isRowValid(grid: List<List<Int>>, row: Int, col: Int, num: Int): Boolean {
        for (currentCol in 0..8) {
            if (currentCol == col) continue
            if (grid[row][currentCol] == num) return false
        }
        return true
    }

    private fun isColumnValid(grid: List<List<Int>>, row: Int, col: Int, num: Int): Boolean {
        for (currentRow in 0..8) {
            if (currentRow == row) continue
            if (grid[currentRow][col] == num) return false
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