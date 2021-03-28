package com.example.huskis.data

import java.io.Serializable

data class Todo (var title:String="", val itemList:MutableList<item>) : Serializable{
    var id = this.hashCode();

    data class item(var itemName:String="", var completed:Boolean){


            fun flipStatus(){
                completed=!completed
            }
    }

    fun getSize(): Int {
        print("\nTOTAL : \n")
        print(itemList.size)
        return itemList.size

    }

    fun getCompleted(): Int {
        var n:Int = 0
        for (i in itemList)
        {
            print(i.completed)
            if(i.completed == true)
                n+=1
        }
        print("\nCOMPLETED : \n")
        print(n)
        return n

    }



}