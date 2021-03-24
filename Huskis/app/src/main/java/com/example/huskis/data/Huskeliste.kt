package com.example.huskis.data

import java.io.Serializable

data class Huskeliste (var tittel:String, val liste:MutableList<String>) : Serializable{

    fun getSize(): Int {
        return liste.size
    }



}