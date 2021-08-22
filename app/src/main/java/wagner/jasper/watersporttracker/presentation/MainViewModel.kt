package wagner.jasper.watersporttracker.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import wagner.jasper.watersporttracker.domain.model.*
import wagner.jasper.watersporttracker.domain.usecase.FetchLocationUpdatesUseCase
import wagner.jasper.watersporttracker.utils.CombinedLiveData
import wagner.jasper.watersporttracker.utils.LocationProcessor.bearingBetweenTwoCoordinates
import wagner.jasper.watersporttracker.utils.LocationProcessor.getCurrentSpeedInMeters
import wagner.jasper.watersporttracker.utils.LocationProcessor.getDistanceInMeters
import wagner.jasper.watersporttracker.utils.Mathematics.round
import wagner.jasper.watersporttracker.utils.TimeUtils.toFormattedTime
import javax.inject.Inject

@InternalCoroutinesApi
@HiltViewModel
class MainViewModel @Inject constructor(
    fetchLocationUpdates: FetchLocationUpdatesUseCase
) : ViewModel() {

    val screenOrientation = MutableLiveData(ScreenOrientationMode.PORTRAIT)
    private val speed = MutableLiveData(15.2)
    private val pathLengthInMeters = MutableLiveData(0.0)

    val speedUnit = MutableLiveData(SpeedUnit.KM_PER_HOUR)

    val distanceUnit = speedUnit.map {
        when (it) {
            SpeedUnit.KM_PER_HOUR, SpeedUnit.METER_PER_SECOND -> DistanceUnit.METERS
            SpeedUnit.KNOTS -> DistanceUnit.NAUTIC_MILES
        }
    }

    val pathUi =
        CombinedLiveData(pathLengthInMeters, distanceUnit.map { it.factor }) { data: List<*> ->
            val result = round(
                (pathLengthInMeters.value)?.times(distanceUnit.value?.factor ?: 1.0) ?: 0.0,1)
            result
        }


    val speedUi = CombinedLiveData(speed, speedUnit.map { it.factor }) { data: List<*> ->
        val speed = (data[0] as? Double)?.times(data[1] as? Double ?: 1.0)
        round(speed ?: 0.0, 1)

    }
    val currentHeading = MutableLiveData(289)
    private val newLocation = MutableLiveData<LocationData>(null)
    private val prevLocation = MutableLiveData<LocationData>(null)
    val currentTime = MutableLiveData(System.currentTimeMillis().toFormattedTime())

    init {
        viewModelScope.launch {
            fetchLocationUpdates().collect { location ->

                // update prev data
                prevLocation.value = newLocation.value

                // update current data
                newLocation.value = location
                currentTime.value = location.timeStamp.toFormattedTime()

                prevLocation.value?.let { prevLoc ->
                    pathLengthInMeters.value =
                        pathLengthInMeters.value?.plus(getDistanceInMeters(prevLoc, location))
                    speed.value = getCurrentSpeedInMeters(prevLoc, location)
                    currentHeading.value = bearingBetweenTwoCoordinates(prevLoc, location)
                }
            }
        }
    }

    fun toggleUnit() {
        speedUnit.value = speedUnit.value?.next()
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