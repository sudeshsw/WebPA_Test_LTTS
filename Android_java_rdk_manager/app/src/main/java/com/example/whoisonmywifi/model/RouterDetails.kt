package com.example.whoisonmywifi.model

import kotlinx.serialization.Serializable

class RouterDetailsForUI(
    var  Manufacturer:String,
    var  ModelName:String,
    var SoftwareVersion:String,
    var SSID:String

)

@Serializable
data class RouterDetails(
    var parameters:List<Parameter>
)

@Serializable
data class UpdateParam(
    var name:String,
    var message:String
)

@Serializable
data class UpdateStatus(
    var parameters:List<UpdateParam>
)