package no.uia.ikt205.knotsandcrosses

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import no.uia.ikt205.knotsandcrosses.api.GameService
import no.uia.ikt205.knotsandcrosses.api.data.Game
import no.uia.ikt205.knotsandcrosses.databinding.ActivityMainBinding
import no.uia.ikt205.thgame.GameManager
import no.uia.ikt205.thgame.dialogs.CreateGameDialog
import no.uia.ikt205.thgame.dialogs.JoinGameDialog
import no.uia.ikt205.thgame.dialogs.GameDialogListener

class MainActivity : AppCompatActivity() , GameDialogListener {

    val TAG:String = "MainActivity"

    lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startGameButton.setOnClickListener {
            createNewGame()
        }

        binding.joinGameButton.setOnClickListener {
            joinGame()
        }

    }

    private fun createNewGame(){
        val dlg = CreateGameDialog()
        dlg.show(supportFragmentManager,"CreateGameDialogFragment")

    }

    private fun joinGame(){
        val dlg = JoinGameDialog()
        dlg.show(supportFragmentManager, "JoinGameDiaglogFragment")

        Log.d(TAG, "join game button pushed")

    }

    override fun onDialogCreateGame(player: String) {
        Log.d(TAG,player)
        GameManager.createGame(player)
    }

    override fun onDialogJoinGame(player: String, gameId: String) {
        Log.d(TAG, "HALLO$player $gameId")
        GameManager.joinGame(player, gameId)

    }

}