package com.example.todoapps.ui.add

import android.app.DatePickerDialog
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.example.todoapps.R
import com.example.todoapps.ToDoApplication
import com.example.todoapps.data.model.ToDoItem
import com.example.todoapps.databinding.FragmentAddBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddFragment : Fragment(){
    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private val addViewModel: AddViewModel by viewModels {
        AddViewModelFactory((requireActivity().application as ToDoApplication).repository)
    }
    private var selectedDate: Date? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Date Picker
        setupDatePickerSpinner()

        // Back button
        binding.backIcon.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // Save button
        binding.saveIcon.setOnClickListener {
            saveToDo()
        }

        savedInstanceState?.getLong("selectedDate")?.let {
            selectedDate = Date(it)
            updateSpinnerDate()
        }

        addViewModel.saveStatus.observe(viewLifecycleOwner) { success ->
            when (success) {
                true -> {
                    Toast.makeText(requireContext(), "To Do Added Successfully", Toast.LENGTH_SHORT).show()

                    val navController = requireActivity().findNavController(R.id.fragmentContainerView)
                    navController.navigate(
                        R.id.homeFragment,
                        null,
                        NavOptions.Builder().setPopUpTo(R.id.homeFragment, true).build()
                    )

                    addViewModel.resetSaveStatus()
                }
                false -> {
                    Toast.makeText(requireContext(), "Failed to Add To Do", Toast.LENGTH_SHORT).show()
                    addViewModel.resetSaveStatus()
                }
                null -> {

                }
            }
        }
    }

    // Reset status
    override fun onResume() {
        super.onResume()
        addViewModel.resetSaveStatus()
    }

    private fun setupDatePickerSpinner() {
        binding.dateTextView.setOnClickListener { showDatePicker() }
        binding.dateIcon.setOnClickListener { showDatePicker() }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val pickedDate = Calendar.getInstance()
                pickedDate.set(year, month, dayOfMonth)

                if (pickedDate.time.after(Date())) {
                    selectedDate = pickedDate.time
                    updateSpinnerDate()
                } else {
                    Toast.makeText(requireContext(), "Please select a date in the future", Toast.LENGTH_SHORT).show()
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    private fun updateSpinnerDate() {
        val formattedDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(selectedDate!!)
        binding.dateTextView.text = formattedDate
    }

    private fun saveToDo() {
        val title = binding.addTitle.text.toString().trim()
        val description = binding.addDesc.text.toString().trim()

        if (title.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter the title", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedDate == null) {
            Toast.makeText(requireContext(), "Please select a due date", Toast.LENGTH_SHORT).show()
            return
        }

        val toDo = ToDoItem(0, title, description, selectedDate!!)
        addViewModel.addToDo(toDo)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
