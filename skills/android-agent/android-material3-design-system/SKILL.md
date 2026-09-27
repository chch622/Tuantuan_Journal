---
name: "android-material3-design-system"
description: "Apply Material 3 tokens, color, type, spacing, adaptive components, and theme ownership in Android apps."
metadata:
  version: "0.1.0"
  category: "ui"
  tags: ["android", "material3", "design-system", "compose"]
  triggers:
    include: ["material3 theme android", "design system tokens compose", "android typography color system", "material components cleanup", "adaptive material ui"]
    exclude: ["room dao issue", "agp version mismatch", "hilt injection only"]
  owners: ["@android-agent-skills/maintainers"]
  test_targets: ["examples/orbittasks-compose", "examples/orbittasks-xml", "benchmarks/triggers.jsonl"]
---
# Android Material3 Design System

## When To Use
- Use this skill when the request is about: material3 theme android, design system tokens compose, android typography color system.
- Primary outcome: Apply Material 3 tokens, color, type, spacing, adaptive components, and theme ownership in Android apps.
- Handoff skills when the scope expands:
- `android-compose-foundations`
- `android-compose-accessibility`

## Workflow
1. Identify whether the target surface is Compose, View system, or a mixed interoperability screen.
2. Select the lowest-friction UI pattern that satisfies responsiveness, accessibility, and performance needs.
3. Build the UI around stable state, explicit side effects, and reusable design tokens.
4. Exercise edge cases such as long text, font scaling, RTL, and narrow devices in the fixture apps.
5. Validate with unit, UI, and screenshot-friendly checks before handing off.

## Guardrails
- Optimize for stable state and predictable rendering before adding animation or abstraction.
- Respect accessibility semantics, contrast, focus order, and touch target guidance by default.
- Do not mix Compose and View system ownership without an explicit interoperability boundary.
- Prefer measured performance work over premature micro-optimizations.

## Anti-Patterns
- Embedding navigation or business logic directly in leaf UI components.
- Using fixed dimensions that break on localization or dynamic text.
- Ignoring semantics and announcing only visual changes.
- Porting XML patterns directly into Compose without adapting the mental model.

## Examples
### Happy path
- Scenario: Apply a coherent Material 3 theme to the Compose showcase screens.
- Command: `cd examples/orbittasks-compose && ./gradlew :app:assembleDebug`

### Edge case
- Scenario: Keep XML and Compose surfaces visually aligned during mixed UI migration.
- Command: `cd examples/orbittasks-xml && ./gradlew :app:assembleDebug`

### Failure recovery
- Scenario: Avoid routing theme-token work into accessibility or generic Compose foundations.
- Command: `python3 scripts/eval_triggers.py --skill android-material3-design-system`

## Done Checklist
- The implementation path is explicit, minimal, and tied to the right Android surface.
- Relevant example commands and benchmark prompts have been exercised or updated.
- Handoffs to adjacent skills are documented when the request crosses boundaries.
- Official references cover the chosen pattern and the main migration or troubleshooting path.

## Official References
- [https://developer.android.com/develop/ui/compose/designsystems/material3](https://developer.android.com/develop/ui/compose/designsystems/material3)
- [https://developer.android.com/develop/ui/compose/designsystems/custom](https://developer.android.com/develop/ui/compose/designsystems/custom)
- [https://developer.android.com/develop/ui/views/theming/themes](https://developer.android.com/develop/ui/views/theming/themes)
- [https://developer.android.com/guide/practices/ui_guidelines/material-design](https://developer.android.com/guide/practices/ui_guidelines/material-design)
