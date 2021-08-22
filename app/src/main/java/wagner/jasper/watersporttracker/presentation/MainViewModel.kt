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
import wagner.jasper.watersporttracker.utils.LocationProcessor.bearingBetweenTwoCoordinates
import wagner.jasper.watersporttracker.utils.LocationProcessor.getCurrentSpeedInMeters
import wagner.jasper.watersporttracker.utils.LocationProcessor.getDistanceInMeters
import wagner.jasper.watersporttracker.utils.TimeUtils.toFormattedTime
import javax.inject.Inject

@InternalCoroutinesApi
@HiltViewModel
class MainViewModel @Inject constructor(
    fetchLocationUpdates: FetchLocationUpdatesUseCase
) : ViewModel() {

    val screenOrientation = MutableLiveData(ScreenOrientationMode.PORTRAIT)
    val currentSpeed = MutableLiveData(15.2)
    val currentHeading = MutableLiveData(289)
    private val newLocation = MutableLiveData<LocationData>(null)
    private val prevLocation = MutableLiveData<LocationData>(null)
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
                    pathLength.value =
                        pathLength.value?.plus(getDistanceInMeters(prevLoc, location))
                    currentSpeed.value = getCurrentSpeedInMeters(prevLoc, location)
                    currentHeading.value = bearingBetweenTwoCoordinates(prevLoc, location)
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