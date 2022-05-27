package dev.novak.regroup

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import dev.novak.regroup.model.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    // TODO: https://support.iconscout.com/en/article/how-to-give-attribution-1xm9g7a/

    private val apiKey: String by lazy {
        packageManager
            .getApplicationInfo(packageName, PackageManager.GET_META_DATA).metaData
            .getString("com.google.android.geo.API_KEY")!!
    }
    private lateinit var viewModel: LocationsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.find_destination_button).setOnClickListener {
            onDestinationButtonClicked()
        }
        findViewById<Button>(R.id.add_location_fab).setOnClickListener {
            onAddLocationButtonClicked()
        }
        viewModel = LocationsViewModel(apiKey)
        addDestinationListener()
    }

    // See https://medium.com/@gracekim1611/android-studio-dialogs-edb96717a64e
    private fun onAddLocationButtonClicked() {
        Timber.i("onAddLocationButtonClicked")
        val alert = AlertDialog.Builder(this)
        val locationInput = EditText(this)
        alert.setTitle("Add a starting location")
            .setView(locationInput)
            .setPositiveButton("Add") { _, _ ->
                if (!locationInput.text.isNullOrBlank()) {
                    val origin = locationInput.text.toString()
                    Timber.i("Adding to origin list: $origin")
                    viewModel.addOrigin(origin)
                }
            }
            .setNegativeButton("Cancel") { _, _ -> }
            .show()
    }

    private fun onDestinationButtonClicked() {
        val searchInput = findViewById<TextInputLayout>(R.id.keyword_input).editText?.text.toString()
        Timber.i("searchListener input: $searchInput")
        if (searchInput.isNotBlank()) {
            val progressIndicator = findViewById<View>(R.id.progress_indicator)
            progressIndicator.visibility = View.VISIBLE
            viewModel.startSearch(searchInput)
        } else {
            Timber.e("Not able to search, no keyword provided.")
        }
    }

    private fun addDestinationListener() {
        Timber.i("addDestinationListener")
        val progressIndicator = findViewById<View>(R.id.progress_indicator)
        val resultText = findViewById<TextView>(R.id.result_text)
        viewModel.onDestination {
            Timber.i("Got destination result: $it")
            progressIndicator.visibility = View.INVISIBLE
            it?.let {
                resultText.text = it
            }
        }
    }
}
