package academy.learnprogramming.placefinder

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filterable
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.name_row.view.*
import java.io.FilterReader


class MainAdapter(
    var places: MutableList<Feature> = mutableListOf()
    ) : RecyclerView.Adapter<MainAdapter.CustomViewHolder>(), Filterable {

    private var featureListToShow: MutableList<Feature> = mutableListOf()

    init {
        featureListToShow = Places
    }

    override fun getItemCount(): Int {
        return places.features.size()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.name_row, parent, false)

        return CustomViewHolder(cellForRow)
    }


    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val feature = places.features.get(position)

        holder.view.textView_place_name.text = feature.properties.name
        holder?.feature = feature

    }

    override fun getFilter(): Filter {
        return placeFilter
    }

    private val placeFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults? {
            val aFilteredList: MutableList<Feature> = mutableListOf()

            if (constraint == null || constraint.isEmpty()) {
                places

            } else {
                places.filter {
                    it.places.name.constrain(
                        constraint.toString(),
                        ignoreCase = true
                    )
                } as MutableList<Feature>

            }

            val result = places.features()
            result.values = aFilteredList
            return result
        }

        @Suppress("UNCHEKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            results?.values.let {
                featureListToShow = it as MutableList<Feature>
            }
            notifyDataSetChanged()
        }
    }


    class CustomViewHolder(val view: View, var feature: Feature? = null) :
        RecyclerView.ViewHolder(view) {

        companion object {
            val PLACE_TITLE_KEY = "PLACE_TITLE"
            val PLACE_ID_KEY = "PLACE_ID"
        }

        init {
            view.setOnClickListener {
                val intent = Intent(view.context, PlaceDetailActivity::class.java)

                intent.putExtra(PLACE_TITLE_KEY, feature?.properties?.name)
                intent.putExtra(PLACE_ID_KEY, feature?.properties?.id)

                view.context.startActivity(intent)
            }
        }
    }
}





