package cn.edu.sicnu.guoxiangyu2024110511.weather

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cn.edu.sicnu.guoxiangyu2024110511.weather.model.WeatherResponse
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson

class WeatherDetailActivity : AppCompatActivity() {

    companion object {
        private const val WEATHER_URL_PREFIX = "http://t.weather.itboy.net/api/weather/city/"

        fun resolveWeatherIcon(type: String): Int {
            return when {
                type == "晴" -> R.drawable.sun
                type == "阴" -> R.drawable.cloud
                type == "多云" -> R.drawable.mcloud
                type.contains("雨") -> R.drawable.rain
                type.contains("雪") -> R.drawable.snow
                else -> R.drawable.thunder
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val cityCode = intent.getStringExtra(MainActivity.EXTRA_CITY_CODE) ?: "101010100"
        val cityName = intent.getStringExtra(MainActivity.EXTRA_CITY_NAME) ?: ""

        val ivWeatherIcon = findViewById<ImageView>(R.id.ivWeatherIcon)
        val tvCityProvince = findViewById<TextView>(R.id.tvCityProvince)
        val tvTemperature = findViewById<TextView>(R.id.tvTemperature)
        val tvWeatherMeta = findViewById<TextView>(R.id.tvWeatherMeta)
        val lvForecast = findViewById<ListView>(R.id.lvForecast)

        tvCityProvince.text = getString(R.string.loading_weather, cityName)

        val queue = Volley.newRequestQueue(this)
        val url = WEATHER_URL_PREFIX + cityCode
        val request = object : StringRequest(
            Request.Method.GET,
            url,
            Response.Listener { json ->
                val weather = Gson().fromJson(json, WeatherResponse::class.java)
                val info = weather.cityInfo
                val data = weather.data
                val firstDay = data.forecast.firstOrNull()

                tvCityProvince.text = getString(R.string.city_parent_format, info.city, info.parent)
                tvTemperature.text = getString(R.string.temp_format, data.wendu)
                val todayType = firstDay?.type ?: ""
                tvWeatherMeta.text = getString(
                    R.string.weather_detail_format,
                    todayType,
                    data.shidu,
                    data.quality
                )
                ivWeatherIcon.setImageResource(resolveWeatherIcon(todayType))
                lvForecast.adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_list_item_1,
                    data.forecast
                )
            },
            Response.ErrorListener { error ->
                tvCityProvince.text = getString(
                    R.string.error_weather,
                    error.message ?: error.javaClass.simpleName
                )
            }
        ) {
            override fun parseNetworkResponse(response: NetworkResponse): Response<String> {
                val charset = HttpHeaderParser.parseCharset(response.headers, "UTF-8")
                val parsed = String(response.data, charset(charset))
                return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response))
            }
        }
        queue.add(request)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
