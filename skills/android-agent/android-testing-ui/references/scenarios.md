# Android Testing UI Runnable Scenarios

## Happy path
- Goal: Run Compose UI assertions for the task board and verify the same surface with Roborazzi.
- Command: `cd examples/orbittasks-compose && ./gradlew :app:connectedDebugAndroidTest verifyRoborazziDebug`

## Edge case
- Goal: Record or refresh visual baselines for the Compose fixture after an intentional UI change.
- Command: `cd examples/orbittasks-compose && ./gradlew recordRoborazziDebug`

## Failure recovery
- Goal: Separate UI-testing requests from UI-state reviews or accessibility-only prompts.
- Command: `python3 scripts/eval_triggers.py --skill android-testing-ui`
