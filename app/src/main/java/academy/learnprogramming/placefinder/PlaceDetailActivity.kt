package academy.learnprogramming.placefinder

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewConfiguration.get
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.place_detail_row.view.*
import okhttp3.*
import okhttp3.HttpUrl.get
import java.io.IOException

class PlaceDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        recyclerView_main.layoutManager = LinearLayoutManager(this)

        // we'll change the nav bar title..
        val navBarTitle = intent.getStringExtra(CustomViewHolder.PLACE_TITLE_KEY)
        supportActionBar?.title = navBarTitle

        val placeId = intent.getIntExtra(CustomViewHolder.PLACE_ID_KEY, -1)

        fetchJSON()
    }

    private fun fetchJSON() {

        val placeId = intent.getLongExtra(CustomViewHolder.PLACE_ID_KEY, -1)
        val placeDetailUrl = "https://www.noforeignland.com/home/api/v1/place?id=" + placeId

        val client = OkHttpClient()
        val request = Request.Builder().url(placeDetailUrl).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                println("onResponse for individual place called")
                val body = response.body()?.string()

                val gson = GsonBuilder().create()
                val fromPlaceId = gson.fromJson(body, FromPlaceId::class.java)

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


     private class PlaceDetailAdapter(val fromPlaceId: FromPlaceId) :
        RecyclerView.Adapter<PlaceInfoViewHolder>() {

        override fun getItemCount(): Int {
            return 1
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceInfoViewHolder {

            val layoutInflater = LayoutInflater.from(parent.context)
            val customView = layoutInflater.inflate(R.layout.place_detail_row, parent, false)

            return PlaceInfoViewHolder(customView, fromPlaceId)

        }

        override fun onBindViewHolder(holder: PlaceInfoViewHolder, position: Int) {
            val place = fromPlaceId.place
            var comments = place.comments

            comments = android.text.Html.fromHtml(comments).toString()

            holder.customView.textView_course_lesson_title?.text = place.name
            holder.customView.textView_place_info?.text = comments

            val bannerImage = holder.customView.imageView_place
            val bannerUrl = place.banner

            val defaultImage = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcTinfoISklIK4Gl2T29OvfmjF4dVXk_E0CXGVyVesHzsABEyeAk&usqp=CAU"

            if (bannerUrl.isEmpty()) {
                Picasso.get().load(defaultImage).into(bannerImage)
            } else {
                Picasso.get().load(bannerUrl).into(bannerImage)
            }
        }
    }

    private class PlaceInfoViewHolder(val customView: View, var fromPlaceId: FromPlaceId? = null) :
        RecyclerView.ViewHolder(customView) {
    }
}

