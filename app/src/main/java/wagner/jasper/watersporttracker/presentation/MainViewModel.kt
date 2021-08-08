package wagner.jasper.watersporttracker.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect
import wagner.jasper.watersporttracker.domain.model.ScreenOrientationMode
import wagner.jasper.watersporttracker.domain.usecase.FetchLocationUpdatesUseCase
import javax.inject.Inject

@InternalCoroutinesApi
@HiltViewModel
class MainViewModel @Inject constructor(
    fetchLocationUpdates: FetchLocationUpdatesUseCase
): ViewModel() {

    val screenOrientation = MutableLiveData(ScreenOrientationMode.PORTRAIT)
    val currentTime = MutableLiveData("15:32")
    val totalDistance = MutableLiveData("0.23 NM")
    val currentSpeed = MutableLiveData(15.2)
    val currentHeading = MutableLiveData("289°")

    init {
       viewModelScope.launch {
           fetchLocationUpdates().collect {
               currentSpeed.value = it.latitude
               currentHeading.value = it.bearing.toString() ?: "-"
           }
       }
    }

    fun toggleScreenOrientation() {
        when (screenOrientation.value) {
            ScreenOrientationMode.PORTRAIT -> screenOrientation.value = ScreenOrientationMode.LANDSCAPE
            ScreenOrientationMode.LANDSCAPE -> screenOrientation.value = ScreenOrientationMode.PORTRAIT
        }
    }

}