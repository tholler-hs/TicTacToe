package de.hhn.tictactoe.view


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import de.hhn.tictactoe.model.GameModel
import de.hhn.tictactoe.model.Field
import de.hhn.tictactoe.model.Status



class TicTacToeViewModel : ViewModel() {


    var currentGame = mutableStateOf(GameModel())
    var gameField = mutableStateOf(Array(3) { Array(3) { Field() } })


    fun resetGame() {

        currentGame.value = GameModel()


        gameField.value = Array(3) { row ->
            Array(3) { column ->
                Field(indexRow = row, indexColumn = column)
            }
        }

    }


    fun selectField(field: Field) {

        if (field.status == Status.Empty && !currentGame.value.isGameEnding) {

            field.status = currentGame.value.currentPlayer
            gameField.value[field.indexRow][field.indexColumn] = field.copy()
            currentGame.value.currentPlayer = currentGame.value.currentPlayer.next()
            checkEndingGame()
            gameField.value[field.indexRow][field.indexColumn] = field.copy()
            gameField.value = Array(3) { row ->
                Array(3) { column ->
                    gameField.value[row][column].copy()
                }
            }
        }
    }

    private fun checkEndingGame() {

        for (i in 0 until 3) {
            if (checkLine(gameField.value[i][0], gameField.value[i][1], gameField.value[i][2]) ||
                checkLine(gameField.value[0][i], gameField.value[1][i], gameField.value[2][i])
            ) {
                return
            }
        }

        if (checkLine(gameField.value[0][0], gameField.value[1][1], gameField.value[2][2]) ||
            checkLine(gameField.value[0][2], gameField.value[1][1], gameField.value[2][0])
        ) {            return
        }


        if (gameField.value.all { row -> row.all { it.status != Status.Empty } }) {

            currentGame.value.isGameEnding = true
            currentGame.value.winningPlayer = Status.Empty
        }
    }


    private fun checkLine(vararg fields: Field): Boolean {

        if (fields.all { it.status != Status.Empty } && fields.all { it.status == fields[0].status }) {

            currentGame.value.isGameEnding = true
            currentGame.value.winningPlayer = fields[0].status
            return true
        }

        return false
    }
}
