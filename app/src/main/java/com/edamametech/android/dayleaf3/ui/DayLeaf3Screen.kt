import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.edamametech.android.dayleaf3.ui.NoteViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun noteDateString(noteDate: LocalDate): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd E", Locale.US)
    return noteDate.format(formatter)
}

@Composable
fun DayLeaf3Screen(
    viewModel: NoteViewModel
) {
    val uiState = viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    val dateString = if (uiState.value.date == null) {
        "↻"
    } else {
        noteDateString(uiState.value.date!!)
    }

    Column {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            Text(
                dateString,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.StartEllipsis,
                modifier = Modifier
                    .weight(1F)
                    .padding(8.dp)
            )
            /* Export */
            OutlinedButton(
                enabled = uiState.value.anyExportable || uiState.value.isEdited,
                onClick = { /* TODO */ },
                content = {
                    Text("↓")
                },
                shape = RoundedCornerShape(4.dp)
            )
            /* Previous day */
            OutlinedButton(
                enabled = uiState.value.previousDate != null,
                onClick = {
                    if (uiState.value.previousDate != null) {
                        coroutineScope.launch(Dispatchers.IO) {
                            viewModel.saveAndSetDate(uiState.value.previousDate!!)
                        }
                    }
                },
                content = {
                    Text("<")
                },
                shape = RoundedCornerShape(4.dp)
            )
            /* Next day */
            OutlinedButton(
                enabled = uiState.value.nextDate != null,
                onClick = {
                    if (uiState.value.nextDate != null) {
                        coroutineScope.launch(Dispatchers.IO) {
                            viewModel.saveAndSetDate(uiState.value.nextDate!!)
                        }
                    }
                },
                content = {
                    Text(">")
                },
                shape = RoundedCornerShape(4.dp)
            )
            // Today
            OutlinedButton(
                onClick = {
                    coroutineScope.launch(Dispatchers.IO) {
                        viewModel.saveAndSetDate(LocalDate.now())
                    }
                },
                content = {
                    Text(">>")
                },
                shape = RoundedCornerShape(4.dp)
            )
        }
        BasicTextField(
            enabled = uiState.value.date != null,
            value = uiState.value.text,
            onValueChange = { viewModel.updateNote(it) },
            textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(8.dp)
                .background(color = MaterialTheme.colorScheme.surface)
        )
    }
}
