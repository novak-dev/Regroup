package dev.novak.regroup

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dev.novak.regroup.model.LocationsViewModel
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    private val locationsViewModel: LocationsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        setContentView(R.layout.activity_main)

        val apiKey = packageManager.getApplicationInfo(packageName,
            PackageManager.GET_META_DATA).metaData.getString("com.google.android.geo.API_KEY")!!
        locationsViewModel.setKey(apiKey)
    }

}
