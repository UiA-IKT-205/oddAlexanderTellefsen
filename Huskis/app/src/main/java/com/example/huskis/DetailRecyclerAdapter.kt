package com.example.huskis

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.example.huskis.data.Todo
import com.example.huskis.databinding.DetailslayoutBinding

class DetailRecyclerAdapter(
    private var item: MutableList<Todo.item>,
    title: String,
    private val updateHeader: () -> Unit
) : RecyclerView.Adapter<DetailRecyclerAdapter.Viewholder>() {

    var title: String = title

    inner class Viewholder(val binding: DetailslayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(item: Todo.item) {
            binding.detailsItemNameTv.text = item.itemName
            binding.itemCheckbox.isChecked = item.completed
            binding.itemCheckbox.setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->
                flipStatus(item)
                updateHeader()
            }
            binding.itemDelete.setOnClickListener {
                deleteItem(item)
                updateHeader()
            }
        }
    }

    override fun getItemCount(): Int = item.size

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.bind(item[position])

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        return Viewholder(
            DetailslayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    fun updateCollection(newList: List<Todo.item>) {
        item = newList as MutableList<Todo.item>
        updateHeader()
        notifyDataSetChanged()

        //Syncing changes with online database/cloud by uploading new sublist to the list
        ListDepositoryManager.instance.updateDatabase(title)

    }

    fun deleteItem(dItem: Todo.item) {
        //Deleting entry from local mutableList
        item.removeAt(item.indexOf(dItem))
        updateCollection(this.item)


    }

    fun flipStatus(item: Todo.item) {
        item.flipStatus()
        updateCollection(this.item)
    }

}
