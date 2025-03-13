package com.example.whoisonmywifi.ui.screens

import androidx.compose.material3.NavigationRail
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation(viewModel: WebPAViewModel){
    val navController = rememberNavController()
    NavHost(navController,
        startDestination = "router_list"){
        composable("router_list"){
            RouterListScreen(viewModel,navController)
        }
        composable("devices/{router}") { backStackEntry ->
            val routerName = backStackEntry.arguments?.getString("router") ?: ""
            ConnectedDevicesScreen(viewModel, routerName, navController)
        }
        composable("routerDetails/{router}") { backStackEntry ->
            val routerName = backStackEntry.arguments?.getString("router") ?: ""
            RouterDetailsScreen(viewModel, routerName,navController)
        }


        }
    }
