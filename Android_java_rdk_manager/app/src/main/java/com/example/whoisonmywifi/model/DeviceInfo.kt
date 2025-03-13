package com.example.whoisonmywifi.model

import kotlinx.serialization.Serializable




@Serializable
data class Parameter(
    var name:String,
    var value:String,
    var dataType:Int
)

class ParameterForUI(
    var HostName:String,
    var IPAddress:String,
    var Activestate:String,
    var SSID:String

)


@Serializable
data class DeviceList(
    var name:String,
    var value:List<Parameter>,
    var dataType:Int,
    var parameterCount:Int,
    var message:String
)


@Serializable
data class ConnectedDevices (
    var parameters:List<DeviceList>

)
