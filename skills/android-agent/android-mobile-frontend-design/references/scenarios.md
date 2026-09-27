# Android Mobile Frontend Design Runnable Scenarios

## Happy path
- Goal: Create a new Android mobile screen design brief with a clear hierarchy, shell, and brand-forward direction.
- Command: `bash skills/android-mobile-frontend-design/scripts/run_examples.sh`

## Edge case
- Goal: Improve an existing OrbitTasks Compose surface so it survives localization, RTL, keyboard insets, and narrow widths without clipped controls.
- Command: `cd examples/orbittasks-compose && ./gradlew :app:assembleDebug`

## Failure recovery
- Goal: Fix overflow, truncation, and weak recovery states in an XML screen before escalating to a full redesign.
- Command: `cd examples/orbittasks-xml && ./gradlew :app:assembleDebug`

