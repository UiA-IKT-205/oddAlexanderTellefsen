package com.example.superpiano

import android.graphics.Color.RED
import android.os.Bundle
import android.provider.CalendarContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.superpiano.data.Notes
import com.example.superpiano.databinding.FragmentPianoBinding
import kotlinx.android.synthetic.main.fragment_half_tone_piano_key.*
import kotlinx.android.synthetic.main.fragment_half_tone_piano_key.view.*
import kotlinx.android.synthetic.main.fragment_piano.view.*
import kotlinx.android.synthetic.main.fragment_piano.view.HalfTonePianoKey
import java.io.File
import java.io.FileOutputStream


class PianoLayout : Fragment() {
    private var _binding:FragmentPianoBinding? = null
    private val binding get() = _binding!!
    private val fullTones = listOf("C", "D", "E", "F", "G", "A", "B","C2", "D2", "E2", "F2", "G2")
    private val halfTones = listOf("C#","D#", "Dummy", "F#","G#","A#", "Dummy", "C2#","D2#", "Dummy", "F2#")
    private var score:MutableList<Notes> = mutableListOf<Notes>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPianoBinding.inflate(layoutInflater)
        val view = binding.root

        val fm = childFragmentManager
        val ft = fm.beginTransaction()


        fullTones.forEach{
            val fullTonePianoKey = FullTonePianoKeyFragment.newInstance(it)
            var startPlay:Long = 0

            fullTonePianoKey.onKeyDown = {note ->
                startPlay = System.nanoTime()
                    println("Piano key down$it")
                }

            fullTonePianoKey.onKeyUp = { note ->
                    var endPlay = System.nanoTime()
                    val note = Notes(it, startPlay, endPlay)
                    score.add(note)
                    println("Piano key Up$note")
                }
            ft.add(view.FullTonePianoKey.id, fullTonePianoKey, "note_$it")
        }

        halfTones.forEach{
            val halfTonePianoKey = HalfTonePianoKeyFragment.newInstance(it)
            var startPlay:Long = 0

            halfTonePianoKey.onKeyDown = {note ->
                    startPlay = System.nanoTime()
                    println("Piano key down$note")
                }

            halfTonePianoKey.onKeyUp = {note ->
                    var endPlay = System.nanoTime()
                    val note = Notes(it, startPlay, endPlay)
                    score.add(note)
                    println("Piano key Up$note")

                }

            if (it.equals("Dummy"))
            {
                //Add Code that adds fragment without button or removes/disables button from existing fragment
                halfTonePianoKey.disableButton = true
            }

            ft.add(view.HalfTonePianoKey.id, halfTonePianoKey, "note_$it")
        }
        ft.commit()

       view.saveTonesBt.setOnClickListener {
            var fileName = view.fileEditText.text.toString()
            val path = this.activity?.getExternalFilesDir(null)
            if (score.count() > 0 && fileName.isNotEmpty() && path != null){
                fileName = "$fileName.mus"
                FileOutputStream(File(path,fileName),true).bufferedWriter().use { writer ->

                    score.forEach {
                        writer.write("${it.toString()}\n")
                    }
                }
            } else {
                /// TODO: What to do?
            }
        }

        return view
    }


}