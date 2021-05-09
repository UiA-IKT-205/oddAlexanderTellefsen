package no.uia.ikt205.knotsandcrosses

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import no.uia.ikt205.knotsandcrosses.api.data.Game
import no.uia.ikt205.knotsandcrosses.api.data.GameState
import no.uia.ikt205.knotsandcrosses.databinding.ActivityGameBinding
import no.uia.ikt205.thgame.GameManager

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding
    var turn: Boolean = false
    var playerTag: Char = '\u0000'
    var opponentTag: Char = '\u0000'

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var game: Game? = intent.getParcelableExtra("game")

        //Init game
        game?.let { loadState(it.state) }
        game?.let { loadPlayers(it) }
        setButtonListeners(game)

        val mainHandler = Handler(Looper.getMainLooper())

        //Poll game, and update if changed
        mainHandler.post(object : Runnable {
            override fun run() {
                GameManager.pollGame(game?.gameId.toString()) { newGame: Game? ->
                    if (game?.players != newGame?.players && newGame != null)
                        with(binding) {
                            playerOneTv.setText(newGame.players[0] + "\n(X)")
                            playerTwoTv.setText(newGame.players[1] + "\n(O)")
                            gameStatusTv.setText(getString(R.string.opponentMove))
                        }


                    if (game?.state != newGame?.state && newGame != null) {
                        game = newGame
                        game?.let { loadState(it.state) }
                        setButtonListeners(game)
                        binding.gameStatusTv.setText(getString(R.string.yourMove))
                        turn = true
                    }

                }
                mainHandler.postDelayed(this, 1000)
            }
        })
    }

    private fun setButtonListeners(game: Game?) {
        binding.row0Col0.setOnClickListener { makeMove(game, 0, 0) }
        binding.row0col1.setOnClickListener { makeMove(game, 0, 1) }
        binding.row0col2.setOnClickListener { makeMove(game, 0, 2) }
        binding.row1col0.setOnClickListener { makeMove(game, 1, 0) }
        binding.row1col1.setOnClickListener { makeMove(game, 1, 1) }
        binding.row1col2.setOnClickListener { makeMove(game, 1, 2) }
        binding.row2col0.setOnClickListener { makeMove(game, 2, 0) }
        binding.row2col1.setOnClickListener { makeMove(game, 2, 1) }
        binding.row2col2.setOnClickListener { makeMove(game, 2, 2) }
    }

    private fun makeMove(game: Game?, r: Int, c: Int) {
        if (turn) {
            if (game != null && game.state[r][c] == '0') {
                game.state[r][c] = playerTag
                game?.state?.let { GameManager.updateGame(game.gameId, it) }
                turn = false
                binding.gameStatusTv.setText("Waiting for ${opponentTag}'s move")
            }

        }
        game?.let { loadState(it.state) }
    }

    fun loadState(state: GameState) {
        binding.row0Col0.setText(convertChar(state[0][0]))
        binding.row0col1.setText(convertChar(state[0][1]))
        binding.row0col2.setText(convertChar(state[0][2]))

        binding.row1col0.setText(convertChar(state[1][0]))
        binding.row1col1.setText(convertChar(state[1][1]))
        binding.row1col2.setText(convertChar(state[1][2]))

        binding.row2col0.setText(convertChar(state[2][0]))
        binding.row2col1.setText(convertChar(state[2][1]))
        binding.row2col2.setText(convertChar(state[2][2]))

    }

    fun loadPlayers(game: Game) {
        if (game.players.size == 1) {
            playerTag = 'X'
            opponentTag = '0'
            binding.playerOneTv.setText(game.players[0] + "\n(X)")
        } else {
            playerTag = 'O'
            opponentTag = 'X'
            with(binding) {
                playerOneTv.setText(game.players[0] + "\n(X)")
                playerTwoTv.setText(game.players[1] + "\n(O)")
                gameStatusTv.setText(getString(R.string.yourMove))
            }
            turn = true
        }
    }

    fun checkVictory(state: GameState) {


    }

    fun convertChar(c: Char): String {

        if(c=='0')
            return " "
        else
            return c.toString()
    }

}