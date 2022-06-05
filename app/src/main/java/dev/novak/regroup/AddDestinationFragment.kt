package dev.novak.regroup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import dev.novak.regroup.model.LocationsViewModel
import timber.log.Timber


class AddDestinationFragment : Fragment() {


    private val locationsViewModel: LocationsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_add_destination, container, false)
        view.findViewById<Button>(R.id.next_button).setOnClickListener {
            onNextButton()
        }
        val nextButton = view.findViewById<Button>(R.id.next_button)

        view.findViewById<EditText>(R.id.destination_search)
            .addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) { }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    nextButton.isEnabled = s.toString().trim().isNotEmpty()
                    }
                })
        return view
    }

    private fun onNextButton() {
        val searchInput = view?.findViewById<TextInputLayout>(R.id.keyword_input)
            ?.editText?.text.toString()
        if (searchInput.isNotBlank()) {
            locationsViewModel.startSearch(searchInput)
            findNavController().navigate(R.id.action_destination_to_result)
        } else {
            Timber.e("Not able to search, no keyword provided.")
        }
    }

}