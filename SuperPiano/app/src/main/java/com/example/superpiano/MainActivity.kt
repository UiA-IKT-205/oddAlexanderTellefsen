package com.example.superpiano

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.superpiano.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

private val TAG:String = "SuperPiano:MainActivity"

private lateinit var binding: ActivityMainBinding
private lateinit var auth: FirebaseAuth
private lateinit var piano:PianoLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        signInAnonymously()
        piano = supportFragmentManager.findFragmentById(binding.piano.id) as PianoLayout
        piano.onSave = {
            this.uploadFile(it)
        }
    }


    private fun uploadFile(file: Uri){

        Log.d(TAG, "Upload file $file")
        val ref = FirebaseStorage.getInstance().reference.child("tunes/${file.lastPathSegment}")
        var uploadTask = ref.putFile(file)
        uploadTask.addOnSuccessListener {
            Log.d(TAG, "Saved file ${it.toString()} to cloud")
        }.addOnFailureListener {
            Log.e(TAG, "Error saving ${file.toString()} to cloud")
        }
    }
    private fun signInAnonymously(){

        auth.signInAnonymously().addOnSuccessListener {
            Log.d(TAG, "Login success ${it.user.toString()}")


        }.addOnFailureListener {
            Log.e(TAG, "Login failed $it")

        }


    }
}