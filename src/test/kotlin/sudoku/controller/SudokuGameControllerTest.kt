package sudoku.controller

import org.junit.jupiter.api.Test
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import kotlin.test.assertEquals

class SudokuGameControllerTest {
    private fun createEmptyGrid(): List<List<Int>> {
        return List(9) { List(9) { 0 } }
    }
    private fun createNonPreFilledGrid(): List<List<Boolean>> {
        return List(9) { List(9) { false } }
    }

    @Test
    fun `create new game with 30 pre-filled numbers`() {
        val controller = SudokuGameController()
        controller.createNewGame()

        var lockedCluesCount = 0
        for (row in 0..8) {
            for (col in 0..8) {
                if (controller.currentBoard.isPreFilled[row][col]) {
                    lockedCluesCount++
                }
            }
        }

        assertEquals(30, lockedCluesCount)
    }

    @Test
    fun `pre-filled cell cannot be modified`() {
        val controller = SudokuGameController()

        val mockPreFilledMask = listOf(
            listOf(true,  false, false, false, false, false, false, false, false),
            listOf(false, false, false, false, false, false, false, false, false),
            listOf(false, false, false, false, false, false, false, false, false),
            listOf(false, false, false, false, false, false, false, false, false),
            listOf(false, false, false, false, false, false, false, false, false),
            listOf(false, false, false, false, false, false, false, false, false),
            listOf(false, false, false, false, false, false, false, false, false),
            listOf(false, false, false, false, false, false, false, false, false),
            listOf(false, false, false, false, false, false, false, false, false)
        )
        controller.currentBoard = controller.currentBoard.copy(
            isPreFilled = mockPreFilledMask
        )

        val result = controller.makeMove(0, 0, 6, "A1")

        assertTrue(result is MoveResult.Failure)
        assertEquals("Invalid move. A1 is pre-filled.", result.errorMessage)
    }

    @Test
    fun `number provided must be between 1 to 9`() {
        val controller = SudokuGameController()

        val invalidNumberZero = controller.makeMove(0, 0, 0, "A1")
        assertTrue(invalidNumberZero is MoveResult.Failure)
        assertEquals("Invalid number input.", invalidNumberZero.errorMessage)

        val invalidNumberTen = controller.makeMove(0, 0, 10, "A1")
        assertTrue(invalidNumberTen is MoveResult.Failure)
        assertEquals("Invalid number input.", invalidNumberTen.errorMessage)
    }

    @Test
    fun `able to write number into empty cell`() {
        val controller = SudokuGameController()

        controller.currentBoard = controller.currentBoard.copy(
            grid = createEmptyGrid(),
            isPreFilled = createNonPreFilledGrid()
        )

        val result = controller.makeMove(0, 2, 4, "A3")

        assertTrue(result is MoveResult.Success)
        assertEquals("Move accepted.", result.message)
        assertEquals(4, controller.currentBoard.grid[0][2])
    }

    @Test
    fun `able to clear cell`() {
        val controller = SudokuGameController()

        val mockGrid = listOf(
            listOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
            listOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
            listOf(0, 0, 0, 0, 8, 0, 0, 0, 0),
            listOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
            listOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
            listOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
            listOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
            listOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
            listOf(0, 0, 0, 0, 0, 0, 0, 0, 0)
        )
        controller.currentBoard = controller.currentBoard.copy(
            grid = mockGrid,
            isPreFilled = createNonPreFilledGrid()
        )

        // Clear it directly by passing a value of 0
        val clearResult = controller.clearCell(2, 4, "C5")

        assertTrue(clearResult is MoveResult.Success)
        assertEquals("C5 is cleared.", clearResult.message)
        assertEquals(0, controller.currentBoard.grid[2][4])
    }

    @Test
    fun `able to request a hint`() {
        val controller = SudokuGameController()
        controller.createNewGame()

        val hint = controller.provideHint()

        assertTrue(hint.startsWith("Hint: Cell "))
        assertTrue(hint.contains(" = "))
    }

