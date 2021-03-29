package com.example.huskis

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.huskis.data.Todo
import com.example.huskis.databinding.ActivityDetailsBinding
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.alert_dialog_input.view.*

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private lateinit var todo: Todo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Setting up back button
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = ""

            // show back button on toolbar
            // on back button press, it will navigate to parent activity
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        //Adding picked items to acitivity
        if (ListHolder.PickedTodo != null) {
            todo = ListHolder.PickedTodo!!
            Log.i("Details view", todo.toString())
        } else {

            finish()
        }

        //Adding adapter
        binding.detailsCardListing.layoutManager = LinearLayoutManager(this)
        binding.detailsCardListing.adapter =
            DetailRecyclerAdapter(todo.itemList, todo.title, this::updateHeader)

        //Header
        binding.detailsCardListing.addItemDecoration(HeaderDecoration(150, 50))


        //Updating header on startup (Progressbar)
        updateHeader()

        //Adding floating action button
        binding.fabItemAdd.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            builder.setTitle(getString(R.string.inputItem))
            val dialogLayout = inflater.inflate(R.layout.alert_dialog_input, null)
            val inputText = dialogLayout.findViewById<EditText>(R.id.inputEditText)
            dialogLayout.inputEditText.hint = getString(R.string.inputItemExample)
            builder.setView(dialogLayout)
            builder.setNeutralButton(getString(R.string.cancel)) { dialog, which -> dialog.dismiss() }
            builder.setPositiveButton(getString(R.string.ok)) { dialogInterface: DialogInterface, i: Int ->
                if (inputText.text.toString() != "")
                    addItem(Todo.item(inputText.text.toString(), false))
            }
            builder.show()
        }
    }


    private fun addItem(item: Todo.item) {
        ListDepositoryManager.instance.addItem(todo, item)
        binding.detailsCardListing.adapter?.notifyDataSetChanged()

    }

    fun updateHeader() {
        detailsHeaderTxtId.text = todo.title
        detailsHeaderPb.max = todo.getSize()
        detailsHeaderPb.setProgress(todo.getCompleted(), true)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}