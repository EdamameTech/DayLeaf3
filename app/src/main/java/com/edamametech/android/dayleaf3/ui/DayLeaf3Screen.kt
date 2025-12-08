import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.edamametech.android.dayleaf3.ui.theme.DayLeaf3Theme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

fun noteDateString(noteDate: LocalDate) : String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd E", Locale.US)
    return noteDate.format(formatter)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayLeaf3Screen(noteDate: LocalDate) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                title = {
                    Text(noteDateString(noteDate))
                }
            )
        }
    ) { innerPadding ->
        Text(
            text = "Hello, World!",
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DayLeaf3ScreenPreview() {
    var timestamp = LocalDate.of(2025,12,6)
    // when this line of code is written
    DayLeaf3Theme {
        DayLeaf3Screen(timestamp)
    }
}
