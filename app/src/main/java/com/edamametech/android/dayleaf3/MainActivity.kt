package com.edamametech.android.dayleaf3

import DayLeaf3Screen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.edamametech.android.dayleaf3.ui.theme.DayLeaf3Theme
import com.edamametech.android.dayleaf3.ui.NoteViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DayLeaf3Theme {
                enableEdgeToEdge(
                    statusBarStyle = if (!isSystemInDarkTheme()) {
                        SystemBarStyle.light(
                            MaterialTheme.colorScheme.primaryContainer.toArgb(),
                            MaterialTheme.colorScheme.primaryContainer.toArgb()
                        )
                    } else {
                        SystemBarStyle.dark(
                            MaterialTheme.colorScheme.primaryContainer.toArgb()
                        )
                    }
                )
                Surface(
                    modifier = Modifier
                        .safeDrawingPadding()
                        .fillMaxSize(),
                ) {
                    DayLeaf3Screen()
                }
            }
        }
    }
}
