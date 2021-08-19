package wagner.jasper.watersporttracker.domain.model

data class LocationData(
    val latitude: Double,
    val longitude: Double,
    val bearing: Float,
    val timeStamp: Long,
    val elapsedTime: Long,
)
