package com.example.todoapps.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapps.data.model.ToDoItem
import com.example.todoapps.databinding.TodoItemBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TodoAdapter(
    private val onClick: (ToDoItem, Boolean) -> Unit,
    private val onLongClick: (ToDoItem) -> Unit
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    private var isGridView = true
    private var expandedItems = mutableSetOf<Int>()

    fun setViewType(isGridView: Boolean) {
        this.isGridView = isGridView
        notifyDataSetChanged()
    }

    fun setExpandedItems(expandedItems: Set<Int>) {
        this.expandedItems = expandedItems.toMutableSet()
        notifyDataSetChanged()
    }

    fun toggleDescription(todoItem: ToDoItem) {
        val position = differ.currentList.indexOf(todoItem)
        if (position != -1) {
            if (expandedItems.contains(position)) {
                expandedItems.remove(position)
            } else {
                expandedItems.add(position)
            }
            notifyItemChanged(position)
        }
    }

    fun getExpandedItems(): Set<Int> {
        return expandedItems
    }

    inner class TodoViewHolder(val itemBinding: TodoItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(todo: ToDoItem) {
            itemBinding.listTitle.text = todo.title
            itemBinding.listDesc.text = todo.description
            itemBinding.dueDate.text = formatDate(todo.dueDate)

            itemBinding.listDesc.visibility =
                if (expandedItems.contains(adapterPosition) || isGridView) View.VISIBLE else View.GONE

            itemView.setOnClickListener {
                onClick(todo, isGridView)
            }

            itemView.setOnLongClickListener {
                onLongClick(todo)
                true
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<ToDoItem>() {
        override fun areItemsTheSame(oldItem: ToDoItem, newItem: ToDoItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ToDoItem, newItem: ToDoItem): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val itemBinding = TodoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val currentTodo = differ.currentList[position]
        holder.bind(currentTodo)
    }

    private fun formatDate(date: Date): String {
        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return formatter.format(date)
    }
}


