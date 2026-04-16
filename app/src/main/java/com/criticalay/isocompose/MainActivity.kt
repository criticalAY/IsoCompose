package com.criticalay.isocompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.criticalay.isocompose.ui.theme.IsoComposeTheme
import com.criticalay.kubik.compose.IsometricCanvas
import com.criticalay.kubik.core.Point
import com.criticalay.kubik.shapes.Cylinder
import com.criticalay.kubik.shapes.Octahedron
import com.criticalay.kubik.shapes.Prism
import com.criticalay.kubik.shapes.Pyramid
import com.criticalay.kubik.shapes.Stairs

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IsoComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        Text(
                            text = "Kubik Isometric Demo",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(16.dp)
                        )
                        IsometricDemo(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun IsometricDemo(modifier: Modifier = Modifier) {
    IsometricCanvas(modifier = modifier) {
        // Ground platform
        shape(
            Prism(Point(0.0, 0.0, 0.0), 5.0, 5.0, 0.3),
            Color(0xFF90A4AE)
        )

        // Blue building
        shape(
            Prism(Point(0.5, 0.5, 0.3), 1.5, 1.5, 2.0),
            Color(0xFF42A5F5)
        )

        // Red pyramid
        shape(
            Pyramid(Point(3.0, 0.5, 0.3), 1.2, 1.2, 1.5),
            Color(0xFFEF5350)
        )

        // Green cylinder
        shape(
            Cylinder(Point(1.5, 3.0, 0.3), 0.6, 1.8),
            Color(0xFF66BB6A)
        )

        // Orange stairs
        shape(
            Stairs(Point(3.0, 3.0, 0.3), stepCount = 5)
                .scale(Point(3.0, 3.0, 0.3), 1.2, 1.2, 1.5),
            Color(0xFFFFA726)
        )

        // Purple octahedron on top of blue building
        shape(
            Octahedron(Point(0.75, 0.75, 2.3))
                .scale(Point(1.25, 1.25, 2.8), 0.8),
            Color(0xFFAB47BC)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun IsometricDemoPreview() {
    IsoComposeTheme {
        IsometricDemo(modifier = Modifier.fillMaxSize())
    }
}
