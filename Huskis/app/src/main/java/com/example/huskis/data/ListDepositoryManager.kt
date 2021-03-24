package no.uia.ikt205.mybooks.books

import android.content.Context
import android.util.Log
import com.example.huskis.data.Todo


class ListDepositoryManager {

    private lateinit var listCollection: MutableList<Todo>


    var onList: ((List<Todo>) -> Unit)? = null
    var onListUpdate: ((todo: Todo) -> Unit)? = null


    fun load(url: String, context: Context) {

        /*  queue = Volley.newRequestQueue(context)
          val request = JsonArrayRequest(Request.Method.GET, url, null,
              {
                  // JSON -> transport formatet
                  // Gson -> Manipulering og serialisering av json
                  Log.d("BookDepositoryManager", it.toString(3))
              },
              {
                  Log.e("BookDepositoryManager", it.toString())
              })
          queue.add(request)*/



        listCollection = mutableListOf(
        Todo("Butikken", mutableListOf(Todo.item("Jada", true), Todo.item( "Java", false), Todo.item("Joda", true))
        ), Todo("Butikken", mutableListOf(Todo.item("Jada", true), Todo.item( "Java", false), Todo.item("Joda", false))))

        onList?.invoke(listCollection)
    }

    fun updateBook(todo: Todo) {
        // finn bok i listen og erstat med den nye boken.
        onListUpdate?.invoke(todo)
    }

    fun addTodo(todo: Todo) {
        listCollection.add(todo)
        onList?.invoke(listCollection)
    }

    fun deleteTodo(index:Int){
        listCollection.removeAt(index)

    }

    fun deleteItem(index:Int){


    }

    companion object {
        val instance = ListDepositoryManager()
    }

}