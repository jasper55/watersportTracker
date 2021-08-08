package wagner.jasper.watersporttracker.domain.usecase

import android.annotation.SuppressLint
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import wagner.jasper.watersporttracker.domain.model.LocationData
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class FetchLocationUpdatesUseCase @Inject constructor(
    private val client: FusedLocationProviderClient
) {

    @SuppressLint("MissingPermission")
    operator fun invoke(): Flow<LocationData> = callbackFlow {
        val locationRequest = LocationRequest().apply {
            interval = TimeUnit.SECONDS.toMillis(UPDATE_INTERVAL_SECS)
            fastestInterval = TimeUnit.SECONDS.toMillis(FASTEST_UPDATE_INTERVAL_SECS)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val callBack = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                val location = locationResult.lastLocation
                val userLocation = LocationData(
                    latitude = location.latitude,
                    longitude = location.longitude,
                    bearing = location.bearing
                )
                offer(userLocation)
            }
        }

        client.requestLocationUpdates(locationRequest, callBack, Looper.getMainLooper())
        awaitClose { client.removeLocationUpdates(callBack) }
    }

    companion object {
        private const val UPDATE_INTERVAL_SECS = 1L
        private const val FASTEST_UPDATE_INTERVAL_SECS = 1L
    }
}