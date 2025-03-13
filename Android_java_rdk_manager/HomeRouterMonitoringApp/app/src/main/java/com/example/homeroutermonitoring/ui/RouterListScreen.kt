import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource

@Composable
fun RouterListScreen(viewModel: RouterViewModel, navController: NavController) {
    val routers by viewModel.routers.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = { viewModel.refreshData() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            TopAppBar(
                title = { Text("Home Routers") },
                actions = {
                    IconButton(onClick = { viewModel.refreshData() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )

            if (errorMessage != null) {
                Text(
                    text = errorMessage ?: "",
                    color = Color.Red,
                    modifier = Modifier.padding(8.dp)
                )
            }

            if (isRefreshing) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else if (routers.isEmpty()) {
                Text("No routers found", modifier = Modifier.padding(8.dp))
            } else {
                routers.forEach { router ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable { navController.navigate("devices/$router") },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.LightGray)
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = android.R.drawable.ic_menu_manage),
                                contentDescription = "Router Icon",
                                tint = Color.DarkGray
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(router, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }
        }
    }
}