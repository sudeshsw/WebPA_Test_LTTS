import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp

@Composable
fun DeviceDetailsScreen(deviceName: String) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Device Details", style = MaterialTheme.typography.headlineSmall)
        Text("Name: $deviceName", style = MaterialTheme.typography.bodyLarge)
        Text("IP Address: 192.168.1.100", style = MaterialTheme.typography.bodyMedium)
        Text("MAC Address: AA:BB:CC:DD:EE:FF", style = MaterialTheme.typography.bodyMedium)
        Text("Connection: WiFi", style = MaterialTheme.typography.bodyMedium)
    }
}