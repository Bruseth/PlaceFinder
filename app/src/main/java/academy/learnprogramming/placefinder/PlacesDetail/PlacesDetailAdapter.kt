package academy.learnprogramming.placefinder.PlacesDetail

import academy.learnprogramming.placefinder.FromPlaceId
import academy.learnprogramming.placefinder.GoogleMap.MapsActivity
import academy.learnprogramming.placefinder.R
import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.place_detail_row.view.*

class PlaceDetailAdapter(val fromPlaceId: FromPlaceId) :
    RecyclerView.Adapter<PlaceDetailAdapter.PlaceInfoViewHolder>() {

    override fun getItemCount(): Int {
        return 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceInfoViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val customView = layoutInflater.inflate(R.layout.place_detail_row, parent, false)

        return PlaceInfoViewHolder(
            customView,
            fromPlaceId
        )

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PlaceInfoViewHolder, position: Int) {
        val place = fromPlaceId.place
        var comments = place.comments

        comments = android.text.Html.fromHtml(comments).toString()

        holder.customView.textView_course_lesson_title?.text = place.name
        holder.customView.textView_place_info?.text = comments

        val bannerImage = holder.customView.imageView_place
        val bannerUrl = place.banner

        val defaultImage =
            "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcTinfoISklIK4Gl2T29OvfmjF4dVXk_E0CXGVyVesHzsABEyeAk&usqp=CAU"

        if (bannerUrl.isEmpty()) {
            Picasso.get().load(defaultImage).into(bannerImage)
        } else {
            Picasso.get().load(bannerUrl).into(bannerImage)
        }
        val placeName = fromPlaceId.place.name
        val placeLon = fromPlaceId.place.lon
        val placeLat = fromPlaceId.place.lat

        holder.customView.button_google_map.setOnClickListener {
            println("Hello From navigation Link")
            mapScreen(holder.customView, placeName, placeLon, placeLat)
        }

        holder.fromPlaceId = fromPlaceId
    }

    private fun mapScreen(view: View, name: String, lon: Double, lat: Double) {

        val PLACE_NAME_KEY = "PLACE_NAME"
        val PLACE_LON_KEY = "PLACE_LON"
        val PLACE_LAT_KEY = "PLACE_LAT"

        val intent = Intent(view.context, MapsActivity::class.java)
        intent.putExtra(PLACE_NAME_KEY, name)
        intent.putExtra(PLACE_LON_KEY, lon)
        intent.putExtra(PLACE_LAT_KEY, lat)
        view.context.startActivity(intent)
    }

    class PlaceInfoViewHolder(val customView: View, var fromPlaceId: FromPlaceId? = null) :
        RecyclerView.ViewHolder(customView) {

        companion object {
            const val PLACE_ID_KEY = "PLACE_ID"
            const val PLACE_LAT = "PLACE_LAT"
            const val PLACE_LON = "PLACE_LON"
        }
    }
}
