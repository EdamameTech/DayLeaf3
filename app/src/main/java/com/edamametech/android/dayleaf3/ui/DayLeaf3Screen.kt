import androidx.activity.result.ActivityResultLauncher
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.edamametech.android.dayleaf3.ui.NoteViewModel
import com.edamametech.android.dayleaf3.util.exportFileName
import com.edamametech.android.dayleaf3.util.noteDateString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun DayLeaf3Screen(
    viewModel: NoteViewModel, exportNotesActivityLauncher: ActivityResultLauncher<String>
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
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            // Date
            Text(
                dateString,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.StartEllipsis,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .weight(1F)
                    .padding(4.dp)
            )
            // Export
            OutlinedButton(
                enabled = (uiState.value.unexported > 0 || uiState.value.isEdited) && uiState.value.exporting == 0,
                onClick = {
                    exportNotesActivityLauncher.launch(exportFileName(System.currentTimeMillis()))
                },
                content = {
                    Text(
                        "↓",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleSmall
                    )
                },
                shape = RoundedCornerShape(4.dp)
            )
            // Previous day
            OutlinedButton(
                enabled = uiState.value.previousDate != null && uiState.value.exporting == 0,
                onClick = {
                    if (uiState.value.previousDate != null) {
                        coroutineScope.launch(Dispatchers.IO) {
                            viewModel.saveAndSetDate(uiState.value.previousDate!!)
                        }
                    }
                },
                content = {
                    Text(
                        "<",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleSmall
                    )
                },
                shape = RoundedCornerShape(4.dp)
            )
            // Next day
            OutlinedButton(
                enabled = uiState.value.nextDate != null && uiState.value.exporting == 0,
                onClick = {
                    if (uiState.value.nextDate != null) {
                        coroutineScope.launch(Dispatchers.IO) {
                            viewModel.saveAndSetDate(uiState.value.nextDate!!)
                        }
                    }
                },
                content = {
                    Text(
                        ">",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleSmall
                    )
                },
                shape = RoundedCornerShape(4.dp)
            )
            // Today
            OutlinedButton(
                enabled = uiState.value.exporting == 0,
                onClick = {
                    coroutineScope.launch(Dispatchers.IO) {
                        viewModel.saveAndSetDate(LocalDate.now())
                    }
                },
                content = {
                    Text(
                        ">>",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleSmall
                    )
                },
                shape = RoundedCornerShape(4.dp)
            )
        }
        if (uiState.value.exporting > 0) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                Text(
                    String.format(
                        "↻ %d/%d", uiState.value.exporting, uiState.value.unexported
                    ),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                )
            }
        }
        if (uiState.value.exportError != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.errorContainer)
            ) {
                Text(
                    uiState.value.exportError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .weight(1F)
                        .padding(4.dp)
                )
                OutlinedButton(
                    enabled = uiState.value.exportError != null,
                    onClick = {
                        viewModel.dismissError()
                    },
                    content = {
                        Text(
                            "✓",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.titleSmall,
                        )
                    },
                    shape = RoundedCornerShape(4.dp)
                )
            }
        }
        BasicTextField(
            enabled = uiState.value.date != null && uiState.value.exporting == 0,
            value = uiState.value.text,
            onValueChange = { viewModel.updateNote(it) },
            textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(4.dp)
                .background(color = MaterialTheme.colorScheme.surface)
        )
    }
}