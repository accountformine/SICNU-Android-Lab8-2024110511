package cn.edu.sicnu.guoxiangyu2024110511.weather.model

data class City(
    val id: Int = 0,
    val pid: Int = 0,
    val city_code: String = "",
    val city_name: String = "",
    val post_code: String? = null,
    val area_code: String? = null,
    val ctime: String? = null
) {
    override fun toString(): String = city_name
}
