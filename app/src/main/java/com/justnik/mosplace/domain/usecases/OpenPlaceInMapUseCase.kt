package com.justnik.mosplace.domain.usecases

import android.content.Context
import android.content.Intent
import android.net.Uri

class OpenPlaceInMapUseCase(private val context: Context){
    operator fun invoke(placeName: String){
        val uri = Uri.parse("geo:0,0?q=$placeName")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}