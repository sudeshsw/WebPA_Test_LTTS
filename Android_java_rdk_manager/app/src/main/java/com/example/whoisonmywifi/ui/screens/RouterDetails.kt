package com.example.whoisonmywifi.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.whoisonmywifi.model.ConnectedDevices
import com.example.whoisonmywifi.model.Parameter
import com.example.whoisonmywifi.model.ParameterForUI
import com.example.whoisonmywifi.model.RouterDetails
import com.example.whoisonmywifi.model.RouterDetailsForUI


fun getRouterDetailsForUI(paramList:List<Parameter>):RouterDetailsForUI
{
    var routerDetailsForUI:RouterDetailsForUI=RouterDetailsForUI("","","","")
    for (param in paramList){
        if (param.name.contains("ModelName",true)){
            routerDetailsForUI.ModelName = param.value
        }else if(param.name.contains("X_RDK_FirmwareName",true)){
            routerDetailsForUI.SoftwareVersion=param.value
        }else if(param.name.equals("Device.DeviceInfo.Manufacturer",true)){
            routerDetailsForUI.Manufacturer=param.value
        }else if(param.name.equals("Device.WiFi.SSID.10001.SSID",true)){
            routerDetailsForUI.SSID=param.value
        }
    }
    return routerDetailsForUI
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouterDetailsScreen(
    viewModel: WebPAViewModel,
    routerName: String,
    navController: NavController
){
    val routerDetails by viewModel.routerDetails.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()



    LaunchedEffect(routerName) {
        if(routerName != null)
        {
            viewModel.getDetailsForRouter(routerName)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ){

        TopAppBar(
            title = { Text("Gateway Details") }

        )

        if (isRefreshing) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
        }
        if(routerDetails.parameters.isNotEmpty()) {
            val routerDetailsForUI = getRouterDetailsForUI(routerDetails.parameters)
            DetailRow(label = "Model Name",routerDetailsForUI.ModelName)
            DetailRow(label = "Manufacturer",routerDetailsForUI.Manufacturer)
            DetailRow(label = "Software Version",routerDetailsForUI.SoftwareVersion)
            EditableField(label="SSID",routerDetailsForUI.SSID) {
                newValue->routerDetailsForUI.SSID=newValue
                var routerDetailsToUpdate:RouterDetails = RouterDetails(emptyList())
                var paramList = arrayListOf<Parameter>()
                val param:Parameter=Parameter("","",0)
                param.name ="Device.WiFi.SSID.10001.SSID"
                param.value= newValue
                param.dataType=0

                paramList.add(param)
                routerDetailsToUpdate.parameters = paramList.toList()
                viewModel.updateSSID(routerDetailsToUpdate,routerName)


            }

            Button(onClick=
            {navController.popBackStack() }) {
                Text("Back")
            }





        }
    }




}

@Composable
fun DetailRow(label:String,value:String){
    Row(modifier= Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
        ){
        Text("$label:", fontWeight = FontWeight.Bold)
        Text(value)
    }
}

@Composable
fun EditableField(label:String,value:String,
                  onUpdate:( String)->Unit)
{
    var text by remember{ mutableStateOf(value) }
    Row(
        modifier =Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    )
    {
        Column(modifier = Modifier.weight(1f)){
            Text("$label:", fontWeight = FontWeight.Bold)
            TextField(value = text,
                onValueChange={ text=it},
                singleLine = true,
                modifier= Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )
        }
        Button(onClick = {onUpdate(text)}){

            Text("Update")
        }

    }

}


