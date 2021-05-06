package no.uia.ikt205.knotsandcrosses

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import no.uia.ikt205.knotsandcrosses.api.data.Game
import no.uia.ikt205.knotsandcrosses.databinding.ActivityGameBinding
import no.uia.ikt205.thgame.GameManager
import no.uia.ikt205.knotsandcrosses.api.data.GameState

typealias GameCallback = (state: Game?) -> Unit

class GameActivity : AppCompatActivity() {

    private lateinit var binding:ActivityGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }

    fun loadState(state:GameState){


    }
}