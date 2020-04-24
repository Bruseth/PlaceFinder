package academy.learnprogramming.placefinder

import academy.learnprogramming.placefinder.db.PlacesDatabase
import academy.learnprogramming.placefinder.db.PlacesEntity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private var adapter: MainAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val database =
            Room.databaseBuilder(applicationContext, PlacesDatabase::class.java, "places.db")
                .allowMainThreadQueries().build()

        val listOfLocations = database.placesDAO().getAllPlaces()

        recyclerView_main.layoutManager = LinearLayoutManager(this)

        loadLocations(listOfLocations)
    }

    private fun loadLocations(listOfLocations: List<PlacesEntity>) {
        runOnUiThread {
            adapter = MainAdapter(listOfLocations as MutableList<PlacesEntity>)
            recyclerView_main.adapter = adapter
            adapter!!.notifyDataSetChanged()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.main_search, menu)

        val searchItem: MenuItem = menu.findItem(R.id.app_bar_search)
        val searchView: SearchView = searchItem.actionView as SearchView

        searchView.imeOptions = EditorInfo.IME_ACTION_DONE

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter?.filter?.filter(newText)
                return false
            }
        })
        return true
    }


}

