package no.uia.ikt205.thgame

import android.content.Intent
import android.util.Log
import no.uia.ikt205.knotsandcrosses.App.Companion.context
import no.uia.ikt205.knotsandcrosses.GameActivity
import no.uia.ikt205.knotsandcrosses.api.GameService
import no.uia.ikt205.knotsandcrosses.api.data.Game
import no.uia.ikt205.knotsandcrosses.api.data.GameState

object GameManager {
    val TAG:String = "GameManager"
    var player:String? = null
    var state:GameState? = null

    val StartingGameState:GameState = listOf(listOf(0,0,0),listOf(0,0,0),listOf(0,0,0))

    fun createGame(player:String){

        GameService.createGame(player,StartingGameState) { game: Game?, err: Int? ->
            if(err != null){
                ///TODO("What is the error code? 406 you forgot something in the header. 500 the server di not like what you gave it")
                Log.d(TAG, "Error starting game. Error code : $err")
            } else {
                val intent = Intent(context, GameActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent)
            }
        }

    }

    fun joinGame(player:String, gameId:String){

        GameService.joinGame(player, gameId) { game: Game?, err: Int? ->
            if(err != null){
                ///TODO("What is the error code? 406 you forgot something in the header. 500 the server di not like what you gave it")
                Log.d(TAG, "Error joining game. Error code : $err")
            } else {

            }
        }



    }

    fun pollGame(gameId:String){

        GameService.pollGame(gameId) { game: Game?, err: Int? ->
            if(err != null){
                ///TODO("What is the error code? 406 you forgot something in the header. 500 the server di not like what you gave it")
                Log.d(TAG, "Error refreshing game. Error code : $err")
            } else {

            }
        }

    }

    fun updateGame(gameId:String, state:GameState){

        GameService.updateGame(gameId, state) { game: Game?, err: Int? ->
            if(err != null){
                ///TODO("What is the error code? 406 you forgot something in the header. 500 the server di not like what you gave it")
                Log.d(TAG, "Error updating game. Error code : $err")
            } else {

            }
        }

    }

}