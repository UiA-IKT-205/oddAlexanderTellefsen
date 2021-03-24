package com.example.huskis.data
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.huskis.databinding.ListlayoutBinding

private val TAG:String = "Huskis:MainActivity"


class ListRecyclerAdapter(private val huskeliste:MutableList<Huskeliste>,
                          private val onListClicked:(Huskeliste)-> Unit)
                        : RecyclerView.Adapter<ListRecyclerAdapter.Viewholder>() {


    inner class Viewholder(val binding:ListlayoutBinding):RecyclerView.ViewHolder(binding.root) {


        fun bind(huskeliste: Huskeliste) {
            var position: Int = getAdapterPosition()

            binding.tvTittel.text = huskeliste.tittel
            binding.pbProgress.max = huskeliste.getSize()
            binding.pbProgress.setProgress(1, true)

            binding.deleteBtn.setOnClickListener {deleteItem(position)}
        }
    }
    override fun getItemCount(): Int = huskeliste.size

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.bind(huskeliste[position])
        holder.itemView.setOnClickListener {
            onListClicked(
                huskeliste[position]
            )
        }
    }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        return Viewholder(ListlayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    }


    public fun updateCollection(newBooks:List<Huskeliste>){
        notifyDataSetChanged()

    }

    public fun deleteItem(position:Int){
        lateinit var dialog:AlertDialog


        huskeliste.removeAt(position)
        notifyDataSetChanged()

    }


}

