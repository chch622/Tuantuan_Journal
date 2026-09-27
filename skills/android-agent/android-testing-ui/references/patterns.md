# Android Testing UI Patterns

## Selection Notes
- Category: `quality-release`
- Best fit when the request matches the trigger language in `SKILL.md` and the implementation focus is `Validate Android UI behavior with Compose UI tests, Espresso-style checks, accessibility assertions, and state coverage.`
- Use screenshot checks for stable visual surfaces and instrumentation for interaction-heavy flows.
- Reach for neighboring skills only after this skill has framed the main problem.

## Default Review Sequence
1. Scope the risk surface: correctness, security, performance, test depth, or release automation.
2. Pick the narrowest verification strategy that still catches the likely regressions, including Roborazzi when the risk is visual.
3. Instrument the workflow so failures are actionable rather than just red.
4. Run the relevant checks on the showcase apps and packaging outputs.
5. Capture any residual risk with explicit follow-up work and owner skills.

## Handoff Shortlist
- `android-compose-accessibility`
- `android-ui-states-validation`
