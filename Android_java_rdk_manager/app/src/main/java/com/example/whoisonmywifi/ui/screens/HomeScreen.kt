package com.example.whoisonmywifi.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Label
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.whoisonmywifi.ui.theme.WhoIsOnMyWifiTheme
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.compose.material3.pulltorefresh.*
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouterListScreen(viewModel: WebPAViewModel, navController: NavController) {
    val routers by viewModel.routers.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()



    Column(modifier = Modifier.padding(16.dp))
    {
        TopAppBar(
            title = { Text("Home Routers") },
            actions = {
                Button(onClick = { viewModel.refreshData() }) {
                    Text("Refresh")
                }
            }
        )

     /*   Text("Gateways", style = MaterialTheme.typography.headlineSmall)
        Button(onClick =
        { viewModel.refreshData() }
        ) {
            Text("Refresh")
        }

      */
        if (isRefreshing) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
        }
        if (routers.devices.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){
                items(routers.devices){device->
                    Card(
                        modifier = Modifier.fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable { navController.navigate("devices/${device.id}") }
                        ,
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors= CardDefaults.cardColors(containerColor =
                            MaterialTheme.colorScheme.surface
                        )
                    ){
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_menu_manage),
                            contentDescription = "Device Icon",
                            tint = Color.DarkGray
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Home Router",
                            fontSize = 18.sp,
                            modifier = Modifier.padding(16.dp),
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = device.id,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(16.dp),
                            fontWeight = FontWeight.Bold
                        )
                        Button(onClick = {navController.navigate("routerDetails/${device.id}")}) {Text("Details ") }

                    }

                }

            }

        }else if(!isRefreshing)
        {
            Text("No Routers Found",
                style = MaterialTheme.typography.bodyMedium
                )
        }
    }


}


