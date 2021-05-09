package no.uia.ikt205.thgame

import android.content.Intent
import android.util.Log
import no.uia.ikt205.knotsandcrosses.App.Companion.context
import no.uia.ikt205.knotsandcrosses.GameActivity
import no.uia.ikt205.knotsandcrosses.api.GameService
import no.uia.ikt205.knotsandcrosses.api.data.Game
import no.uia.ikt205.knotsandcrosses.api.data.GameState


typealias GameCallback = (game:Game?) -> Unit

object GameManager {
    val TAG:String = "GameManager"
    val StartingGameState:GameState = listOf(mutableListOf('0','0','0'),mutableListOf('0','0','0'),mutableListOf('0','0','0'))

    fun createGame(player:String){

        GameService.createGame(player,StartingGameState) { game: Game?, err: Int? ->
            if(err != null){
                Log.d(TAG, "Error starting game. Error code : $err")
            } else {
                val intent = Intent(context, GameActivity::class.java)
                intent.putExtra("game", game)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent)
            }
        }
    }

    fun joinGame(player:String, gameId:String){

        GameService.joinGame(player, gameId) { game: Game?, err: Int? ->
            if(err != null){
                Log.d(TAG, "Error joining game. Error code : $err")
            } else {
                val intent = Intent(context, GameActivity::class.java)
                intent.putExtra("game", game)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent)
            }
        }
    }

    fun pollGame(gameId:String, callback:GameCallback){

        GameService.pollGame(gameId) { game: Game?, err: Int? ->
            if(err != null){
                Log.d(TAG, "Error refreshing game. Error code : $err")
            } else {
                callback(game)
            }
        }

    }

    fun updateGame(gameId:String, state:GameState){

        GameService.updateGame(gameId, state) { game: Game?, err: Int? ->
            if(err != null){
                Log.d(TAG, "Error updating game. Error code : $err")
            }
        }
    }

}