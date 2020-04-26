package academy.learnprogramming.placefinder.GoogleMap


import academy.learnprogramming.placefinder.PlacesDetail.PlaceDetailAdapter
import academy.learnprogramming.placefinder.R
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Transformations.map
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private lateinit var lastLocation: Location

    private lateinit var map: GoogleMap

    private lateinit var fusedLocationClient: FusedLocationProviderClient


    private fun setUpMap() {

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

    }


    override fun onMapReady(googleMap: GoogleMap) {
        val placeId =
            intent.getLongExtra(PlaceDetailAdapter.PlaceInfoViewHolder.PLACE_ID_KEY, 1)

        val latitude =
            intent.getDoubleExtra(PlaceDetailAdapter.PlaceInfoViewHolder.PLACE_LAT, 1.0)
        val longitude =
            intent.getDoubleExtra(PlaceDetailAdapter.PlaceInfoViewHolder.PLACE_LON, 1.0)

        println(placeId)
        println(latitude)
        println(longitude)
        map = googleMap


        val myPlace = LatLng(latitude, longitude)
        map.addMarker(MarkerOptions().position(myPlace).title("My Favorite City/Dummy"))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(myPlace, 16.0f))

        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMarkerClickListener(this)

        setUpMap()

    }

    override fun onMarkerClick(p0: Marker?) = false

}