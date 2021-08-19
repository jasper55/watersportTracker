package wagner.jasper.watersporttracker.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {
    val sdf = SimpleDateFormat("HH:mm", Locale.GERMAN)
    fun Long.toFormattedTime(): String { return sdf.format(Date(this)) }

}