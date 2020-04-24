package academy.learnprogramming.placefinder.PlacesDetail

import academy.learnprogramming.placefinder.FromPlaceId
import academy.learnprogramming.placefinder.MainAdapter
import academy.learnprogramming.placefinder.Place
import academy.learnprogramming.placefinder.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.place_detail_row.view.*
import okhttp3.*

import java.io.IOException

class PlaceDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        recyclerView_main.layoutManager = LinearLayoutManager(this)

        // we'll change the nav bar title..
        val navBarTitle = intent.getStringExtra(MainAdapter.CustomViewHolder.PLACE_TITLE_KEY)
        supportActionBar?.title = navBarTitle

        val placeId = intent.getIntExtra(MainAdapter.CustomViewHolder.PLACE_ID_KEY, -1)

        fetchJSON()
    }

    private fun fetchJSON() {

        val placeId = intent.getLongExtra(MainAdapter.CustomViewHolder.PLACE_ID_KEY, -1)
        val placeDetailUrl = "https://www.noforeignland.com/home/api/v1/place?id=" + placeId

        val client = OkHttpClient()
        val request = Request.Builder().url(placeDetailUrl).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                println("onResponse for individual place called")
                val body = response.body?.string()

                val gson = GsonBuilder().create()

                val fromPlaceId = gson.fromJson(body, FromPlaceId::class.java)
                val place = gson.fromJson(body, Place::class.java)

                runOnUiThread {
                    recyclerView_main.adapter =
                        PlaceDetailAdapter(
                            fromPlaceId
                        )
                }

            }

            override fun onFailure(call: Call, e: IOException) {

            }
        })
    }



}

