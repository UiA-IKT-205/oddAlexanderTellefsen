package com.example.superpiano.data

data class Notes(val value:String, val start:Long, val duration:Long){


    override fun toString(): String {
        return "$value,$start,$duration"
    }
}

