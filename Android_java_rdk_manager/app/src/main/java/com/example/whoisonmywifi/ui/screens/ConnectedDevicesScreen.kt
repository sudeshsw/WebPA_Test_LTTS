package com.example.whoisonmywifi.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Button

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.rounded.Home

import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

import androidx.navigation.NavController
import com.example.whoisonmywifi.model.Parameter
import com.example.whoisonmywifi.model.ParameterForUI


fun getDeviceForUI(paramList:List<Parameter>): ArrayList<ParameterForUI> {
    var paramForUIList=arrayListOf<ParameterForUI>()
    var hostNameFound = false
    var paramForUI:ParameterForUI=ParameterForUI("","","","")
    for(param in paramList){

        if(param.name.contains("HostName",true))
        {
            if(hostNameFound) {
                paramForUI.HostName = param.value
                Log.d("dff", "host name found" + param.value + param.name)
            }

        }else if (param.name.contains( "IPAddress")){


                hostNameFound=true
                paramForUI = ParameterForUI("","","","")
                paramForUI.IPAddress = param.value
                Log.d("dff","IP Address found"+param.value+":"+param.name)

        } else if (param.name.contains("Layer1Interface")){
            if(hostNameFound) {
                paramForUI.SSID = param.value
                Log.d("dff","SSID found"+param.value+":"+param.name)
            }
        } else if (param.name.contains("Active")){
            if(hostNameFound) {
                paramForUI.Activestate = param.value
                paramForUIList.add(paramForUI)
                hostNameFound=false
                paramForUI = ParameterForUI("","","","")
                Log.d("dff","active found"+param.value+":"+param.name)
            }
        }

    }
    return paramForUIList


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectedDevicesScreen(
    viewModel: WebPAViewModel,
    routerName: String,
    navController: NavController
)
{


    val connectedDevices by viewModel.connectedDevices.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()


    var uilist:ArrayList<ParameterForUI>

    LaunchedEffect(routerName) {
        viewModel.getConnectedDevices(routerName)
    }


    Column(modifier = Modifier.padding(16.dp)) {

        TopAppBar(
            title = { Text("Connected Devices") },
            actions = {
                Button(onClick = { viewModel.getConnectedDevices(routerName) }) {
                    Text("Refresh")
                }
            }
        )

        if (isRefreshing) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (connectedDevices.parameters.isNotEmpty()) {

            uilist = getDeviceForUI(connectedDevices.parameters[0].value)

            LazyColumn(
                modifier = Modifier.fillMaxSize(),


            ) {


                //implement logic if no devices found
                val listUI = uilist.toList()
                items( listUI)
                {item->


                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            ,
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Row(
                            modifier =
                            Modifier.padding(8.dp).fillMaxWidth(),

                        ) {

                            Log.d("ff","host name in ui"+item.HostName)
                            Text("Device Name:  "+ item.HostName +" Active: "+item.Activestate,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyLarge)


                        }
                        Row(modifier =
                        Modifier.padding(8.dp).fillMaxWidth())
                        {

                            Text(" IP Address:"+item.IPAddress+" Layer1Interface :"+item.SSID,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyLarge)

                        }

                    }
                }



            }
        }
    }
}