package com.hcapps.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.hcapps.ui.R
import io.realm.kotlin.mongodb.User
import io.realm.kotlin.mongodb.ext.profileAsBsonDocument

data class NavigationItem(
    val icon: ImageVector,
    val title: String,
    val subtitle: String? = null,
    val iconDescription: String? = null,
    val onClick: () -> Unit = {},
    val selected: Boolean = false
)

@Composable
fun NavigationDrawer(
    drawerState: DrawerState,
    onSignOutClicked: () -> Unit,
    onDeleteAllClicked: () -> Unit,
    currentUser: User?,
    content: @Composable () -> Unit
) {

    val navigationItem = listOf(
        NavigationItem(
            icon = Icons.Rounded.Logout,
            title = "Sign out",
            iconDescription = "Google icon",
            onClick = onSignOutClicked
        ),
        NavigationItem(
            icon = Icons.Rounded.Delete,
            title = "Delete All Journals",
            iconDescription = "delete all",
            onClick = onDeleteAllClicked
        ),
        NavigationItem(
            icon = Icons.Rounded.Info,
            title = "About",
            subtitle = "Version 1.0",
            iconDescription = "about",
            onClick = {}
        )
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            val profileBson = currentUser?.profileAsBsonDocument()
            ModalDrawerSheet(modifier = Modifier.fillMaxWidth(0.8f)) {
                Text(
                    text = "Journal",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    letterSpacing = TextUnit(0.5f, TextUnitType.Sp)
                )
                Image(
                    painter = painterResource(id = R.drawable.navigation_drawer_illustration),
                    contentDescription = "App Logo"
                )
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AsyncImage(model = profileBson?.getString("picture")?.value, contentDescription = "profile picture", modifier = Modifier.clip(CircleShape))
                    Column() {
                        Text(text = profileBson?.getString("name")?.value ?: "", style = MaterialTheme.typography.titleMedium)
                        Text(text = profileBson?.getString("email")?.value ?: "", style = MaterialTheme.typography.bodyMedium)
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                navigationItem.forEach { item ->
                    NavigationDrawerItem(
                        label = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = item.icon, contentDescription = item.iconDescription)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = item.title,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    item.subtitle?.let {
                                        Text(text = it, style = MaterialTheme.typography.labelSmall)
                                    }
                                }
                            }
                        },
                        selected = item.selected,
                        onClick = item.onClick
                    )
                }
            }
        },
        content = content
    )
}