---
name: "android-testing-ui"
description: "Validate Android UI behavior with Compose UI tests, Espresso-style checks, screenshot assertions, and accessibility verification."
metadata:
  version: "0.1.0"
  category: "quality-release"
  tags: ["android", "testing", "ui-tests", "instrumentation", "screenshot"]
  triggers:
    include: ["android ui test", "compose ui test screen", "espresso validation android", "instrumentation test android flow", "accessibility assertions android ui", "roborazzi screenshot test android", "visual regression android ui", "android-testing-ui skill", "screenshot baseline verification in compose", "verify roborazzi baseline android"]
    exclude: ["gradle version conflict", "room dao only", "release version tag", "loading empty error flow", "state matrix before release"]
  owners: ["@android-agent-skills/maintainers"]
  test_targets: ["examples/orbittasks-compose", "examples/orbittasks-xml", "benchmarks/triggers.jsonl"]
---
# Android Testing UI

## When To Use
- Use this skill when the request is about: android ui test, compose ui test screen, espresso validation android.
- Primary outcome: Validate Android UI behavior with Compose UI tests, Espresso-style checks, screenshot assertions, and accessibility verification.
- Reach for this skill when the missing work is assertions, instrumentation, screenshot capture, or accessibility verification, not product-state design.
- Handoff skills when the scope expands:
- `android-compose-accessibility`
- `android-ui-states-validation`

## Workflow
1. Scope the risk surface: behavior, visual regressions, accessibility semantics, or cross-device interaction.
2. Pick the narrowest verification strategy that still catches the likely regressions: instrumentation for behavior, Roborazzi for visuals, or both for high-risk flows.
3. Instrument the workflow so failures are actionable rather than just red.
4. Run the relevant checks on the showcase apps and packaging outputs.
5. Capture any residual risk with explicit follow-up work and owner skills, including handing off to `android-ui-states-validation` when the state matrix itself is incomplete.

## Guardrails
- Prefer reproducible checks in CI over one-off local heroics.
- Keep screenshot tests stable by using deterministic content, fixed themes, and minimal time-based UI churn.
- Fail with a precise remediation path instead of a vague quality gate.
- Keep secrets, signing material, and production credentials out of examples and fixtures.
- Treat performance and security work as engineering tasks with evidence, not folklore.

## Anti-Patterns
- Adding more tests without increasing signal.
- Turning screenshot tests into pixel-noise snapshots of unstable surfaces.
- Shipping benchmarks or security scans that no one can reproduce.
- Hard-coding release credentials into build logic.
- Using synthetic metrics with no user-impact interpretation.

## Remediation Examples
### Choose behavior vs screenshot coverage
```kotlin
composeTestRule.onNodeWithText("Review blocked state").performClick()
composeTestRule.onNodeWithText("Reminder readiness").assertIsDisplayed()
```

```kotlin
@Test
fun orbitTasksBoard_matchesGolden() {
    composeTestRule.setContent { OrbitTasksApp() }
    composeTestRule.onRoot().captureRoboImage()
}
```

### Accessibility assertion alongside UI behavior
```kotlin
composeTestRule
    .onNodeWithContentDescription("Team avatar for release readiness")
    .assertIsDisplayed()
```

## Examples
### Happy path
- Scenario: Run Compose UI assertions for the task board and action flows, then verify the same stable surface with Roborazzi.
- Command: `cd examples/orbittasks-compose && ./gradlew :app:connectedDebugAndroidTest verifyRoborazziDebug`

### Edge case
- Scenario: Record or refresh screenshot baselines when a stable Compose surface changes intentionally.
- Command: `cd examples/orbittasks-compose && ./gradlew recordRoborazziDebug`

### Failure recovery
- Scenario: Separate UI-testing requests from UI-state reviews or accessibility-only prompts.
- Command: `python3 scripts/eval_triggers.py --skill android-testing-ui`

## Done Checklist
- The implementation path is explicit, minimal, and tied to the right Android surface.
- Relevant example commands and benchmark prompts have been exercised or updated.
- Handoffs to adjacent skills are documented when the request crosses boundaries.
- Official references cover the chosen pattern and the main migration or troubleshooting path.

## Official References
- [https://developer.android.com/training/testing/espresso](https://developer.android.com/training/testing/espresso)
- [https://developer.android.com/develop/ui/compose/testing](https://developer.android.com/develop/ui/compose/testing)
- [https://developer.android.com/guide/topics/ui/accessibility/testing](https://developer.android.com/guide/topics/ui/accessibility/testing)
- [https://developer.android.com/training/testing/instrumented-tests](https://developer.android.com/training/testing/instrumented-tests)
- [https://github.com/takahirom/roborazzi](https://github.com/takahirom/roborazzi)
