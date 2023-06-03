package com.example.natg.io.response

import com.example.natg.io.Feature
import com.google.gson.annotations.SerializedName

data class getEm(
    @SerializedName("persona") val persona:List<String>,
    @SerializedName("automovil") val automovil:List<String>,
    @SerializedName("transporte") val transporte:List<String>
)

