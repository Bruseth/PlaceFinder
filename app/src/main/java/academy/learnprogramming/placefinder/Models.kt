package academy.learnprogramming.placefinder

data class Places(val features: List<Feature>)

data class Feature(val type: String, val properties: Properties, val geometry: Geometry)

data class Geometry(val type: String, val coordinates: ArrayList<Double>)

data class Properties(val name: String, val icon: String, val id: Long)


data class FromPlaceId(val place: Place)

data class Place( val id: Long, val name: String, val comments: String, val banner: String, val lat: Double, val lon: Double)