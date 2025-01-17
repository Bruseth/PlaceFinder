package academy.learnprogramming.placefinder

import academy.learnprogramming.placefinder.GoogleMap.MapsActivity
import academy.learnprogramming.placefinder.PlacesDetail.PlaceDetailActivity
import academy.learnprogramming.placefinder.db.PlacesEntity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filterable
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.name_row.view.*
import kotlinx.android.synthetic.main.place_detail_row.view.*


class MainAdapter(
    private var placeListFull: MutableList<PlacesEntity> = mutableListOf()
    ) : RecyclerView.Adapter<MainAdapter.CustomViewHolder>(), Filterable {

    private var featureListToShow: MutableList<PlacesEntity> = mutableListOf()

    init {
        featureListToShow = placeListFull
    }

    override fun getItemCount(): Int {
        return featureListToShow.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.name_row, parent, false)

        return CustomViewHolder(cellForRow)
    }


    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val feature = featureListToShow[position]

        holder.view.textView_place_name.text = feature.placeName

        val placeName = feature.placeName
        val placeLon = feature.placeLon
        val placeLat = feature.placeLat

        val pointerImage = "https://pngimage.net/wp-content/uploads/2018/06/google-map-pointer-png-7.png"

        val buttonPointer = holder.view.imageView_button_pointer_main
        Picasso.get().load(pointerImage).into(buttonPointer)

        holder.view.imageView_button_pointer_main.setOnClickListener {
            println("Hello From navigation Link")
            mapScreen(holder.view, placeName, placeLon, placeLat)
        }

        holder.placesEntity = feature

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

    override fun getFilter(): Filter {
        return placeFilter
    }

    private val placeFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults? {
            val aFilteredList: MutableList<PlacesEntity> =

            if (constraint == null || constraint.isEmpty()) {
                placeListFull

            } else {
                placeListFull.filter {
                    it.placeName.contains(
                        constraint.toString(),
                        ignoreCase = true
                    )
                } as MutableList<PlacesEntity>

            }

            val result = FilterResults()
            result.values = aFilteredList
            return result
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            results?.values.let {
                featureListToShow = it as MutableList<PlacesEntity>
            }
            notifyDataSetChanged()
        }
    }


    class CustomViewHolder(val view: View, var placesEntity: PlacesEntity? = null) :
        RecyclerView.ViewHolder(view) {

        companion object {
            val PLACE_TITLE_KEY = "PLACE_TITLE"
            val PLACE_ID_KEY = "PLACE_ID"
        }

        init {
            view.setOnClickListener {
                val intent = Intent(view.context, PlaceDetailActivity::class.java)

                intent.putExtra(PLACE_TITLE_KEY, placesEntity?.placeName)
                intent.putExtra(PLACE_ID_KEY, placesEntity?.placesId)

                view.context.startActivity(intent)
            }
        }
    }
}





