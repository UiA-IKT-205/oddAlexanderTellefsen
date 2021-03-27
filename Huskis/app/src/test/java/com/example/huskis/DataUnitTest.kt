package com.example.huskis

import com.example.huskis.data.Todo
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UnitTest {


    @Test
    fun dataIsCorrectFormat(){
        assertEquals("Todo(title=Butikken, itemList=[item(itemName=Eple, completed=true), item(itemName=Pære, completed=true), item(itemName=Øl, completed=true)])",
                Todo("Butikken", mutableListOf(Todo.item("Eple", true), Todo.item("Pære", true), Todo.item("Øl", true))).toString())
    }

    @Test
    fun sizeIsCorrect(){

        assertEquals(2, Todo("Butikken", mutableListOf(Todo.item("Eple", true), Todo.item("Pære", true))).getSize())

    }

    @Test
    fun numberOfCompletedIsCorrect(){
        assertEquals(2, Todo("Butikken", mutableListOf(Todo.item("Eple", true), Todo.item("Pære", true),Todo.item("Banan", false))).getCompleted())

    }
}