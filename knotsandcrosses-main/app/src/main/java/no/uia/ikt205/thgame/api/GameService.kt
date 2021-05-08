package no.uia.ikt205.knotsandcrosses.api


import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import no.uia.ikt205.knotsandcrosses.App
import no.uia.ikt205.knotsandcrosses.R
import no.uia.ikt205.knotsandcrosses.api.data.Game
import no.uia.ikt205.knotsandcrosses.api.data.GameState
import org.json.JSONObject
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json



typealias GameServiceCallback = (state:Game?, errorCode:Int? ) -> Unit

/*  NOTE:
    Using object expression to make GameService a Singleton.
    Why? Because there should only be one active GameService ever.
 */

object GameService {

    /// NOTE: Do not want to have App.context all over the code. Also it is nice if we later want to support different contexts
    private val context = App.context
    private var gameId = ""

    /// NOTE: God practice to use a que for performing requests.
    private val requestQue:RequestQueue = Volley.newRequestQueue(context)

    /// NOTE: One posible way of constructing a list of API url. You want to construct the urls so that you can support different environments (i.e. Debug, Test, Prod etc)
    private enum class APIEndpoints(val url:String) {
        CREATE_GAME("%1s%2s%3s".format(context.getString(R.string.protocol), context.getString(R.string.domain),context.getString(R.string.base_path)))
    }




    fun createGame(playerId:String, state:GameState, callback:GameServiceCallback) {

        val url = APIEndpoints.CREATE_GAME.url

        val requestData = JSONObject()
        requestData.put("player", playerId)
        requestData.put("state",state)

        val request = object : JsonObjectRequest(Request.Method.POST,url, requestData,
            {
                print("Object received : "+it)
                val players = Json.decodeFromString<MutableList<String>>(it.get("players").toString())
                val gameId:String = it.get("gameId").toString()
                val state = Json.decodeFromString<List<MutableList<Char>>>(it.get("state").toString())

                val game = Game(players, gameId, state)
                println("Start : ${game}")
                callback(game,null)
            }, {
                // Error creating new game.
                callback(null, it.networkResponse.statusCode)
            } ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Game-Service-Key"] = context.getString(R.string.game_service_key)
                return headers
            }
        }

        requestQue.add(request)
    }

    fun joinGame(playerId:String, gameId:String, callback: GameServiceCallback){
        setGameId(gameId)
        println("GAME ID : "+ getGameId())
        val url = APIEndpoints.CREATE_GAME.url+"/"+gameId+"/join"
        println("URL generated : "+url)
        val requestData = JSONObject()
        requestData.put("player", playerId)
        requestData.put("gameId",gameId)

        val request = object : JsonObjectRequest(Request.Method.POST,url, requestData,
            {
                val players = Json.decodeFromString<MutableList<String>>(it.get("players").toString())
                val gameId:String = it.get("gameId").toString()
                val state = Json.decodeFromString<List<MutableList<Char>>>(it.get("state").toString())

                val game = Game(players, gameId, state)
                println("Join : ${game}")

                callback(game,null)
            }, {
                // Error creating new game.
                callback(null, it.networkResponse.statusCode)
            } ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Game-Service-Key"] = context.getString(R.string.game_service_key)
                return headers
            }
        }

        requestQue.add(request)


    }

    fun updateGame(gameId: String, gameState:GameState, callback: GameServiceCallback){

        val url = APIEndpoints.CREATE_GAME.url+"/"+gameId+"/update"

        val requestData = JSONObject()
        requestData.put("gameId", gameId)
        requestData.put("state", gameState)

        val request = object : JsonObjectRequest(Request.Method.POST,url, requestData,
            {
                val players = Json.decodeFromString<MutableList<String>>(it.get("players").toString())
                val gameId:String = it.get("gameId").toString()
                val state = Json.decodeFromString<List<MutableList<Char>>>(gameState.toString())

                val game = Game(players, gameId, state)

                callback(game,null)
            }, {
                // Error creating new game.
                callback(null, it.networkResponse.statusCode)
            } ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Game-Service-Key"] = context.getString(R.string.game_service_key)
                return headers
            }
        }

        requestQue.add(request)

    }

    fun pollGame(gameId: String,callback:GameServiceCallback){

        val url = APIEndpoints.CREATE_GAME.url+"/"+gameId+"/poll"
        val requestData = JSONObject()

        val request = object : JsonObjectRequest(Request.Method.GET,url, requestData,
            {
                val players = Json.decodeFromString<MutableList<String>>(it.get("players").toString())
                val state = Json.decodeFromString<List<MutableList<Char>>>(it.get("state").toString())

                val game = Game(players, gameId, state)
                println("POLL !! : ${game}")
                callback(game,null)
            }, {
                // Error creating new game.
                callback(null, it.networkResponse.statusCode)
            } ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Game-Service-Key"] = context.getString(R.string.game_service_key)
                return headers
            }
        }

        requestQue.add(request)

    }

    fun setGameId(id:String){
        gameId=id

    }

    fun getGameId(): String {
        return gameId

    }

}