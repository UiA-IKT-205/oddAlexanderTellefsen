package com.example.huskis.data

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.huskis.data.Huskeliste
import com.example.huskis.databinding.ActivityListeBinding
import com.example.huskis.databinding.ActivityMainBinding


class Liste : AppCompatActivity() {

    private lateinit var listAdapter:DetailRecyclerAdapter
    private lateinit var binding: ActivityListeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListeBinding.inflate(layoutInflater)
        setContentView(binding.root)



       val item = intent.getSerializableExtra("data") as? Huskeliste

        if (item != null) {
            setTitle(item.tittel)
        }

        if (item != null) {
            item.tittel = "Hest"
            println(item)

        }


    }
}