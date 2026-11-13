package com.example.locatioandroidsierra.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun MiMapa(modifier: Modifier){
    val posicionGuardada = rememberSaveable {
        LatLng(-18.004821689652044, -70.22611719999999)
    }
    val markerState = rememberMarkerState(
        position = posicionGuardada
    )

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(posicionGuardada, 10f)
    }
    val properties by remember{ mutableStateOf(
        MapProperties(
            mapType =
                MapType.SATELLITE
        )
    ) }
    val uiSettings by remember{ mutableStateOf(
        MapUiSettings(
            zoomControlsEnabled = false,
            myLocationButtonEnabled = true
        )
    ) }

    GoogleMap(modifier = Modifier.fillMaxSize(),
        properties = properties,
        cameraPositionState = cameraPositionState,
        uiSettings = uiSettings,
        onMapLongClick = { latLng ->
            println("Long press en: $latLng")
        }) {
        Marker(state = markerState, title = "Mi Universidad")
    }
}