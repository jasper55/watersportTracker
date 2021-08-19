package wagner.jasper.watersporttracker.utils

object DistanceUtils {

    fun Double.toNauticMiles() = this * 0.000539957
    fun Double.toKnots() = this * 1.94384

}