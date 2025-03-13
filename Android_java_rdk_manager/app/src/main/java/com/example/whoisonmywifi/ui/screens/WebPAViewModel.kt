package com.example.whoisonmywifi.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whoisonmywifi.model.ConnectedDevices
import com.example.whoisonmywifi.model.Device
import com.example.whoisonmywifi.model.DevicesResponse
import com.example.whoisonmywifi.model.RouterDetails
import com.example.whoisonmywifi.model.UpdateStatus
import com.example.whoisonmywifi.network.TOKEN
import com.example.whoisonmywifi.network.WEBPA_URL
import com.example.whoisonmywifi.network.WEPBA_DEVICES_URL
import com.example.whoisonmywifi.network.WebPAAPI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class WebPAViewModel:ViewModel(){

    private val _routers = MutableStateFlow<DevicesResponse>(DevicesResponse(
        emptyList()
    ))
    val routers:StateFlow<DevicesResponse> =_routers

    private val _connectedDevices = MutableStateFlow<ConnectedDevices>(
        ConnectedDevices(emptyList())
    )

    val connectedDevices:StateFlow<ConnectedDevices> = _connectedDevices

    private val _routerDetails = MutableStateFlow<RouterDetails>(
        RouterDetails(emptyList())
    )

    val routerDetails:StateFlow<RouterDetails> = _routerDetails

    private val _updateResult=MutableStateFlow<UpdateStatus>(
        UpdateStatus(emptyList())
    )
    val updateResult:StateFlow<UpdateStatus> =_updateResult

    private var _isUpdating= MutableStateFlow(false)

    private var _isRefreshing= MutableStateFlow(false)

    val isRefreshing:StateFlow<Boolean> = _isRefreshing

    init {
        refreshData()
    }

    fun refreshData()
    {

        getRouterDetails()
    }

    fun getRouterDetails(){

        viewModelScope.launch {

            try{
                println("calling web service")

                _isRefreshing.value = true
                val devicesResponse = WebPAAPI.retrofitService.getDevices(
                    WEPBA_DEVICES_URL,"Basic "+ TOKEN
                )
                _routers.value = devicesResponse
                Log.d("fff",DevicesResponse.toString())


            } catch(e:IOException) {
                println("error in webservice"+e.message)

            }catch(e:HttpException){
                println("HTTP Exception in web service"+e.message)
            }finally {
                _isRefreshing.value = false
            }

        }

    }

    fun getConnectedDevices(routerMac:String){

        viewModelScope.launch {

            try{
                println("calling web service for connected device")

                _isRefreshing.value = true
                val url = WEBPA_URL+"device/"+routerMac+"/config"
                val connecteDevices = WebPAAPI.retrofitService.getConnectedDevices(
                    url,"Basic "+ TOKEN,"Device.Hosts.Host."
                )
                _connectedDevices.value =connecteDevices
                Log.d("fff",connecteDevices.toString())


            } catch(e:IOException) {
                println("error in webservice"+e.message)

            }catch(e:HttpException){
                println("HTTP Exception in web service"+e.message)
            }finally {
                _isRefreshing.value = false
            }

        }

    }

    fun getDetailsForRouter(routerMac: String)
    {

        viewModelScope.launch {

            try{
                println("calling web service for routerdetails")

                _isRefreshing.value = true
                val url = WEBPA_URL+"device/"+routerMac+"/config"
                val routerDetails = WebPAAPI.retrofitService.getDetailsForRouter(
                    url,"Basic "+ TOKEN,"Device.WiFi.SSID.10001.SSID,Device.DeviceInfo.Manufacturer,Device.DeviceInfo.ModelName,Device.DeviceInfo.X_RDK_FirmwareName"
                )
                _routerDetails.value =routerDetails
                Log.d("fff",routerDetails.toString())


            } catch(e:IOException) {
                println("error in webservice"+e.message)

            }catch(e:HttpException){
                println("HTTP Exception in web service"+e.message)
            }finally {
                _isRefreshing.value = false
            }

        }



    }

    fun updateSSID(routerDetails:RouterDetails,routerMac:String)
    {
        viewModelScope.launch {
            try{
                _isRefreshing.value = true
                var url = WEBPA_URL+"device/"+routerMac+"/config?names"
                val updateResult = WebPAAPI.retrofitService.updateSSID(
                    url,"Basic "+ TOKEN,routerDetails
                )
                _updateResult.value=updateResult



            }catch(e:IOException){
                println("error in webservice"+e.message)
            }catch(e:HttpException) {
                println("HTTP Exception in update web service" + e.message)
            }finally {
                _isRefreshing.value = false
            }
        }
    }

}