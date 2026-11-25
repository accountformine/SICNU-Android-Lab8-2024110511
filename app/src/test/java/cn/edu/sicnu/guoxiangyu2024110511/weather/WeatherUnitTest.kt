package cn.edu.sicnu.guoxiangyu2024110511.weather

import cn.edu.sicnu.guoxiangyu2024110511.weather.model.City
import cn.edu.sicnu.guoxiangyu2024110511.weather.model.WeatherResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.junit.Assert.assertEquals
import org.junit.Test

class WeatherUnitTest {

    @Test
    fun gson_parsesCityListAndFiltersBlankCodes() {
        val sampleJson = """
            [
              {"id":1,"pid":0,"city_code":"101010100","city_name":"北京","post_code":"100000","area_code":"010","ctime":"2019-07-11"},
              {"id":2,"pid":0,"city_code":"","city_name":"安徽","post_code":null,"area_code":null,"ctime":null}
            ]
        """.trimIndent()
        val type = object : TypeToken<List<City>>() {}.type
        val cities: List<City> = Gson().fromJson(sampleJson, type)
        val filtered = cities.filter { it.city_code.isNotBlank() }

        assertEquals(1, filtered.size)
        assertEquals("北京", filtered[0].toString())
        assertEquals("101010100", filtered[0].city_code)
    }

    @Test
    fun gson_parsesWeatherResponseAndResolvesIcon() {
        val sampleWeather = """
            {
              "status": 200,
              "message": "success",
              "cityInfo": {"city": "成都市", "citykey": "101270101", "parent": "四川", "updateTime": "14:00"},
              "data": {
                "shidu": "65%",
                "quality": "良",
                "wendu": "18",
                "forecast": [
                  {"ymd": "2026-11-25", "week": "星期三", "high": "高温 20℃", "low": "低温 12℃", "fx": "北风", "fl": "2级", "type": "多云"}
                ]
              }
            }
        """.trimIndent()
        val weather = Gson().fromJson(sampleWeather, WeatherResponse::class.java)

        assertEquals("成都市", weather.cityInfo.city)
        assertEquals("18", weather.data.wendu)
        assertEquals(1, weather.data.forecast.size)
        assertEquals(R.drawable.mcloud, WeatherDetailActivity.resolveWeatherIcon("多云"))
        assertEquals(R.drawable.rain, WeatherDetailActivity.resolveWeatherIcon("小雨"))
    }
}
