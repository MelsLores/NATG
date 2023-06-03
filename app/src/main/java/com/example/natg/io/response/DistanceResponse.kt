package com.example.natg.io.response

import com.example.natg.model.Step
data class DistanceResponse(
    val distance: Int,
    val price:Float,
    val steps:List<Step>,
    val time:String
)
