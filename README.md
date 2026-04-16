# kubik

An isometric 3D rendering library for Jetpack Compose. No OpenGL, no native code — just Canvas drawing all the way down.

[![Maven Central](https://img.shields.io/maven-central/v/io.github.criticalay/kubik)](https://central.sonatype.com/artifact/io.github.criticalay/kubik)
[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](LICENSE)

```kotlin
IsometricCanvas(modifier = Modifier.fillMaxSize()) {
    shape(Prism(Point(0.0, 0.0, 0.0), 2.0, 2.0, 1.0), Color.Blue)
    shape(Pyramid(Point(3.0, 0.0, 0.0)), Color.Red)
}
```

That renders a blue box and a red pyramid in isometric view, lit from a default direction with correct depth ordering. That's basically the whole API.

## Why this exists

I wanted to draw isometric scenes from Compose without reaching for OpenGL or shipping a heavy native dependency. Turns out you can do a lot with the 2D Canvas if you do the projection math yourself — so I wrote it. kubik is a from-scratch Kotlin/Compose port of the ideas in [jayteealao/isometric](https://github.com/jayteealao/isometric) and, further back, [jdan/isomer](https://github.com/jdan/isomer).

It's useful for:

- Dashboards and data viz with a 3D feel
- Small games and prototypes
- Decorative scenes and icons
- Anything where you want the look of 3D without the tooling overhead

It's **not** a replacement for a real 3D engine. If you need dynamic cameras, textures, or thousands of draw calls per frame, grab Filament or Sceneview.

## Install

Available on Maven Central.

```kotlin
// build.gradle.kts (your module)
dependencies {
    implementation("io.github.criticalay:kubik:1.0.0")
}
```

Requirements:

- `minSdk` ≥ 24
- Jetpack Compose (any reasonably recent BOM)
- Kotlin 1.9+

kubik pulls `androidx.compose.ui` and `androidx.compose.foundation` transitively — you don't need to add them explicitly if you don't already have them.

## Examples

### 1. A tiny city

A ground tile with a building and a pyramid. Shows how positions and sizes compose.

```kotlin
@Composable
fun CityBlock() {
    IsometricCanvas(modifier = Modifier.fillMaxSize()) {
        // Ground platform
        shape(
            Prism(Point(0.0, 0.0, 0.0), width = 5.0, depth = 5.0, height = 0.3),
            Color(0xFF90A4AE),
        )

        // Tower — sits on top of the ground (z = 0.3)
        shape(
            Prism(Point(0.5, 0.5, 0.3), width = 1.5, depth = 1.5, height = 2.0),
            Color(0xFF42A5F5),
        )

        // Pyramid next to it
        shape(
            Pyramid(Point(3.0, 0.5, 0.3), width = 1.2, depth = 1.2, height = 1.5),
            Color(0xFFEF5350),
        )
    }
}
```

Point `(x, y, z)` follows kubik's convention: `x` and `y` are in the ground plane, `z` goes up. Place things on top of a surface by adding the surface's height to `z`.

### 2. A spinning cube

`rotateZ` (and `rotateX` / `rotateY`) rotate a shape around an arbitrary origin point. Combine that with Compose's animation APIs and you get free animation.

```kotlin
@Composable
fun SpinningCube() {
    val angle by rememberInfiniteTransition(label = "spin")
        .animateFloat(
            initialValue = 0f,
            targetValue = (2 * PI).toFloat(),
            animationSpec = infiniteRepeatable(tween(4_000, easing = LinearEasing)),
            label = "angle",
        )

    IsometricCanvas(modifier = Modifier.fillMaxSize()) {
        val center = Point(0.5, 0.5, 0.5)
        shape(
            Prism(Point.ORIGIN, 1.0, 1.0, 1.0).rotateZ(center, angle.toDouble()),
            Color(0xFFAB47BC),
        )
    }
}
```

Every shape method (`translate`, `rotateX/Y/Z`, `scale`) returns a new `Shape`, so you can chain them and the originals stay immutable.

### 3. Extrude your own shape

If the built-in shapes aren't enough, build a 2D `Path` and extrude it.

```kotlin
// A plus/cross, extruded into a 3D block
val plusPath = Path(
    Point(-0.2, -0.6, 0.0), Point( 0.2, -0.6, 0.0),
    Point( 0.2, -0.2, 0.0), Point( 0.6, -0.2, 0.0),
    Point( 0.6,  0.2, 0.0), Point( 0.2,  0.2, 0.0),
    Point( 0.2,  0.6, 0.0), Point(-0.2,  0.6, 0.0),
    Point(-0.2,  0.2, 0.0), Point(-0.6,  0.2, 0.0),
    Point(-0.6, -0.2, 0.0), Point(-0.2, -0.2, 0.0),
)

IsometricCanvas(modifier = Modifier.fillMaxSize()) {
    shape(Shape.extrude(plusPath, height = 0.4), Color(0xFFEC407A))
}
```

Or skip building it yourself and just use the `Cross` shape — that's exactly what it does internally.

## What's in the box

Built-in shapes:

| Shape     | Constructor |
|-----------|-------------|
| `Prism`   | rectangular box |
| `Pyramid` | 4-sided pyramid |
| `Cylinder` | circular extrusion |
| `Cone`     | circular base tapering to apex |
| `Sphere`   | latitude-ring approximation |
| `Torus`    | donut |
| `HexPrism` | 6-sided column |
| `Octahedron` | 8-face polyhedron |
| `Stairs`   | stepped shape |
| `Star`     | extruded N-pointed star |
| `Cross`    | extruded plus shape |
| `Diamond`  | extruded rhombus |
| `Wedge`    | triangular ramp |

Plus `Circle` (a `Path` you can feed into `Shape.extrude(...)`) and `Path` for your own polygons.

## How it works (if you're curious)

kubik never actually rasterizes 3D. Every shape is just a bag of flat polygon faces (`Path` objects). On each frame the engine:

1. Projects each 3D point through a 30° isometric matrix to 2D screen space.
2. Culls back-facing polygons using a signed-area test.
3. Shades each face by dotting its surface normal with a light direction.
4. Sorts faces back-to-front using an intersection-based topological sort.
5. Draws them as filled paths on a Compose `Canvas`.

No GPU, no shaders, no OpenGL context. Just carefully ordered `drawPath` calls.

If you want to read the source, `kubik/src/main/kotlin/com/criticalay/kubik/engine/IsometricProjection.kt` is a good starting point.

## Contributing

Bug reports, feature ideas, and PRs are welcome — see the issue templates. Please run `./gradlew ktlintCheck` before submitting.

For anything security-related, see [SECURITY.md](SECURITY.md) and report it privately, not via a public issue.

## License

Licensed under the Apache License, Version 2.0 — see [LICENSE](LICENSE) for the full text.

```
Copyright 2026 Ashish Yadav <mailtoashish693@gmail.com>

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0
```
