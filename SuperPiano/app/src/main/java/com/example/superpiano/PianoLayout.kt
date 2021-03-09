package com.example.superpiano

import android.app.AlertDialog
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import com.example.superpiano.data.Notes
import com.example.superpiano.databinding.FragmentPianoBinding
import kotlinx.android.synthetic.main.fragment_piano.view.*
import kotlinx.android.synthetic.main.fragment_piano.view.HalfTonePianoKey
import java.io.File
import java.io.FileOutputStream

class PianoLayout : Fragment() {

    var onSave:((file: Uri) -> Unit)? = null

    private var _binding:FragmentPianoBinding? = null
    private val binding get() = _binding!!
    private val fullTones = listOf("C", "D", "E", "F", "G", "A", "B","C2", "D2", "E2", "F2", "G2")
    private val halfTones = listOf("C#","D#", "Dummy", "F#","G#","A#", "Dummy", "C2#","D2#", "Dummy", "F2#")
    private var score:MutableList<Notes> = mutableListOf<Notes>()
    var startTune:Long = 0

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
            var durationKey: Long

            //Set beginning time for tune
            if(score.count()==0)
                {
                    startTune = System.currentTimeMillis()
                }

            fullTonePianoKey.onKeyDown = {note ->
                startPlay = System.currentTimeMillis()-startTune
                    println("Piano key down$it")
                }

            fullTonePianoKey.onKeyUp = { note ->
                    durationKey = System.currentTimeMillis()-startTune-startPlay
                    val note = Notes(it, startPlay, durationKey)
                    score.add(note)
                    println("Piano key Up$note")
                }
            ft.add(view.FullTonePianoKey.id, fullTonePianoKey, "note_$it")
        }

        halfTones.forEach{
            val halfTonePianoKey = HalfTonePianoKeyFragment.newInstance(it)
            var startPlay:Long = 0
            var durationKey:Long

            halfTonePianoKey.onKeyDown = {note ->
                    startPlay = System.currentTimeMillis()-startTune
                    println("Piano key down$note")
                }

            halfTonePianoKey.onKeyUp = {note ->
                    durationKey = System.currentTimeMillis()-startTune-startPlay
                    val note = Notes(it, startPlay, durationKey)
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

           if (fileName.isEmpty())
           {
               Toast.makeText(requireActivity(), "Please enter file name (Default is tune.mu)", Toast.LENGTH_LONG).show()
               fileName="tune.mu"
           }
           else
               fileName = "$fileName.mus"

           var file = File(path, fileName)


           if(path == null)
               Toast.makeText(requireActivity(), "Illegal folder path..", Toast.LENGTH_LONG).show()

           else if(score.count() == 0)
               Toast.makeText(requireActivity(), "No tune made..", Toast.LENGTH_LONG).show()

           else if(file.exists()){
               handleFileExists(file, path)}

           else {
                FileOutputStream(File(path,fileName),true).bufferedWriter().use { writer ->

                    score.forEach {
                        writer.write("${it.toString()}\n")
                    }
                    this.onSave?.invoke((file.toUri()))
                    Toast.makeText(requireActivity(), "File saved..", Toast.LENGTH_LONG).show()
                }
            }
        }

        return view
    }

    private fun handleFileExists(file: File, path: File) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Warning")
        builder.setMessage("File already exists")

        builder.setNeutralButton("Append tune", DialogInterface.OnClickListener{dialog, which ->
            FileOutputStream(File(path,file.name),true).bufferedWriter().use { writer ->
                score.forEach {
                    writer.write("${it.toString()}\n")
                }
             dialog.dismiss()
             this.onSave?.invoke((file.toUri()))
             Toast.makeText(requireActivity(), "File saved..", Toast.LENGTH_LONG).show()
            }
        })


        builder.setNegativeButton("Overwrite") { dialog, which ->
            var tune: String = ""
            score.forEach {
                tune = tune + ("${it.toString()}\n")
                file.writeText("${it.toString()}\n")
            }
            file.writeText(tune)
            this.onSave?.invoke((file.toUri()))
            dialog.dismiss()
            Toast.makeText(requireActivity(), "File saved..", Toast.LENGTH_LONG).show()

        }

        builder.setPositiveButton("Cancel") { dialog, which ->
            dialog.dismiss()

        }


        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }
}