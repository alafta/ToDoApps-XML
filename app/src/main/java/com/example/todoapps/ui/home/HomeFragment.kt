package com.example.todoapps.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.todoapps.R
import com.example.todoapps.ToDoApplication
import com.example.todoapps.data.model.ToDoItem
import com.example.todoapps.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {
    private var homeBinding: FragmentHomeBinding? = null
    private val binding get() = homeBinding!!

    // ViewModel
    private val homeViewModel: HomeViewModel by activityViewModels {
        HomeViewModelFactory((requireActivity().application as ToDoApplication).repository)
    }

    // Recycler Adapter
    private lateinit var todoAdapter: TodoAdapter
    private var isGridView = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupHomeRecyclerView()

        // FAB
        binding.addNoteFab.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_addFragment)
        }

        // Search
        binding.editText.addTextChangedListener { text ->
            val query = text?.toString() ?: ""
            searchItem(query)
        }
        homeViewModel.searchResults.observe(viewLifecycleOwner) { searchResults ->
            todoAdapter.differ.submitList(searchResults)
        }

        // Change View
        binding.changeView.setOnClickListener {
            toggleRecyclerViewLayout()
        }

        // Fetch data
        homeViewModel.allToDos.observe(viewLifecycleOwner) { item ->
            Log.d("HomeFragment", "Received ToDos: ${item.size}")
            todoAdapter.differ.submitList(item)
            updateUi(item)
        }

    }

    override fun onResume() {
        super.onResume()
        homeViewModel.expandedItems.value?.let {
            todoAdapter.setExpandedItems(it)
        }
    }

    private fun updateUi(todos: List<ToDoItem>?) {
        if (todos != null) {
            if (todos.isNotEmpty()) {
                binding.emptyTodo.visibility = View.GONE
                binding.homeRecyclerView.visibility = View.VISIBLE
            } else {
                binding.emptyTodo.visibility = View.VISIBLE
                binding.homeRecyclerView.visibility = View.GONE
            }
        }
    }

    private fun setupHomeRecyclerView() {
        todoAdapter = TodoAdapter(
            onClick = { todoItem, isGridView ->
                if (!isGridView) {
                    todoAdapter.toggleDescription(todoItem)
                    homeViewModel.updateExpandedItems(todoAdapter.getExpandedItems())
                } else {
                    val action = HomeFragmentDirections.actionHomeFragmentToEditFragment(todoItem)
                    requireView().findNavController().navigate(action)
                }
            },
            onLongClick = { todoItem ->
                val action = HomeFragmentDirections.actionHomeFragmentToEditFragment(todoItem)
                requireView().findNavController().navigate(action)
            }
        )

        homeViewModel.expandedItems.observe(viewLifecycleOwner) { expandedItems ->
            todoAdapter.setExpandedItems(expandedItems)
        }

        binding.homeRecyclerView.apply {
            layoutManager = if (isGridView)
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            else
                LinearLayoutManager(requireContext())

            setHasFixedSize(true)
            adapter = todoAdapter
        }

        setupSwipeToDelete()
    }


    private fun setupSwipeToDelete() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val todoItem = todoAdapter.differ.currentList[position]
                showDeleteConfirmationDialog(todoItem)
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.homeRecyclerView)
    }

    private fun showDeleteConfirmationDialog(todoItem: ToDoItem) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Task")
            .setMessage("Are you sure you want to delete this task?")
            .setPositiveButton("Yes") { _, _ ->
                homeViewModel.deleteToDo(todoItem)
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
                todoAdapter.notifyDataSetChanged()
            }
            .create()
            .show()
    }

    private fun toggleRecyclerViewLayout() {
        if (isGridView) {
            // Switch to Linear Layout
            binding.homeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.changeView.setImageResource(R.drawable.list)
        } else {
            // Switch back to Grid Layout
            binding.homeRecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            binding.changeView.setImageResource(R.drawable.grid)
        }
        isGridView = !isGridView
        todoAdapter.setViewType(isGridView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        homeViewModel.updateExpandedItems(emptySet())
        homeBinding = null
    }

    private fun searchItem(query: String?) {
        val searchQuery = "%$query%"
        homeViewModel.searchToDo(searchQuery)
    }

}
