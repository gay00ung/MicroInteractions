package com.microinteractions.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.microinteractions.MicroInteractions
import com.microinteractions.core.MicroInteraction
import com.microinteractions.example.ui.theme.MicroInteractionsTheme
import com.microinteractions.extensions.*
import com.microinteractions.themes.DefaultTheme
import com.microinteractions.themes.EnergeticTheme
import com.microinteractions.themes.SubtleTheme

class ComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MicroInteractionsTheme {
                ComposeExamplesScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposeExamplesScreen() {
    var showSuccess by remember { mutableStateOf(false) }
    var showFailure by remember { mutableStateOf(false) }
    var isToggled by remember { mutableStateOf(false) }
    var selectedTheme by remember { mutableStateOf("Default") }
    
    LaunchedEffect(selectedTheme) {
        when (selectedTheme) {
            "Default" -> MicroInteractions.applyTheme(DefaultTheme())
            "Subtle" -> MicroInteractions.applyTheme(SubtleTheme())
            "Energetic" -> MicroInteractions.applyTheme(EnergeticTheme())
        }
    }
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Compose Examples",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            // Theme Selector
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Theme",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = selectedTheme == "Default",
                            onClick = { selectedTheme = "Default" },
                            label = { Text("Default") },
                            modifier = Modifier.tapInteraction()
                        )
                        FilterChip(
                            selected = selectedTheme == "Subtle",
                            onClick = { selectedTheme = "Subtle" },
                            label = { Text("Subtle") },
                            modifier = Modifier.tapInteraction()
                        )
                        FilterChip(
                            selected = selectedTheme == "Energetic",
                            onClick = { selectedTheme = "Energetic" },
                            label = { Text("Energetic") },
                            modifier = Modifier.tapInteraction()
                        )
                    }
                }
            }
            
            // Basic Interactions
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Basic Interactions",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Button(
                        onClick = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .tapInteraction()
                    ) {
                        Text("Tap Interaction")
                    }
                    
                    Button(
                        onClick = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .microInteraction(MicroInteraction.LongPress, ComposeTrigger.OnLongPress)
                    ) {
                        Text("Long Press Interaction")
                    }
                }
            }
            
            // State Interactions
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "State Interactions",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { showSuccess = true },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Success")
                        }
                        
                        Button(
                            onClick = { showFailure = true },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Failure")
                        }
                    }
                    
                    // Success trigger
                    MicroInteractionTrigger(
                        interaction = MicroInteraction.Success,
                        trigger = showSuccess
                    ) {
                        LaunchedEffect(showSuccess) {
                            if (showSuccess) {
                                showSuccess = false
                            }
                        }
                    }
                    
                    // Failure trigger
                    MicroInteractionTrigger(
                        interaction = MicroInteraction.Failure,
                        trigger = showFailure
                    ) {
                        LaunchedEffect(showFailure) {
                            if (showFailure) {
                                showFailure = false
                            }
                        }
                    }
                }
            }
            
            // Toggle Interaction
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Toggle Feature",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Switch(
                        checked = isToggled,
                        onCheckedChange = { isToggled = it },
                        modifier = Modifier.toggleInteraction()
                    )
                }
            }
            
            // Action Interactions
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Action Interactions",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = { },
                            modifier = Modifier.microInteraction(MicroInteraction.Refresh)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Refresh"
                            )
                        }
                        
                        IconButton(
                            onClick = { },
                            modifier = Modifier.microInteraction(MicroInteraction.Delete)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete"
                            )
                        }
                        
                        IconButton(
                            onClick = { },
                            modifier = Modifier.microInteraction(MicroInteraction.Favorite)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Favorite"
                            )
                        }
                        
                        IconButton(
                            onClick = { },
                            modifier = Modifier.microInteraction(MicroInteraction.Share)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share"
                            )
                        }
                    }
                }
            }
            
            // Presets
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Presets",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { MicroInteractions.Presets.applySilent() },
                            modifier = Modifier
                                .weight(1f)
                                .tapInteraction()
                        ) {
                            Text("Silent")
                        }
                        
                        Button(
                            onClick = { MicroInteractions.Presets.applyBatterySaving() },
                            modifier = Modifier
                                .weight(1f)
                                .tapInteraction()
                        ) {
                            Text("Battery")
                        }
                    }
                }
            }
        }
    }
}

// Missing imports
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*