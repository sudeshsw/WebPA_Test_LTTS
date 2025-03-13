package com.example.whoisonmywifi.model

import kotlinx.serialization.Serializable

@Serializable
data class Device(
    var id:String
)

@Serializable
data class DevicesResponse (
    var devices:List<Device>
)