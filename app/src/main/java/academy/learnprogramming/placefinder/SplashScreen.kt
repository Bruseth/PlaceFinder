package academy.learnprogramming.placefinder

import academy.learnprogramming.placefinder.db.PlacesDatabase
import academy.learnprogramming.placefinder.db.PlacesEntity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val database =
            Room.databaseBuilder(applicationContext, PlacesDatabase::class.java, "places.db").allowMainThreadQueries().build()

        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 2000)

        fetchJson(database)
    }

    private fun fetchJson(database: PlacesDatabase) {

        val url = "https://www.noforeignland.com/home/api/v1/places/"

        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()

                val gson = GsonBuilder().create()

                val places = gson.fromJson(body, Places::class.java)

                if (database.placesDAO().getAllPlaces().isEmpty()) {
                    places.features.forEach {
                        val thread = Thread {
                            val placesEntity = PlacesEntity()
                            placesEntity.placesId = it.properties.id
                            placesEntity.placeName = it.properties.name
                            placesEntity.placeLon = it.geometry.coordinates[0]
                            placesEntity.placeLat = it.geometry.coordinates[1]

                            database.placesDAO().insertPlaces(placesEntity)
                        }
                        thread.start()
                    }
                } else {
                    Log.d("database", "Fetching to local storage")
                }
                Log.d("database", "Fetching from API")
            }

            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
            }
        })
    }
}