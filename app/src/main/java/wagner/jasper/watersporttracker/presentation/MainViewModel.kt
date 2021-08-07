package wagner.jasper.watersporttracker.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import wagner.jasper.watersporttracker.domain.model.ScreenOrientationMode
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {

    val screenOrientation = MutableLiveData(ScreenOrientationMode.PORTRAIT)

    fun toggleScreenOrientation() {
        when (screenOrientation.value) {
            ScreenOrientationMode.PORTRAIT -> screenOrientation.value = ScreenOrientationMode.LANDSCAPE
            ScreenOrientationMode.LANDSCAPE -> screenOrientation.value = ScreenOrientationMode.PORTRAIT
        }
    }
}