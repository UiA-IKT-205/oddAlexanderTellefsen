package com.example.huskis

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.huskis.data.Huskeliste
import com.example.huskis.data.ListRecyclerAdapter
import com.example.huskis.data.Liste
import com.example.huskis.databinding.ActivityMainBinding
import java.io.Serializable
import java.text.ParsePosition

private val TAG:String = "Huskis:MainActivity"
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var listAdapter:ListRecyclerAdapter
    private lateinit var view:ListRecyclerAdapter.Viewholder

    var listCollection:MutableList<Huskeliste> = mutableListOf(
        Huskeliste("Butikken", mutableListOf("Jada", "Java", "Joda")),
        Huskeliste("Jobben", mutableListOf("Gram", "Positiv", "Virus", "LAF"))

    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cardListing.layoutManager = LinearLayoutManager(this)
        listAdapter = ListRecyclerAdapter(listCollection, this::onListClicked)
        binding.cardListing.adapter = listAdapter


        binding.fabAdd.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            builder.setTitle("Enter name of list")
            val dialogLayout = inflater.inflate(R.layout.alert_dialog_input, null)
            val inputText  = dialogLayout.findViewById<EditText>(R.id.inputEditText)
            builder.setView(dialogLayout)
            builder.setPositiveButton("OK") { dialogInterface, i -> addItem(Huskeliste(inputText.text.toString(), mutableListOf()))}
            builder.show()

        }

        }

    private fun addItem(item: Huskeliste) {
        listCollection.add(item)
        listAdapter.notifyDataSetChanged()

    }

    private fun onListClicked(huskeliste: Huskeliste):Unit{
        Log.e(TAG, "Pushed card : >${huskeliste.toString()}")
        val intent = Intent(this, Liste::class.java)
        intent.putExtra("data", huskeliste as Serializable)
        startActivity(intent)

    }

    private fun deleteItem(position:Int){
        listCollection.removeAt(position)

    }


    }


