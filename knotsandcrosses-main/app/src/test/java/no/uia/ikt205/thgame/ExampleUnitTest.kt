package no.uia.ikt205.thgame

import android.util.Log
import no.uia.ikt205.knotsandcrosses.api.GameService
import no.uia.ikt205.knotsandcrosses.api.data.Game
import no.uia.ikt205.knotsandcrosses.api.data.GameState

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    var gameState:Game? = null
    val firstPlayer:String = "Odd"
    val secondPlayer:String = "Alex"
    val initState:GameState = listOf(mutableListOf('0','0','0'),mutableListOf('0','0','0'),mutableListOf('0','0','0'))
    val updateState:GameState = listOf(mutableListOf('X','X','X'),mutableListOf('X','X','X'),mutableListOf('O','O','O'))

    @Test
    fun createGame(){
        GameService.createGame(firstPlayer,initState ){ state:Game?, err:Int? ->
            gameState = state
            assertNotNull(state)
            assertNotNull(state?.gameId)
            assertEquals(firstPlayer, state?.players?.get(0))
        }
    }

    @Test
    fun JoinGame(){
        gameState?.gameId?.let {
            GameService.joinGame(secondPlayer, it) { state:Game?, err:Int? ->
                gameState = state
                assertNotNull(state)
                assertNotNull(state?.gameId)
                assertEquals(firstPlayer, state?.players?.get(0))
                assertEquals(secondPlayer, state?.players?.get(1))
            }
        }
    }

    @Test
    fun PollGame(){
        gameState?.let {
            GameService.pollGame(it.gameId) { state: Game?, err: Int? ->
                assertNotNull(state)
                assertNotNull(state?.gameId)
                assertEquals(firstPlayer, state?.players?.get(0))
                assertEquals(secondPlayer, state?.players?.get(1))
                assertEquals(initState, state?.state)
            }
        }
    }

    @Test
    fun UpdateGame(){
        gameState?.let {
            GameService.updateGame(it.gameId, updateState) { state: Game?, err: Int? ->
                assertNotNull(state)
                assertNotNull(state?.gameId)
                assertEquals(firstPlayer, state?.players?.get(0))
                assertEquals(secondPlayer, state?.players?.get(1))
                assertEquals(updateState, state?.state)
            }
        }
    }


    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }


}