package no.uia.ikt205.thgame.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import no.uia.ikt205.knotsandcrosses.R
import no.uia.ikt205.knotsandcrosses.databinding.ActivityGameBinding
import no.uia.ikt205.knotsandcrosses.databinding.DialogCreateGameBinding
import java.lang.ClassCastException

class CreateGameDialog() : DialogFragment() {

    internal lateinit var listener:GameDialogListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            val builder: AlertDialog.Builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val binding = DialogCreateGameBinding.inflate(inflater)

            builder.apply {
                setTitle("Create game")
                setPositiveButton("Create") { dialog, which ->
                    if(binding.username.text.toString() != ""){
                        listener.onDialogCreateGame(binding.username.text.toString())
                    }
                }
                setNegativeButton("Cancel") { dialog, which ->
                    dialog.cancel()
                }
                setView(binding.root)
            }

            builder.create()


        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as GameDialogListener
        } catch (e:ClassCastException){
            throw ClassCastException(("$context must implement GameDialogListener"))

        }
    }

}