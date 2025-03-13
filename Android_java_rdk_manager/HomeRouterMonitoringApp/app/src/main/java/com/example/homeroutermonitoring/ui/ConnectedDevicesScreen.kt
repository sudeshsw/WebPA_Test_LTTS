import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource

@Composable
fun ConnectedDevicesScreen(viewModel: RouterViewModel, routerName: String, navController: NavController) {
    val devices = viewModel.getDevicesForRouter(routerName)
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Connected Devices - $routerName", style = MaterialTheme.typography.headlineSmall)

        if (errorMessage != null) {
            Text(
                text = errorMessage ?: "",
                color = Color.Red,
                modifier = Modifier.padding(8.dp)
            )
        }

        if (isRefreshing) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (devices.isEmpty()) {
            Text("No devices found", modifier = Modifier.padding(8.dp))
        } else {
            devices.forEach { device ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { navController.navigate("device_details/$device") },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.LightGray)
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_menu_info_details),
                            contentDescription = "Device Icon",
                            tint = Color.DarkGray
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(device, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}