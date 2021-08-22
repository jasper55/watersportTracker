package wagner.jasper.watersporttracker.domain.model

enum class DistanceUnit(val factor: Double, val unit: String) {
    METERS(1.0,"m"),
    NAUTIC_MILES(0.000539957,"NM"),
}