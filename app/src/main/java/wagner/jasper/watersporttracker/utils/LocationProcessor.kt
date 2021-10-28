package wagner.jasper.watersporttracker.utils

import android.util.Log
import wagner.jasper.watersporttracker.domain.model.LocationData
import wagner.jasper.watersporttracker.utils.Mathematics.round
import wagner.jasper.watersporttracker.utils.Metrics.DEGREE_TO_METER
import java.util.concurrent.TimeUnit
import kotlin.math.*

object LocationProcessor {

    private fun LocationData.latInMeters() = this.latitude * DEGREE_TO_METER

    private fun LocationData.lonInMeters() =
        40075000 * cos(Math.toRadians(this.latitude)) / 360 * this.longitude

    fun getDistanceInMeters(prevLocation: LocationData, currentLocation: LocationData): Double {
        val distX = prevLocation.lonInMeters() - currentLocation.lonInMeters()
        val distY = prevLocation.latInMeters() - currentLocation.latInMeters()
        return sqrt(distX * distX + distY * distY)
    }

    fun getCurrentSpeedInMeters(prevLocation: LocationData, currentLocation: LocationData): Double {
        val distX = prevLocation.lonInMeters() - currentLocation.lonInMeters()
        val distY = prevLocation.latInMeters() - currentLocation.latInMeters()
        val deltaT = (TimeUnit.NANOSECONDS.toMillis(
            currentLocation.elapsedTime - prevLocation.elapsedTime
        ) / 1000.0)


        return round(calculateSpeed(distX, distY, deltaT), 1)
    }

    private fun calculateSpeed(distX: Double, distY: Double, deltaT: Double) =
        sqrt(distX * distX + distY * distY) / deltaT

    fun bearingBetweenTwoCoordinates(start: LocationData, end: LocationData): Int {
        val lat1 = Math.toRadians(start.latitude)
        val lat2 = Math.toRadians(end.latitude)
        val lon1 = Math.toRadians(start.longitude)
        val lon2 = Math.toRadians(end.longitude)
        var deltaLon = lon2 - lon1
        val deltaPhi = ln(
            tan(lat2 / 2 + Math.PI / 4)
                    /
                    tan(lat1 / 2 + Math.PI / 4)
        )
        if (abs(deltaPhi) > Math.PI) {
            if (deltaLon > 0.0) {
                deltaLon = -(2.0 * Math.PI - deltaLon)
            } else {
                deltaLon += 2 * Math.PI
            }
        }
        return ((Math.toDegrees(
            atan2(deltaLon, deltaPhi)
        ) + 360.0) % 360).roundToInt()
    }
}

object Metrics {
    const val DEGREE_TO_METER = 111139
}