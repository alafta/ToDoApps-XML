package com.example.todoapps.ui.edit

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todoapps.R
import com.example.todoapps.com.example.todoapps.ui.edit.EditViewModelFactory
import com.example.todoapps.data.model.ToDoItem
import com.example.todoapps.databinding.FragmentEditBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class EditFragment : Fragment(){

    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!

    private lateinit var editViewModel: EditViewModel
    private val args: EditFragmentArgs by navArgs()
    private lateinit var currentToDo: ToDoItem
    private var selectedDate: Date? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentEditBinding.inflate(inflater, container, false)

        val factory = EditViewModelFactory(requireContext())
        editViewModel = ViewModelProvider(this, factory)[EditViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Back button
        binding.backIcon.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // Delete button
        binding.deleteIcon.setOnClickListener {
            deleteToDo()
        }

        // Fetch current data
        currentToDo = args.todoItem!!
        binding.editTitle.setText(currentToDo.title)
        binding.editDesc.setText(currentToDo.description)

        // Date picker
        setupDatePicker()

        // Date
        selectedDate = currentToDo.dueDate
        updateDateTextView()

        //FAB
        binding.editNoteFab.setOnClickListener {
            val toDoTitle = binding.editTitle.text.toString().trim()
            val toDoDesc = binding.editDesc.text.toString().trim()

            if (toDoTitle.isNotEmpty()) {
                val updatedToDo = ToDoItem(
                    id = currentToDo.id,
                    title = toDoTitle,
                    description = toDoDesc,
                    dueDate = selectedDate!!
                )
                editViewModel.editToDo(updatedToDo)
                findNavController().popBackStack(R.id.homeFragment, false)
            } else {
                Toast.makeText(context, "Please enter the title", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupDatePicker() {
        binding.dateTextView.setOnClickListener { showDatePicker() }
        binding.dateIcon.setOnClickListener { showDatePicker() }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        selectedDate?.let { calendar.time = it }

        val datePicker = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val pickedDate = Calendar.getInstance()
                pickedDate.set(year, month, dayOfMonth)

                if (pickedDate.time.after(Date())) {
                    selectedDate = pickedDate.time
                    updateDateTextView()
                } else {
                    Toast.makeText(requireContext(), "Please select a future date", Toast.LENGTH_SHORT).show()
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    private fun updateDateTextView() {
        selectedDate?.let {
            val formattedDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(it)
            binding.dateTextView.text = formattedDate
        }
    }

    private fun deleteToDo() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete Item")
            .setMessage("Do you want to delete this note?")
            .setPositiveButton("Delete") { _, _ ->
                editViewModel.deleteToDo(currentToDo)
                Toast.makeText(context, "Item Deleted", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack(R.id.homeFragment, false)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}
