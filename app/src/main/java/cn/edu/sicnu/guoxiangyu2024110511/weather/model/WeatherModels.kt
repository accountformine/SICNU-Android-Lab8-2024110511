package cn.edu.sicnu.guoxiangyu2024110511.weather.model

data class WeatherResponse(
    val status: Int = 0,
    val message: String = "",
    val date: String = "",
    val time: String = "",
    val cityInfo: CityInfo = CityInfo(),
    val data: WeatherData = WeatherData()
)

data class CityInfo(
    val city: String = "",
    val citykey: String = "",
    val parent: String = "",
    val updateTime: String = ""
)

data class WeatherData(
    val shidu: String = "",
    val pm25: Double = 0.0,
    val pm10: Double = 0.0,
    val quality: String = "",
    val wendu: String = "",
    val ganmao: String = "",
    val forecast: List<Forecast> = emptyList()
)

data class Forecast(
    val date: String = "",
    val high: String = "",
    val low: String = "",
    val ymd: String = "",
    val week: String = "",
    val fx: String = "",
    val fl: String = "",
    val type: String = "",
    val notice: String = ""
) {
    override fun toString(): String = "$ymd $week  $type  $low ~ $high ($fx $fl)"
}
