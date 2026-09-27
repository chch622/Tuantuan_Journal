# Android Material3 Design System Runnable Scenarios

## Happy path
- Goal: Apply a coherent Material 3 theme to the Compose showcase screens.
- Command: `cd examples/orbittasks-compose && ./gradlew :app:assembleDebug`

## Edge case
- Goal: Keep XML and Compose surfaces visually aligned during mixed UI migration.
- Command: `cd examples/orbittasks-xml && ./gradlew :app:assembleDebug`

## Failure recovery
- Goal: Avoid routing theme-token work into accessibility or generic Compose foundations.
- Command: `python3 scripts/eval_triggers.py --skill android-material3-design-system`
