package com.example.huskis

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.huskis.data.Todo
import com.example.huskis.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


private val TAG: String = "Huskis:MainActivity"
class ListHolder {

    companion object {
        var PickedTodo: Todo? = null
        var index:Int? = null
    }
}

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cardListing.layoutManager = LinearLayoutManager(this)
        binding.cardListing.adapter = ListRecyclerAdapter(emptyList<Todo>(), this::onListClicked)

        ListDepositoryManager.instance.onList = {
            (binding.cardListing.adapter as ListRecyclerAdapter).updateCollection(it)
        }
        ListDepositoryManager.instance.load("s", this)

        //Header
        binding.cardListing.addItemDecoration(HeaderDecoration(150, 50))

        //Floating action button
        binding.fabAdd.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            builder.setTitle(getString(R.string.input))
            val dialogLayout = inflater.inflate(R.layout.alert_dialog_input, null)
            val inputText = dialogLayout.findViewById<EditText>(R.id.inputEditText)
            builder.setView(dialogLayout)
            builder.setNeutralButton(getString(R.string.cancel)) { dialog, which -> dialog.dismiss() }
            builder.setPositiveButton(getString(R.string.ok)) { dialogInterface, i -> addList(Todo(inputText.text.toString(), mutableListOf())) }

            builder.show()
        }
    }

    private fun addList(item: Todo) {
        ListDepositoryManager.instance.addTodo(item)

    }

    override fun onResume() {
        super.onResume()
        binding.cardListing.adapter?.notifyDataSetChanged()
    }

    private fun onListClicked(todo: Todo): Unit {
        ListHolder.PickedTodo = todo
        ListHolder.index =
        Log.e(TAG, "Pushed card : >${todo.id}")
        val intent = Intent(this, DetailsActivity::class.java)
        startActivity(intent)
    }

    private fun deleteItem(position: Int) {
        ListDepositoryManager.instance.deleteTodo(position)

    }

}


