package dev.novak.regroup

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dev.novak.regroup.model.LocationsViewModel
import timber.log.Timber


class AddOriginsFragment : Fragment() {
    private val locationsViewModel: LocationsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_add_origins, container, false)

        view.findViewById<Button>(R.id.add_origin_fab).setOnClickListener {
            onAddLocationButtonClicked()
        }

        view.findViewById<Button>(R.id.next_button).setOnClickListener {
            findNavController().navigate(R.id.action_origin_to_destination)
        }
        return view
    }


    // See https://medium.com/@gracekim1611/android-studio-dialogs-edb96717a64e
    private fun onAddLocationButtonClicked() {
        val alert = AlertDialog.Builder(requireContext())
        val locationInput = EditText(requireContext())
        val nextButton = requireView().findViewById<Button>(R.id.next_button)

        alert.setTitle("Add a starting location")
            .setView(locationInput)
            .setPositiveButton("Add") { _, _ ->
                if (!locationInput.text.isNullOrBlank()) {
                    val origin = locationInput.text.toString()
                    Timber.i("Adding to origin list: $origin")
                    locationsViewModel.addOrigin(origin)
                    nextButton.isEnabled = true
                }
            }
            .setNegativeButton("Cancel") { _, _ -> }
            .show()
    }

}