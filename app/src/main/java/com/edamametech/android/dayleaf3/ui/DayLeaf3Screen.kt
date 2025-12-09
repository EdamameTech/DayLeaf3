import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.edamametech.android.dayleaf3.ui.theme.DayLeaf3Theme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun noteDateString(noteDate: LocalDate) : String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd E", Locale.US)
    return noteDate.format(formatter)
}

@Composable
fun DayLeaf3Screen(noteDate: LocalDate) {
    var noteText by rememberSaveable { mutableStateOf("") }
    fun updateNote(text: String) {
        noteText = text
    }

    Column{
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            Text(
                noteDateString(noteDate),
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.StartEllipsis,
                modifier = Modifier
                    .padding(8.dp)
            )
        }
        BasicTextField(
            value = noteText,
            onValueChange = { updateNote(it ) },
            textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(8.dp)
                .background(color = MaterialTheme.colorScheme.surface)
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
