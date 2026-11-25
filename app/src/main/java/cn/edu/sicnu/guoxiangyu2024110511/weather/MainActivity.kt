package cn.edu.sicnu.guoxiangyu2024110511.weather

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import cn.edu.sicnu.guoxiangyu2024110511.weather.model.City
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_CITY_CODE = "city_code"
        const val EXTRA_CITY_NAME = "city_name"
    }

    private lateinit var lvCities: ListView
    private var cityList: List<City> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lvCities = findViewById(R.id.lvCities)

        thread {
            val json = readFileFromRaw(R.raw.citycode)
            val cityType = object : TypeToken<List<City>>() {}.type
            val parsed: List<City> = Gson().fromJson(json, cityType) ?: emptyList()
            val validCities = parsed.filter { it.city_code.isNotBlank() }

            runOnUiThread {
                cityList = validCities
                lvCities.adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_list_item_1,
                    cityList
                )
            }
        }

        lvCities.setOnItemClickListener { _, _, position, _ ->
            val selected = cityList.getOrNull(position) ?: return@setOnItemClickListener
            val intent = Intent(this, WeatherDetailActivity::class.java).apply {
                putExtra(EXTRA_CITY_CODE, selected.city_code)
                putExtra(EXTRA_CITY_NAME, selected.city_name)
            }
            startActivity(intent)
        }
    }

    private fun readFileFromRaw(rawName: Int): String {
        return try {
            resources.openRawResource(rawName).use { inputStream ->
                BufferedReader(InputStreamReader(inputStream, Charsets.UTF_8)).use { reader ->
                    reader.readText()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "[]"
        }
    }
}
