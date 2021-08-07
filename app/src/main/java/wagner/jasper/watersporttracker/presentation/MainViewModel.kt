package wagner.jasper.watersporttracker.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import wagner.jasper.watersporttracker.domain.model.ScreenOrientationMode
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {

    val screenOrientation = MutableLiveData(ScreenOrientationMode.PORTRAIT)
    val currentTime = MutableLiveData("15:32")
    val totalDistance = MutableLiveData("0.23 NM")
    val currentSpeed = MutableLiveData("15.2")
    val currentHeading = MutableLiveData("289°")

    fun toggleScreenOrientation() {
        when (screenOrientation.value) {
            ScreenOrientationMode.PORTRAIT -> screenOrientation.value = ScreenOrientationMode.LANDSCAPE
            ScreenOrientationMode.LANDSCAPE -> screenOrientation.value = ScreenOrientationMode.PORTRAIT
        }
    }
}