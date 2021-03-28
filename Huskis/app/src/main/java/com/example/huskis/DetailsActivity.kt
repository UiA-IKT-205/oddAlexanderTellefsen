package com.example.huskis

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.huskis.databinding.ActivityDetailsBinding
import com.example.huskis.data.Todo

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private lateinit var todo:Todo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        todo = ListHolder.PickedTodo!!
        binding.detailsCardListing.layoutManager = LinearLayoutManager(this)
        binding.detailsCardListing.adapter = DetailRecyclerAdapter(todo.itemList, todo.title)

        //Header
        binding.detailsCardListing.addItemDecoration(HeaderDecoration(150, 50))
        /* if(ListHolder.PickedTodo != null){
            todo = ListHolder.PickedTodo
            //Log.i("Details view", receivedBook.toString())
        } else{

            //setResult(RESULT_CANCELED, Intent(EXTRA_BOOK_INFO).apply {
                // leg til info vi vil sende tilbake til Main
            })

            finish()
        }*/
        title=todo.title


        binding.fabItemAdd.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            builder.setTitle("Enter name of item")
            val dialogLayout = inflater.inflate(R.layout.alert_dialog_input, null)
            val inputText = dialogLayout.findViewById<EditText>(R.id.inputEditText)
            builder.setView(dialogLayout)
            builder.setNeutralButton("Cancel") { dialog, which -> dialog.dismiss() }
            builder.setPositiveButton("OK") { dialogInterface: DialogInterface, i: Int -> addItem(Todo.item(inputText.text.toString(), false)) }
            builder.show()
        }
    }

    private fun addItem(item: Todo.item) {

        ListDepositoryManager.instance.addItem(todo, item)
        //todo.itemList.add(item)
        binding.detailsCardListing.adapter?.notifyDataSetChanged()

    }
}