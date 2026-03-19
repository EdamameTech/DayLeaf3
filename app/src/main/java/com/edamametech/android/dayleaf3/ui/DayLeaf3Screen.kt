import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.edamametech.android.dayleaf3.ui.theme.DayLeaf3Theme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun noteDateString(noteDate: LocalDate): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd E", Locale.US)
    return noteDate.format(formatter)
}

@Composable
fun DayLeaf3Screen() {
    /* Mock notes to be replaced by database */
    var allNotes = hashMapOf<LocalDate, String>(
        LocalDate.of(2026, 3, 14) to "It is pie day!",
        LocalDate.of(2026, 3, 17) to "It is Saint Pattick's Day!",
        LocalDate.now() to "Hello, World! It is today!"
    )
    var allDates = allNotes.keys.sorted()

    /* UI state */
    var noteDate by rememberSaveable { mutableStateOf(LocalDate.now()) }
    var noteText by rememberSaveable { mutableStateOf(allNotes[noteDate] ?: "") }
    var isToday = noteDate.isEqual(LocalDate.now())
    var isFirstDate = noteDate.isEqual(allDates[0])

    fun updateNote(text: String) {
        noteText = text
    }

    fun setDate(date: LocalDate) {
        noteDate = date
        noteText = allNotes[date] ?: ""
    }

    fun offsetDate(offset: Int) {
        var t = allDates.binarySearch(noteDate) + offset
        if (t < 0) {
            t = 0
        }
        if (t >= allDates.size) {
            t = allDates.size - 1
        }
        setDate(allDates[t])
    }

    Column {
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
                    .weight(1F)
                    .padding(8.dp)
            )
            /* Export */
            OutlinedButton(
                enabled = false,
                onClick = { /* TODO */ },
                content = {
                    Text("^")
                },
                shape = RoundedCornerShape(4.dp)
            )
            /* Previous day */
            OutlinedButton(
                enabled = !isFirstDate,
                onClick = { offsetDate(-1) },
                content = {
                    Text("<")
                },
                shape = RoundedCornerShape(4.dp)
            )
            /* Next day */
            OutlinedButton(
                enabled = !isToday,
                onClick = { offsetDate(1) },
                content = {
                    Text(">")
                },
                shape = RoundedCornerShape(4.dp)
            )
            // Today
            OutlinedButton(
                enabled = !isToday,
                onClick = { setDate(LocalDate.now()) },
                content = {
                    Text(">>")
                },
                shape = RoundedCornerShape(4.dp)
            )
        }
        BasicTextField(
            value = noteText,
            onValueChange = { updateNote(it) },
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
    DayLeaf3Theme {
        DayLeaf3Screen()
    }
}
