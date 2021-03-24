package com.example.huskis

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.huskis.databinding.ActivityDetailsBinding
import com.example.huskis.data.Todo
import com.example.huskis.ListHolder
import com.example.huskis.data.DetailRecyclerAdapter
import com.example.huskis.data.ListRecyclerAdapter

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private lateinit var todo:Todo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        todo = ListHolder.PickedTodo!!
        binding.detailsCardListing.layoutManager = LinearLayoutManager(this)
        binding.detailsCardListing.adapter = DetailRecyclerAdapter(todo.itemList)
       /* if(ListHolder.PickedTodo != null){
            todo = ListHolder.PickedTodo
            //Log.i("Details view", receivedBook.toString())
        } else{

            //setResult(RESULT_CANCELED, Intent(EXTRA_BOOK_INFO).apply {
                // leg til info vi vil sende tilbake til Main
            })

            finish()
        }
        title=todo.title.toString()*/
    }
}