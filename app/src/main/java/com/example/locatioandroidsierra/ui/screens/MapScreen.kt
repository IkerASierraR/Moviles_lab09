package com.example.locatioandroidsierra.ui.screens
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.example.locatioandroidsierra.viewmodel.MapViewModel
import timber.log.Timber
@Composable
fun MapScreen(mapViewModel: MapViewModel){

    // Inicializa el estado de posición de la cámara, que controla la posición de la cámara en el mapa
    val cameraPositionState = rememberCameraPositionState()
    // Obtiene el contexto actual
    val context = LocalContext.current
    // Observa la ubicación del usuario desde el ViewModel
    val userLocation by mapViewModel.userLocation
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context) }
    // Manejar las solicitudes de permiso para acceder a la ubicación precisa
    val permissionLauncher = rememberLauncherForActivityResult (
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Obtener la ubicación del usuario y actualizar la cámara si se otorga el permiso
            mapViewModel. fetchUserLocation (context, fusedLocationClient)
        } else {
            // Manejar el caso en que se deniega el permiso
            Timber. e ( "El usuario denegó el permiso de ubicación." )
        }
    }
    // Solicitar el permiso de ubicación cuando se lanza el componible
    LaunchedEffect ( Unit ) {
        when ( PackageManager. PERMISSION_GRANTED ) {
        // Comprobar si el permiso de ubicación ya está otorgado
            ContextCompat. checkSelfPermission (context, android. Manifest . permission .
            ACCESS_FINE_LOCATION ) -> {
                // Obtener la ubicación del usuario y actualizar la cámara
                mapViewModel. fetchUserLocation (context, fusedLocationClient)
            }
            else -> {
                // Solicitar el permiso de ubicación si no se ha concedido
                permissionLauncher. launch (android. Manifest . permission .
                ACCESS_FINE_LOCATION )
            }
        }
    }


    LaunchedEffect(userLocation) { userLocation?.let { mapViewModel.fetchNearbyPlaces(context, it) } }


    // Muestra el mapa de Google
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {


        mapViewModel.nearbyPlaces.forEach { Marker(state = rememberMarkerState(position = it.location!!), title = it.displayName) }



        // Si la ubicación del usuario está disponible, colocar un marcador en el mapa
        userLocation?.let {
            Marker(
                state = rememberMarkerState(position = it), // Colocar el marcador en la ubicación del usuario
                title = "Tu ubicación", // Establecer el título para el marcador
                snippet = "Aquí es donde te encuentras actualmente." // Establecer el snippet para el marcador
            )
            // Mover la cámara a la ubicación del usuario con un nivel de zoom de 10f
            cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 10f)
        }
    }
}