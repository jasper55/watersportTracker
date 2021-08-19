package wagner.jasper.watersporttracker.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import wagner.jasper.watersporttracker.domain.model.LocationData
import wagner.jasper.watersporttracker.domain.model.ScreenOrientationMode
import wagner.jasper.watersporttracker.domain.usecase.FetchLocationUpdatesUseCase
import wagner.jasper.watersporttracker.utils.DistanceUtils.toKnots
import wagner.jasper.watersporttracker.utils.DistanceUtils.toNauticMiles
import wagner.jasper.watersporttracker.utils.LocationProcessor.distanceBetween
import wagner.jasper.watersporttracker.utils.Mathematics.round
import wagner.jasper.watersporttracker.utils.TimeUtils.toFormattedTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@InternalCoroutinesApi
@HiltViewModel
class MainViewModel @Inject constructor(
    fetchLocationUpdates: FetchLocationUpdatesUseCase
) : ViewModel() {

    val screenOrientation = MutableLiveData(ScreenOrientationMode.PORTRAIT)
    val currentSpeed = MutableLiveData(15.2)
    val currentHeading = MutableLiveData(289)
    val newLocation = MutableLiveData<LocationData>(null)
    val prevLocation = MutableLiveData<LocationData>(null)
    val currentTime = MutableLiveData(System.currentTimeMillis().toFormattedTime())
    val pathLength = MutableLiveData(0.0)

    init {
        viewModelScope.launch {
            fetchLocationUpdates().collect { location ->

                // update prev data
                prevLocation.value = newLocation.value

                // update current data
                newLocation.value = location
                currentTime.value = location.timeStamp.toFormattedTime()

                prevLocation.value?.let { prevLoc ->
                    val distance = distanceBetween(
                        lon1 = prevLoc.longitude,
                        lat1 = prevLoc.latitude,
                        lon2 = location.longitude,
                        lat2 = location.latitude
                    )
                    pathLength.value = pathLength.value?.plus(distance.toNauticMiles())
                    val timeDiff = TimeUnit.NANOSECONDS.toSeconds(location.elapsedTime - prevLoc.elapsedTime)
                    currentSpeed.value = round((distance/timeDiff).toKnots(),2)
                    currentHeading.value = location.bearing.toInt()
                }
            }
        }
    }

    fun toggleScreenOrientation() {
        when (screenOrientation.value) {
            ScreenOrientationMode.PORTRAIT -> screenOrientation.value =
                ScreenOrientationMode.LANDSCAPE
            ScreenOrientationMode.LANDSCAPE -> screenOrientation.value =
                ScreenOrientationMode.PORTRAIT
        }
    }

}