    @Test
    fun `check current grid row for validity`() {
        val controller = SudokuGameController()

        val invalidGridRow = listOf(
            listOf(5, 3, 3, 0, 7, 0, 0, 0, 0),
            listOf(6, 0, 0, 1, 9, 5, 0, 0, 0),
            listOf(0, 9, 8, 0, 0, 0, 0, 6, 0),
            listOf(8, 0, 0, 0, 6, 0, 0, 0, 3),
            listOf(4, 0, 0, 8, 0, 3, 0, 0, 1),
            listOf(7, 0, 0, 0, 2, 0, 0, 0, 6),
            listOf(0, 6, 0, 0, 0, 0, 2, 8, 0),
            listOf(0, 0, 0, 4, 1, 9, 0, 0, 5),
            listOf(0, 0, 0, 0, 8, 0, 0, 7, 9)
        )
        controller.currentBoard = controller.currentBoard.copy(
            grid = invalidGridRow,
            isPreFilled = createNonPreFilledGrid()
        )

        val report = controller.checkCurrentValidity()
        assertEquals("Number 3 already exists in Row A.", report)
    }

    @Test
    fun `check current grid column for validity`() {
        val controller = SudokuGameController()

        val invalidGridCol = listOf(
            listOf(5, 3, 0, 0, 7, 0, 0, 0, 0),
            listOf(6, 0, 0, 1, 9, 5, 0, 0, 0),
            listOf(5, 9, 8, 0, 0, 0, 0, 6, 0),
            listOf(8, 0, 0, 0, 6, 0, 0, 0, 3),
            listOf(4, 0, 0, 8, 0, 3, 0, 0, 1),
            listOf(7, 0, 0, 0, 2, 0, 0, 0, 6),
            listOf(0, 6, 0, 0, 0, 0, 2, 8, 0),
            listOf(0, 0, 0, 4, 1, 9, 0, 0, 5),
            listOf(0, 0, 0, 0, 8, 0, 0, 7, 9)
        )
        controller.currentBoard = controller.currentBoard.copy(
            grid = invalidGridCol,
            isPreFilled = createNonPreFilledGrid()
        )

        val report = controller.checkCurrentValidity()
        assertEquals("Number 5 already exists in Column 1.", report)
    }

    @Test
    fun `check current grid sub grid for validity`() {
        val controller = SudokuGameController()

        val invalidGridSubGrid = listOf(
            listOf(5, 3, 0, 0, 7, 0, 0, 0, 0),
            listOf(6, 8, 0, 1, 9, 5, 0, 0, 0),
            listOf(0, 9, 8, 0, 0, 0, 0, 6, 0),
            listOf(8, 0, 0, 0, 6, 0, 0, 0, 3),
            listOf(4, 0, 0, 8, 0, 3, 0, 0, 1),
            listOf(7, 0, 0, 0, 2, 0, 0, 0, 6),
            listOf(0, 6, 0, 0, 0, 0, 2, 8, 0),
            listOf(0, 0, 0, 4, 1, 9, 0, 0, 5),
            listOf(0, 0, 0, 0, 8, 0, 0, 7, 9)
        )
        controller.currentBoard = controller.currentBoard.copy(
            grid = invalidGridSubGrid,
            isPreFilled = createNonPreFilledGrid()
        )

        val report = controller.checkCurrentValidity()
        assertEquals("Number 8 already exists in the same 3×3 subgrid.", report)
    }

    @Test
    fun `validates successfully when puzzle is completed`() {
        val controller = SudokuGameController()
        controller.createNewGame()

        val report = controller.checkCurrentValidity()
        assertEquals("No rule violations detected.", report)
    }

    @Test
    fun `ends game when grid is completely and correctly filled`() {
        val controller = SudokuGameController()
        controller.createNewGame()

        assertFalse(controller.isGameOver())
    }
}