package sudoku.model

data class SudokuBoard (
    val grid: List<List<Int>> = List(9) { List(9) { 0 } },
    val isPreFilled: List<List<Boolean>> = List(9) { List(9) { false } }
)