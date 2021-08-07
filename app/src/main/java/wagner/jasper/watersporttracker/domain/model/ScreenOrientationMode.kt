package wagner.jasper.watersporttracker.domain.model

import android.content.pm.ActivityInfo

enum class ScreenOrientationMode(val identifier: Int) {
    LANDSCAPE(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE),
    PORTRAIT(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT),
}
