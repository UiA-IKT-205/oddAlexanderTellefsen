package com.example.huskis

import android.content.Context
import com.example.huskis.data.Todo


class ListDepositoryManager {

    private lateinit var listCollection: MutableList<Todo>


    var onList: ((List<Todo>) -> Unit)? = null


    fun load(url: String, context: Context) {

        listCollection = mutableListOf(
                Todo("Butikken", mutableListOf(Todo.item("Jada", true), Todo.item("Java", false), Todo.item("Joda", true))
                ), Todo("Butikken", mutableListOf(Todo.item("Jada", true), Todo.item("Java", false), Todo.item("Joda", false))))

        onList?.invoke(listCollection)
    }

    fun addTodo(todo: Todo) {
        listCollection.add(todo)
        onList?.invoke(listCollection)
    }

    fun deleteTodo(index: Int) {
        listCollection.removeAt(index)

    }

    companion object {
        val instance = ListDepositoryManager()
    }

}