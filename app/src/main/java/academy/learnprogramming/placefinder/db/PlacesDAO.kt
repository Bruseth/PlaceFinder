package academy.learnprogramming.placefinder.db

import androidx.lifecycle.LiveData
import androidx.room.*




@Dao
interface PlacesDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaces(places: PlacesEntity)

    @Query(value = "SELECT * FROM PlacesEntity")
    fun getAllPlaces(): List<PlacesEntity>

    @Query(value = "SELECT * FROM PlacesEntity WHERE placesId LIKE :id")
    fun getPlacesById(id : Long): List<PlacesEntity>

    @Query(value = "SELECT placesId FROM PlacesEntity")
    fun getAllPlacesIds(): List<Long>

    @Update
    fun updatePlace(vararg place: PlacesEntity)

    @Query(value = "UPDATE PlacesEntity SET placesId = :newId WHERE placesId LIKE :id")
    fun updatePlaceWithQuery(id: Long, newId: Long)


}