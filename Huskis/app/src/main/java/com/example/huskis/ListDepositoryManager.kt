package com.example.huskis

import android.content.Context
import com.example.huskis.data.Todo
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth

private val URL: String = "https://huskis-79721-default-rtdb.europe-west1.firebasedatabase.app/"
private val USER: String = "Anonymous"


class ListDepositoryManager {


    private lateinit var listCollection: MutableList<Todo>
    val database = Firebase.database(URL)
    val myRef = database.getReference("Todo-List")
    auth

    var onList: ((List<Todo>) -> Unit)? = null


    fun load(url: String, context: Context) {
        listCollection = mutableListOf()
        readFromRealtimeDatabase()

    }


    fun addTodo(todo: Todo) {
        myRef.child(USER).child("List Items").child(todo.title).setValue(todo)
        listCollection.add(todo)
        onList?.invoke(listCollection)

    }

    fun addItem(key: Todo, item: Todo.item) {
        //Adding item to database
        myRef.child(USER).child("List Items").child(key.title).child("itemList")
            .child(item.itemName).setValue(item)

        //Adding item to local mutableList
        listCollection[listCollection.indexOf(key)].itemList.add(item)

        updateStats()
    }

    fun deleteItem(listKey: String, itemKey: String) {
        //Deleting entry from database. Deletion from local mutableList is done in adapter
        myRef.child(USER).child("List Items").child(listKey).child("itemList")
            .child(itemKey).removeValue()

        updateStats()
    }

    fun deleteTodo(index: Int) {
        //Deleting entry from database
        myRef.child(USER).child("List Items").child(listCollection[index].title).removeValue()

        //Deleting entry from local mutableList
        listCollection.removeAt(index)

        updateStats()
    }

    fun flipStatus(listKey: String, item: Todo.item, status: Boolean) {
        //Flipping status locally in mutableList
        item.flipStatus()

        //Flipping status entry on remote database
        myRef.child(USER).child("List Items").child(listKey).child("itemList")
            .child(item.itemName).child("completed").setValue(!status)

        updateStats()

    }

    fun updateStats(){
        for (v in listCollection) {
            myRef.child(USER).child("List Items").child(v.title).child("size").setValue(v.getSize())
            myRef.child(USER).child("List Items").child(v.title).child("completed").setValue(v.getCompleted())
        }

    }

    fun readFromRealtimeDatabase() {
        //Complex interface between app and firebase databse - Reads/updates the value from from database into local mutableLIst at startup
        myRef.orderByKey().addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dBitems = snapshot.child(USER).children.iterator()
                if (dBitems.hasNext()) {
                    val toDoListindex = dBitems.next()
                    val itemsIterator = toDoListindex.children.iterator()
                    while (itemsIterator.hasNext()) {
                        //get current item
                        val currentItem = itemsIterator.next()
                        val itemMap: HashMap<String, Any>
                        val tmp = mutableListOf<Todo.item>()
                        if (currentItem.child("itemList").getValue() != null) {
                            itemMap =
                                currentItem.child("itemList").getValue() as HashMap<String, Any>
                            for ((k, v) in itemMap) {
                                v as HashMap<String, Boolean>
                                println(v.get("completed"))
                                tmp.add(
                                    Todo.item(
                                        v.get("itemName").toString(),
                                        v.get("completed")!!
                                    )
                                )
                            }
                        }
                        listCollection!!.add(Todo(currentItem.key.toString(), tmp))
                    }
                }
                onList?.invoke(listCollection)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        )
    }

    companion object {
        val instance = ListDepositoryManager()
    }
}