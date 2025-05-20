# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Kotlin Multiplatform Project Overview

This is a Kotlin Multiplatform project targeting Android and Desktop with plans to expose functionality to JavaScript.

Project structure:
- `/composeApp` - Core of the project containing shared code across platforms:
  - `commonMain` - Platform-independent code shared by all targets
  - `androidMain` - Android-specific implementation
  - `desktopMain` - Desktop JVM-specific implementation

The codebase uses a simple architecture with:
- `Platform.kt` - Interface with platform-specific implementations
- `Greeting.kt` - Simple example class that uses the platform info
- `App.kt` - Main Compose UI component for the application

## Build and Run Commands

### Building the Project

```bash
# Build the entire project
./gradlew build

# Or use the build script
./build.sh
```

### Running Desktop Application

```bash
# Run the desktop application
./gradlew :composeApp:desktopRun
```

### Running Android Application

```bash
# Run the Android application (requires connected device or emulator)
./gradlew :composeApp:installDebug
```

### Running Tests

```bash
# Run all tests
./gradlew test

# Run just desktop tests
./gradlew :composeApp:desktopTest

# Run just Android tests
./gradlew :composeApp:testDebugUnitTest
```

## Current Development Focus

The current branch is working on exposing Kotlin code to JavaScript. The plan involves:

1. Modifying `composeApp/build.gradle.kts` to add JavaScript as a target
2. Creating JavaScript-specific implementation for `Platform` interface
3. Using `@JsExport` annotation to mark code for JavaScript export
4. Setting up build tasks for the JavaScript bundle

When this is implemented, the JavaScript output can be built with:
```bash
# Development build
./gradlew :composeApp:jsBrowserDevelopmentWebpack

# Production build
./gradlew :composeApp:jsBrowserDistributionWebpack
```

## Compose Multiplatform Details

The project uses Compose Multiplatform to share UI across Android and Desktop.

Key components to know:
- The `@Composable` annotation marks functions that define UI
- The entry point for the UI is the `App.kt` file
- Platform-specific entry points are in `MainActivity.kt` (Android) and `main.kt` (Desktop)