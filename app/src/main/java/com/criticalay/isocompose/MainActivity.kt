package com.criticalay.isocompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.criticalay.isocompose.ui.theme.IsoComposeTheme
import com.criticalay.kubik.compose.IsometricCanvas
import com.criticalay.kubik.core.Point
import com.criticalay.kubik.shapes.Cone
import com.criticalay.kubik.shapes.Cross
import com.criticalay.kubik.shapes.Cylinder
import com.criticalay.kubik.shapes.Diamond
import com.criticalay.kubik.shapes.HexPrism
import com.criticalay.kubik.shapes.Octahedron
import com.criticalay.kubik.shapes.Prism
import com.criticalay.kubik.shapes.Pyramid
import com.criticalay.kubik.shapes.Sphere
import com.criticalay.kubik.shapes.Stairs
import com.criticalay.kubik.shapes.Star
import com.criticalay.kubik.shapes.Torus
import com.criticalay.kubik.shapes.Wedge

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IsoComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    var selectedTab by remember { mutableIntStateOf(0) }
                    val tabs = listOf("City", "Shapes", "Advanced")

                    Column(modifier = Modifier.padding(innerPadding)) {
                        TabRow(selectedTabIndex = selectedTab) {
                            tabs.forEachIndexed { index, title ->
                                Tab(
                                    selected = selectedTab == index,
                                    onClick = { selectedTab = index },
                                    text = { Text(title) }
                                )
                            }
                        }
                        when (selectedTab) {
                            0 -> CityDemo(modifier = Modifier.fillMaxWidth().weight(1f))
                            1 -> ShapesGallery(modifier = Modifier.fillMaxWidth().weight(1f))
                            2 -> AdvancedShapes(modifier = Modifier.fillMaxWidth().weight(1f))
                        }
                    }
                }
            }
        }
    }
}

/** Original city scene with basic shapes. */
@Composable
fun CityDemo(modifier: Modifier = Modifier) {
    IsometricCanvas(modifier = modifier) {
        // Ground
        shape(Prism(Point(0.0, 0.0, 0.0), 5.0, 5.0, 0.3), Color(0xFF90A4AE))
        // Blue building
        shape(Prism(Point(0.5, 0.5, 0.3), 1.5, 1.5, 2.0), Color(0xFF42A5F5))
        // Red pyramid
        shape(Pyramid(Point(3.0, 0.5, 0.3), 1.2, 1.2, 1.5), Color(0xFFEF5350))
        // Green cylinder
        shape(Cylinder(Point(1.5, 3.0, 0.3), 0.6, 1.8), Color(0xFF66BB6A))
        // Orange stairs
        shape(
            Stairs(Point(3.0, 3.0, 0.3), stepCount = 5)
                .scale(Point(3.0, 3.0, 0.3), 1.2, 1.2, 1.5),
            Color(0xFFFFA726)
        )
        // Purple octahedron on building
        shape(
            Octahedron(Point(0.75, 0.75, 2.3))
                .scale(Point(1.25, 1.25, 2.8), 0.8),
            Color(0xFFAB47BC)
        )
    }
}

/** Gallery of all new shapes. */
@Composable
fun ShapesGallery(modifier: Modifier = Modifier) {
    IsometricCanvas(modifier = modifier, scale = 50.0) {
        // Ground
        shape(Prism(Point(-1.0, -1.0, 0.0), 10.0, 10.0, 0.2), Color(0xFFE0E0E0))

        // Row 1: Cone, Wedge, HexPrism
        shape(Cone(Point(0.5, 0.5, 0.2), radius = 0.8, height = 1.5), Color(0xFFFF7043))
        shape(Wedge(Point(2.5, 0.5, 0.2), 1.5, 1.5, 1.2), Color(0xFF5C6BC0))
        shape(HexPrism(Point(5.5, 0.5, 0.2), radius = 0.8, height = 1.2), Color(0xFF26A69A))

        // Row 2: Star, Cross, Diamond
        shape(Star(Point(0.5, 4.0, 0.2), outerRadius = 1.0, innerRadius = 0.4, height = 0.5), Color(0xFFFFCA28))
        shape(Cross(Point(3.5, 4.0, 0.2), size = 1.8, armWidth = 0.5, height = 0.5), Color(0xFFEC407A))
        shape(Diamond(Point(6.0, 4.0, 0.2), width = 1.5, depth = 1.5, height = 0.8), Color(0xFF29B6F6))

        // Row 3: Sphere, Torus
        shape(Sphere(Point(1.0, 7.0, 1.2), radius = 1.0, latSegments = 10, lonSegments = 14), Color(0xFF66BB6A))
        shape(Torus(Point(5.0, 7.0, 1.0), majorRadius = 1.0, minorRadius = 0.35, majorSegments = 16, minorSegments = 10), Color(0xFFAB47BC))
    }
}

/** Advanced composition mixing shapes. */
@Composable
fun AdvancedShapes(modifier: Modifier = Modifier) {
    IsometricCanvas(modifier = modifier, scale = 55.0) {
        // Platform
        shape(HexPrism(Point(3.0, 3.0, 0.0), radius = 3.5, height = 0.3), Color(0xFF78909C))

        // Central tower: hex prism + sphere on top
        shape(HexPrism(Point(3.0, 3.0, 0.3), radius = 0.8, height = 2.5), Color(0xFF42A5F5))
        shape(Sphere(Point(3.0, 3.0, 3.3), radius = 0.6, latSegments = 8, lonSegments = 12), Color(0xFFFFCA28))

        // Surrounding cone pillars
        shape(Cone(Point(1.0, 1.5, 0.3), radius = 0.4, height = 1.5), Color(0xFFEF5350))
        shape(Cone(Point(5.0, 1.5, 0.3), radius = 0.4, height = 1.5), Color(0xFFEF5350))
        shape(Cone(Point(1.0, 4.5, 0.3), radius = 0.4, height = 1.5), Color(0xFFEF5350))
        shape(Cone(Point(5.0, 4.5, 0.3), radius = 0.4, height = 1.5), Color(0xFFEF5350))

        // Floating torus
        shape(Torus(Point(3.0, 3.0, 4.5), majorRadius = 1.2, minorRadius = 0.2, majorSegments = 18, minorSegments = 8), Color(0xFFAB47BC))

        // Diamond and star accents
        shape(Diamond(Point(0.5, 3.0, 0.3), 1.0, 1.0, 0.6), Color(0xFF26A69A))
        shape(Star(Point(5.5, 3.0, 0.3), outerRadius = 0.7, innerRadius = 0.3, height = 0.3), Color(0xFFFFA726))

        // Wedge ramps
        shape(Wedge(Point(1.5, 5.5, 0.3), 1.0, 1.5, 0.8), Color(0xFF8D6E63))
        shape(Wedge(Point(4.0, 5.5, 0.3), 1.0, 1.5, 0.8), Color(0xFF8D6E63))
    }
}

@Preview(showBackground = true)
@Composable
fun CityDemoPreview() {
    IsoComposeTheme {
        CityDemo(modifier = Modifier.fillMaxSize())
    }
}
