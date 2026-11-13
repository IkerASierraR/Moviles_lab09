package com.example.locatioandroidsierra.viewmodel
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.CircularBounds
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.SearchNearbyRequest
import timber.log.Timber
class MapViewModel: ViewModel() {
    private val _userLocation = mutableStateOf<LatLng?>( null )
    val userLocation: State<LatLng?> = _userLocation


    val nearbyPlaces = mutableStateListOf<Place>()
    fun fetchNearbyPlaces(context: Context, location: LatLng) {
        val appInfo = context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
        if (!Places.isInitialized()) Places.initializeWithNewPlacesApiEnabled(context, appInfo.metaData.getString("com.google.android.geo.API_KEY")!!)
        val request = SearchNearbyRequest.builder(CircularBounds.newInstance(location, 1000.0), listOf(Place.Field.DISPLAY_NAME, Place.Field.LOCATION)).setMaxResultCount(10).build()
        Places.createClient(context).searchNearby(request)
            .addOnSuccessListener { nearbyPlaces.clear(); nearbyPlaces.addAll(it.places) }
    }



// Función para obtener la ubicación del usuario y actualizar el estado

fun fetchUserLocation (context: Context, fusedLocationClient:
FusedLocationProviderClient ) {

    // Comprobar si se ha concedido el permiso de ubicación
    if (ContextCompat.checkSelfPermission(context,
            android.Manifest.permission.ACCESS_FINE_LOCATION) ==
        PackageManager.PERMISSION_GRANTED) {
            try {
                // Obtener la última ubicación conocida
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    location?.let {
                        // Actualizar la ubicación del usuario en el estado
                        val userLatLng = LatLng(it.latitude, it.longitude)
                        _userLocation.value = userLatLng
                    }
                }
            } catch (e: SecurityException) {
                Timber.e( "Se revocó el permiso de acceso a la ubicación: ${e.localizedMessage}" )
            }
        } else {
            Timber.e( "No se concedió el permiso de ubicación." )
        }
    }
}