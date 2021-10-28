package wagner.jasper.watersporttracker.utils

import kotlin.math.pow

object Mathematics {

    fun round(value: Double, places: Int): Double {
        var value = value
        if (places < 0) throw IllegalArgumentException()

        val factor = 10.0.pow(places.toDouble()).toLong()
        value *= factor
        val tmp = Math.round(value)
        return tmp.toDouble() / factor
    }

    fun round(value: Float, places: Int): Float {
        var value = value
        if (places < 0) throw IllegalArgumentException()

        val factor = 10.0.pow(places.toDouble()).toLong()
        value *= factor
        val tmp = Math.round(value)
        return tmp.toFloat() / factor
    }

}
