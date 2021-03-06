package com.example.huskis

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.huskis.databinding.ListlayoutBinding
import com.example.huskis.data.Todo
import kotlin.collections.List

private val TAG: String = "Huskis:MainActivity"


class ListRecyclerAdapter(
    private var todo: List<Todo>,
    private val onListClicked: (todo: Todo) -> Unit
) : RecyclerView.Adapter<ListRecyclerAdapter.Viewholder>() {

    inner class Viewholder(val binding: ListlayoutBinding) : RecyclerView.ViewHolder(binding.root) {


        fun bind(todo: Todo) {
            var position: Int = getAdapterPosition()
            binding.tvTittel.text = todo.title
            binding.pbProgress.max = todo.getSize()
            binding.pbProgress.setProgress(todo.getCompleted(), true)
            binding.pbProgress.max = todo.getSize()
            binding.deleteBtn.setOnClickListener { deleteList(position) }

        }
    }

    override fun getItemCount(): Int = todo.size

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.bind(todo[position])
        holder.itemView.setOnClickListener {
            onListClicked(
                todo[position]
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        return Viewholder(
            ListlayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    fun updateCollection(newList: List<Todo>) {
        todo = newList as MutableList<Todo>
        notifyDataSetChanged()

    }

    fun deleteList(position: Int) {
        ListDepositoryManager.instance.deleteTodo(position)
        updateCollection(todo)

    }
}

