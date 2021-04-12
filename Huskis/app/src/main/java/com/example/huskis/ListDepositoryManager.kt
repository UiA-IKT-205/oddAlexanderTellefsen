package com.example.huskis

import android.content.Context
import android.util.Log
import com.example.huskis.data.Todo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

private var URL: String = "https://huskis-79721-default-rtdb.europe-west1.firebasedatabase.app/"
private var USER: String = "Anonymous"
private var EMAIL: String = "Anonymous@email.com"
private var DATABASE : String = "FIREBASE DATABASE:"

class ListDepositoryManager {

    private lateinit var listCollection: MutableList<Todo>
    val database = Firebase.database(URL)
    val myRef = database.getReference("Todo-List")
    var onList: ((List<Todo>) -> Unit)? = null


    fun load(user:String, email:String, url: String, context: Context) {

        USER = user
        EMAIL = email
        URL = url

        listCollection = mutableListOf()
        Firebase.database.setPersistenceEnabled(true)
        myRef.keepSynced(true)
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


    }

    fun deleteTodo(index: Int) {
        //Deleting entry from database
        myRef.child(USER).child("List Items").child(listCollection[index].title).removeValue()

        //Deleting entry from local mutableList
        listCollection.removeAt(index)
        onList?.invoke(listCollection)


    }

    fun updateDatabase(title: String) {
        myRef.child(USER).child("List Items").child(title).removeValue()
        for (v in listCollection) {
            myRef.child(USER).child("List Items").child(v.title).child("size").setValue(v.getSize())
            myRef.child(USER).child("List Items").child(v.title).child("completed").setValue(v.getCompleted())

            for (w in v.itemList) {
                myRef.child(USER).child("List Items").child(v.title).child("itemList").child(w.itemName).push()
                myRef.child(USER).child("List Items").child(v.title).child("itemList").child(w.itemName).setValue(w)

                }
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
                Log.w(DATABASE, "Google sign in failed")
            }
        }
        )
    }

    fun getListCount(): Int = listCollection.size

    companion object {
        val instance = ListDepositoryManager()
    }
}