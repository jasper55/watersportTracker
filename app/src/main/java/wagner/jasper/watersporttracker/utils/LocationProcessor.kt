package wagner.jasper.watersporttracker.utils

import android.location.Location
import android.util.Log
import wagner.jasper.watersporttracker.utils.Mathematics.round
import wagner.jasper.watersporttracker.utils.Metrics.DEGREE_TO_METER
import java.lang.Math.toDegrees
import kotlin.math.*

object LocationProcessor {


    private const val EARTH_RADIUS = 6371.0 * 1000.0 // meters

    fun distanceBetween(lon1: Double, lat1: Double, lon2: Double, lat2: Double): Double {
        val deltaLon = Math.toRadians(lon2 - lon1)
        val deltaLat = Math.toRadians(lat2 - lat1)
        val a = sin(deltaLat / 2.0).pow(2.0) +
                cos(Math.toRadians(lat1)) *
                cos(Math.toRadians(lat2)) *
                sin(deltaLon / 2.0).pow(2.0)
        val c = 2.0 * atan2(sqrt(a), sqrt(1.0 - a))
        return EARTH_RADIUS * c
    }

    fun lonToMeters(lon: Double): Double {
        val distance = distanceBetween(lon, 0.0, 0.0, 0.0)
        return distance * if (lon < 0.0) -1.0 else 1.0
    }

//    fun metersToGeoPoint(lonMeters: Double,
//                         latMeters: Double): GeoPoint? {
//        val point = GeoPoint(0.0, 0.0)
//        val pointEast: GeoPoint = pointPlusDistanceEast(point, lonMeters)
//        return pointPlusDistanceNorth(pointEast, latMeters)
//    }

    fun latToMeters2(lat: Double): Double {
        val distance = distanceBetween(0.0, lat, 0.0, 0.0)
        return distance * if (lat < 0.0) -1.0 else 1.0
    }

    fun latToMeters(lat: Double): Double {
        return lat * DEGREE_TO_METER
    }

    fun lonToMeters(lat: Double, lon: Double): Double {
        val latRad = Math.toRadians(lat)
        return 40075000 * cos(latRad) / 360 * lon
    }

    fun degreeToMeters(degree: Double): Double {
        return degree * DEGREE_TO_METER
    }


//    private fun getPointAhead(point: GeoPoint,
//                              distance: Double,
//                              azimuthDegrees: Double): GeoPoint {
//        val radiusFraction = distance / EARTH_RADIUS
//        val bearing = Math.toRadians(azimuthDegrees)
//        val lat1 = Math.toRadians(point.Latitude)
//        val lng1 = Math.toRadians(point.Longitude)
//        val lat2_part1 = Math.sin(lat1) * Math.cos(radiusFraction)
//        val lat2_part2 = Math.cos(lat1) * Math.sin(radiusFraction) * Math.cos(bearing)
//        val lat2 = Math.asin(lat2_part1 + lat2_part2)
//        val lng2_part1 = Math.sin(bearing) * Math.sin(radiusFraction) * Math.cos(lat1)
//        val lng2_part2 = Math.cos(radiusFraction) - Math.sin(lat1) * Math.sin(lat2)
//        var lng2 = lng1 + Math.atan2(lng2_part1, lng2_part2)
//        lng2 = (lng2 + 3.0 * Math.PI) % (2.0 * Math.PI) - Math.PI
//        return GeoPoint(Math.toDegrees(lat2), Math.toDegrees(lng2))
//    }

//    private fun pointPlusDistanceEast(point: GeoPoint, distance: Double): GeoPoint {
//        return getPointAhead(point, distance, 90.0)
//    }
//
//    private fun pointPlusDistanceNorth(point: GeoPoint, distance: Double): GeoPoint {
//        return getPointAhead(point, distance, 0.0)
//    }

//    fun calculateDistance(track: Array<GeoPoint>?): Double {
//        var distance = 0.0
//        var lastLon: Double
//        var lastLat: Double
//        //WARNING! I didn't find array.length type. Seems it's int, so we can use next comparison:
//        if (track == null || track.size - 1 <= 0) //track.length == 0 || track.length == 1
//            return 0.0
//        lastLon = track[0].Longitude
//        lastLat = track[0].Latitude
//        for (i in 1 until track.size) {
//            distance += Coordinates.distanceBetween(
//                lastLat, lastLon,
//                track[i].Latitude, track[i].Longitude)
//            lastLat = track[i].Latitude
//            lastLon = track[i].Longitude
//        }
//        return distance
//    }


    fun speedX(location: Location): Double {
        var speed = 0.0
        if (location.hasSpeed()) {
            speed = location.speed.toDouble()
        }
        return speed * cos(Math.toRadians(location.bearing.toDouble()))
    }

    fun speedY(location: Location): Double {
        var speed = 0.0
        if (location.hasSpeed()) {
            speed = location.speed.toDouble()
        }
        return speed * sin(Math.toRadians(location.bearing.toDouble()))
    }

    fun calculateSpeed(
        lon_m: Double,
        lat_m: Double,
        xPrevLon: Double,
        xPrevLat: Double,
        deltaT: Double
    ): Double {
        val distX = xPrevLon - lon_m
        val distY = xPrevLat - lat_m
        val dist = sqrt(distX * distX + distY * distY)
        return round(dist / deltaT, 2)
    }

//    fun calculateBearing(lon_m: Double, lat_m: Double): Double {
//        return round(atan(lon_m / lat_m),0)
//    }

    fun calculateSpeed(distX: Double, distY: Double, deltaT: Double): Double {
        val dist = sqrt(distX * distX + distY * distY)
        var speed = dist / deltaT

        return speed
    }

    fun formatSpeed(speed: Double, unitFactor: Double): String {
        var speed = round(speed * unitFactor, 1).toString()
        Log.d("Debug Output", "speed_rounded: $speed")

        if (speed.toString().length == 4) {
            speed = round(speed.toDouble(), 1).toString()
        }
        if (speed.toString().length == 5) {
            speed = round(speed.toDouble(), 0).toInt().toString()
        }
        Log.d("Debug Output", "speed_returned: $speed")

        return speed
    }

    fun formatVMG(speed: Double): String {
        var speed = round(speed, 1).toString()
        Log.d("Debug Output", "speed_rounded: $speed")

        if (speed.length == 4) {
            speed = round(speed.toFloat(), 1).toString()
        }
        if (speed.length == 5) {
            speed = round(speed.toFloat(), 0).toInt().toString()
        }
        Log.d("Debug Output", "speed_returned: $speed")

        return speed
    }

    fun calculateBearing(lon_m: Double, lat_m: Double): Int {
        val lonRad = Math.toRadians(lon_m)
        val latRad = Math.toRadians(lat_m)
        return round(toDegrees(atan(lonRad / latRad)), 0).toInt()
    }

    fun calculateVMG(speed: Double, heading: Int, wd: Int): Double {
        val headingRad = Math.toRadians(heading.toDouble())
        val wdRad = Math.toRadians(wd.toDouble())
        return Mathematics.round(
            cos(
                headingRad
                    .minus(wdRad)
            )
                    * speed, 2
        )
    }
}

object Metrics {
    const val DEGREE_TO_METER = 111139
}