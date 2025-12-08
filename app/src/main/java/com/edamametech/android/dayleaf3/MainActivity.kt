package com.edamametech.android.dayleaf3

import DayLeaf3Screen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.edamametech.android.dayleaf3.ui.theme.DayLeaf3Theme
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val noteDate = LocalDate.now()

        setContent {
            DayLeaf3Theme {
                Surface(color = MaterialTheme.colorScheme.background) {
                  Surface(
                    modifier = Modifier.safeDrawingPadding().fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                  ) {
                    DayLeaf3Screen(noteDate)
                  }
                }
            }
        }
    }
}
