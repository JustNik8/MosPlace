package com.justnik.mosplace.domain.usecases

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.justnik.mosplace.domain.entities.Place
import com.justnik.mosplace.domain.getAbbreviationWithName

class OpenPlaceInMapUseCase(private val context: Context){
    operator fun invoke(place: Place){
        val latitude = place.latitude
        val longitude = place.longitude

        val uri = if (latitude == 0.0 && longitude == 0.0){
            Uri.parse("geo:0,0?q=${place.title}")
        } else {
            Uri.parse("geo:$latitude,$longitude")
        }
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        Log.d("Place", "$latitude, $longitude")
        context.startActivity(intent)
    }
}