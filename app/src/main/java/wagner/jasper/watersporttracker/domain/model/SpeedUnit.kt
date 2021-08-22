package wagner.jasper.watersporttracker.domain.model

enum class SpeedUnit(val factor: Double, val unit: String) {
        METER_PER_SECOND(1.0,"m/s"),
        KM_PER_HOUR(3.6,"km/h"),
        KNOTS(1.94384,"kt"),
}

inline fun <reified T: Enum<T>> T.next(): T {
    val values = enumValues<T>()
    val nextOrdinal = (ordinal + 1) % values.size
    return values[nextOrdinal]
}