import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.edamametech.android.dayleaf3.ui.theme.DayLeaf3Theme
import java.time.LocalDate

@Composable
fun DayLeaf3Screen(timestamp: LocalDate) {
    Text(
        text = "Hello, ${timestamp}!"
    )
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
