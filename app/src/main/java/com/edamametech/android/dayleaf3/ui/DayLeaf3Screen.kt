import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.edamametech.android.dayleaf3.ui.theme.DayLeaf3Theme

@Composable
fun DayLeaf3Screen() {
    Text(
        text = "Hello, World!"
    )
}

@Preview(showBackground = true)
@Composable
fun DayLeaf3ScreenPreview() {
    DayLeaf3Theme {
        DayLeaf3Screen()
    }
}
