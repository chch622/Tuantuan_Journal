# Android UI States Validation Runnable Scenarios

## Happy path
- Goal: Validate OrbitTasks loading, content, and success confirmation states.
- Command: `cd examples/orbittasks-compose && ./gradlew :app:connectedDebugAndroidTest`

## Edge case
- Goal: Exercise long content, empty lists, and sync failures in the XML fixture.
- Command: `cd examples/orbittasks-xml && ./gradlew :app:connectedDebugAndroidTest`

## Failure recovery
- Goal: Avoid misrouting UI validation work to accessibility or testing-only skills.
- Command: `python3 scripts/eval_triggers.py --skill android-ui-states-validation`
