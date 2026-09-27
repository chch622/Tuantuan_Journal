---
name: "android-ui-states-validation"
description: "Review Android UI flows for empty, loading, error, offline, and edge-case behavior before release."
metadata:
  version: "0.1.0"
  category: "product"
  tags: ["android", "ui", "validation", "edge-cases"]
  triggers:
    include: ["validate android ui states", "check loading empty error flow android", "edge cases in android screen", "android screen regression review", "state coverage before release", "android-ui-states-validation skill", "state matrix before release", "recovery ux android screen"]
    exclude: ["macrobenchmark only", "dependency alignment only", "hilt graph only", "roborazzi screenshot test android", "compose ui test screen", "espresso validation android"]
  owners: ["@android-agent-skills/maintainers"]
  test_targets: ["examples/orbittasks-compose", "examples/orbittasks-xml", "benchmarks/triggers.jsonl"]
---
# Android UI States Validation

## When To Use
- Use this skill when the request is about: validate android ui states, check loading empty error flow android, edge cases in android screen.
- Primary outcome: Review Android UI flows for empty, loading, error, offline, and edge-case behavior before release.
- Reach for this skill when the core question is state coverage and recovery UX, not which test framework or assertion library to use.
- This skill decides what the user should see in each branch of the state matrix. `android-testing-ui` only validates that design with assertions and screenshots.
- Handoff skills when the scope expands:
- `android-compose-accessibility`
- `android-testing-ui`

## Workflow
1. Confirm the user-visible journey, target device behavior, and failure states that matter.
2. Build a state matrix that covers loading, content, empty, error, offline, stale-data, denied-permission, and post-action confirmation states as applicable.
3. Identify the owning screens, activities, destinations, and state holders for each branch of that matrix.
4. Validate recovery UX, accessibility, configuration changes, and back-stack behavior in the showcase apps.
5. Hand off to testing only when the missing work is about assertions or automation rather than state design.

## Guardrails
- Treat loading, empty, error, offline, and permission-denied states as first-class UI states.
- Do not hide navigation or permission side effects inside reusable UI components.
- Prefer lifecycle-aware APIs over manual callback chains.
- Keep deep links, intents, and permission prompts testable and observable.
- Distinguish transient messages, persistent error states, and stale-but-usable content instead of collapsing them into one generic failure screen.

## Anti-Patterns
- Assuming the happy path is enough for product flows.
- Hard-coding request codes or route strings in multiple places.
- Triggering navigation directly from repositories or network layers.
- Shipping flows without recovery UI for denied permissions or broken state.
- Using tests as a substitute for deciding what the product should show in each failure mode.

## Examples
### Happy path
- Scenario: Validate OrbitTasks loading, content, and success confirmation states.
- Command: `cd examples/orbittasks-compose && ./gradlew :app:connectedDebugAndroidTest`

### Edge case
- Scenario: Exercise long content, empty lists, and sync failures in the XML fixture.
- Command: `cd examples/orbittasks-xml && ./gradlew :app:connectedDebugAndroidTest`

### Failure recovery
- Scenario: Avoid misrouting UI validation work to accessibility or testing-only skills.
- Command: `python3 scripts/eval_triggers.py --skill android-ui-states-validation`

## Done Checklist
- The implementation path is explicit, minimal, and tied to the right Android surface.
- Relevant example commands and benchmark prompts have been exercised or updated.
- Handoffs to adjacent skills are documented when the request crosses boundaries.
- Official references cover the chosen pattern and the main migration or troubleshooting path.

## Official References
- [https://developer.android.com/topic/architecture/ui-layer](https://developer.android.com/topic/architecture/ui-layer)
- [https://developer.android.com/guide/topics/resources/runtime-changes](https://developer.android.com/guide/topics/resources/runtime-changes)
- [https://developer.android.com/guide/practices/ui_guidelines](https://developer.android.com/guide/practices/ui_guidelines)
- [https://developer.android.com/guide/topics/ui/accessibility/apps](https://developer.android.com/guide/topics/ui/accessibility/apps)
