package com.example.superpiano

import android.graphics.Color.RED
import android.os.Bundle
import android.provider.CalendarContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.superpiano.databinding.FragmentPianoBinding
import kotlinx.android.synthetic.main.fragment_half_tone_piano_key.*
import kotlinx.android.synthetic.main.fragment_half_tone_piano_key.view.*
import kotlinx.android.synthetic.main.fragment_piano.view.*
import kotlinx.android.synthetic.main.fragment_piano.view.HalfTonePianoKey


class PianoLayout : Fragment() {
    private var _binding:FragmentPianoBinding? = null
    private val binding get() = _binding!!
    private val fullTones = listOf("C", "D", "E", "F", "G", "A", "B","C2", "D2", "E2", "F2", "G2")
    private val halfTones = listOf("C#","D#", "Dummy", "F#","G#","A#", "Dummy", "C2#","D2#","F2#","G2#")

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
            fullTonePianoKey.onKeyDown =
                {
                    println("Piano key down$it")
                }

            fullTonePianoKey.onKeyUp =
                {
                    println("Piano key Up$it")
                }
            ft.add(view.FullTonePianoKey.id, fullTonePianoKey, "note_$it")
        }

        halfTones.forEach{
            val halfTonePianoKey = HalfTonePianoKeyFragment.newInstance(it)
            halfTonePianoKey.onKeyDown =
                {
                    println("Piano key down$it")
                }

            halfTonePianoKey.onKeyUp =
                {
                    println("Piano key Up$it")
                }

            if (it.equals("Dummy"))
            {
                //Add Code that adds fragment without button or removes/disables button from existing fragment
                
            }
                ft.add(view.HalfTonePianoKey.id, halfTonePianoKey, "note_$it")
        }
        ft.commit()
        return view
    }


}