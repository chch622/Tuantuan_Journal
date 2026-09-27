---
name: "android-mobile-frontend-design"
description: "Design Android mobile frontend experiences from scratch, improve existing screens, and fix UI issues with brand-forward, localization-safe, overflow-safe guidance across Compose and Views."
metadata:
  version: "0.1.0"
  category: "ui"
  tags: ["android", "design", "frontend", "mobile-ui", "compose", "views", "localization", "adaptive"]
  triggers:
    include: ["android mobile frontend design", "create screen from scratch", "redesign existing screen visually", "fix clipped cropped truncated ui", "localization safe mobile ui", "rtl safe mobile ui", "brand forward mobile screen design", "android-mobile-frontend-design skill", "make this screen feel premium and memorable", "world class mobile frontend design pass", "visually stunning but accessible android ui", "improve hierarchy spacing and motion in android ui"]
    exclude: ["room migration only", "retrofit contract only", "hilt injection only", "unit test only", "gradle plugin upgrade only", "model screen state", "design system tokens compose", "recomposition too much android", "adb semantic ui automation"]
  owners: ["@android-agent-skills/maintainers"]
  test_targets: ["examples/orbittasks-compose", "examples/orbittasks-xml", "benchmarks/triggers.jsonl"]
---
# Android Mobile Frontend Design

## When To Use
- Use this skill when the request is about: android mobile frontend design, design android screen from scratch, redesign android mobile screen.
- Primary outcome: Design Android mobile frontend experiences from scratch, improve existing screens, and fix UI issues with brand-forward, localization-safe, overflow-safe guidance across Compose and Views.
- Use this skill when the work is design-intent first: hierarchy, rhythm, screen structure, navigation chrome, spacing, surface model, visual direction, overflow safety, or localization-safe mobile UX.
- Reach for this skill before implementation details when the user wants a screen to feel premium, modern, polished, clearer, calmer, more branded, or more mobile-native.
- Read `references/patterns.md` when you need the create/improve/fix routing matrix, Compose-vs-Views handoff rules, or the localization and overflow stress checklist.
- Read `references/scenarios.md` when you want docs-only walkthroughs for new-screen design briefs, OrbitTasks redesign reviews, or overflow/localization repair passes.
- Handoff skills when the scope expands:
- `android-compose-foundations`
- `android-material3-design-system`
- `android-compose-accessibility`
- `android-viewsystem-foundations`
- `android-testing-ui`

## Operating Modes
### `create`
- Use when a screen does not exist yet or exists only as a rough idea.
- Start from user goal, primary task, state model, and mobile hierarchy instead of jumping to colors or components.
- Decide the screen's dominant pattern early: list-detail, feed, form, dashboard, onboarding flow, confirmation flow, or settings surface.

### `improve`
- Use when a screen works functionally but feels generic, cluttered, dated, confusing, or visually weak.
- Audit what to preserve first: task order, user mental model, brand constraints, and the pieces already carrying useful meaning.
- Redesign hierarchy, spacing, affordances, and action emphasis without changing product intent unless the request explicitly asks for it.

### `fix`
- Use when a screen already exists and has concrete UX defects: clipped text, awkward spacing, unreachable actions, poor hierarchy, broken insets, bad RTL, weak loading states, or cramped controls.
- Repair the smallest set of layout and interaction decisions that restores clarity before proposing a full visual overhaul.
- Treat "cut" as clipped, cropped, truncated, obscured, or otherwise visually broken UI and handle it as a first-class failure mode.

## Workflow
1. Choose the operating mode first: `create`, `improve`, or `fix`.
2. Define the screen job in mobile terms: who is using it, what they must complete fast, what they must understand at a glance, and what deserves confidence or calm.
3. Decide the information hierarchy before styling: primary action, dominant content block, secondary actions, supporting metadata, and recovery states.
4. Choose the right mobile shell for the job: top app bar, bottom bar, rail, sheet, FAB, segmented control, tabs, or list-detail split based on device size and task frequency.
5. Build a surface model that is intentionally brand-forward but still Android-usable: density, corner language, depth, color posture, and typography emphasis should express the product without fighting touch ergonomics.
6. Stress the layout against real mobile constraints: long strings, translated labels, multiline text, RTL, narrow widths, large widths, keyboard, system bars, cutouts, split-screen, tablets, and foldables.
7. Validate action reachability, touch targets, hierarchy, contrast, and scanning speed before handing off to implementation skills.
8. Route the implementation correctly:
- `android-compose-foundations` when the design is settled and the work is Compose structure/layout.
- `android-material3-design-system` when the work is tokenization, theming, or custom design-system translation.
- `android-compose-accessibility` when semantics, focus, or assistive behavior needs dedicated work.
- `android-viewsystem-foundations` when the owning surface is XML/ViewBinding/Fragment UI.
- `android-testing-ui` when the design is ready for screenshot or interaction verification.

## Amazing UI Quality Bar
### What "amazing" means in a mobile Android UI
- The first screenful answers three questions instantly: what this screen is for, what matters most, and what the user should do next.
- The screen feels authored rather than assembled from default components: spacing, type scale, color emphasis, depth, and motion all reinforce the same product personality.
- The primary action is obvious without becoming noisy.
- The UI remains attractive under stress: translation, font scaling, keyboard, gesture navigation, split-screen, and large-screen resizing do not collapse the experience.
- The product feels premium because it is confident and friction-aware, not because it piles on gradients, glass, or decorative chrome.

### Practical craft rules
- Use a consistent spacing rhythm grounded in Android's 4dp grid, but let the screen breathe where clarity benefits from more separation.
- Build typography with distinct jobs: overline/kicker, headline, section label, body, metadata, and action text should not blur together.
- Let one element dominate each viewport region. If headline, filter row, cards, and CTA all compete equally, the design has no conductor.
- Prefer fewer, stronger surfaces over many weak cards and dividers.
- Use color to clarify state, action, and trust. Do not use accent color as wallpaper.
- Motion should reveal structure, preserve context, or confirm state change. Remove it if it is only decorative.

### Screen-family best practices
- Lists and feeds:
  make item hierarchy scannable in under a second, keep row density intentional, and make secondary metadata quieter than the decision-driving content.
- Forms:
  keep labels persistent, make validation and requiredness obvious, and ensure keyboard progression never hides the active task.
- Dashboards:
  do not import desktop density by default; group content into a clear narrative of summary, decision, and drill-down.
- Onboarding and empty states:
  one idea per screen, one primary move, and copy that explains value before detail.
- Sheets and dialogs:
  use them for focused decisions or contextual flows, not as a place to hide full screens with broken navigation.

## Design Direction
### Start with mobile intent, not decoration
- Decide what the user must do one-handed, what can live below the fold, and what must stay visible through motion, keyboard, and narrow widths.
- Mobile UI should feel authored, not squeezed-down desktop UI. Small screens punish weak hierarchy faster than any design review does.
- Brand-forward does not mean louder by default. It means the interface feels deliberate, memorable, and specific to the product instead of generic Material scaffolding.

### Pick a clear visual posture
- Use one dominant posture per screen family:
- `confident utility`: dense, clear, fast, highly legible, restrained color.
- `calm guidance`: more air, stronger sectional rhythm, gentle emphasis, friendly onboarding and empty states.
- `premium trust`: layered surfaces, disciplined motion, richer typography, higher confidence for money, health, or irreversible actions.
- `expressive brand`: stronger color, memorable shapes, and signature layout moves, but still touch-safe and readable.
- Keep the posture consistent across related screens. A premium dashboard and a flat, generic detail screen usually means the system is not yet coherent.

### Create mode specifics
- Start with:
- screen goal
- user state and urgency
- primary action placement
- content order
- state planning for loading, empty, error, offline, and success
- Choose the shell around the primary action:
- bottom action areas for flow completion
- floating action only when it is the dominant repeatable action
- sheets for contextual, interruptible actions
- tabs or segmented controls only when switching content families is central to the screen
- Prefer one strong primary move per screen. If everything shouts, nothing leads.

### Improve mode specifics
- Audit:
- what the eye lands on first
- where the primary action currently sits
- whether spacing communicates grouping
- whether surfaces feel flat, noisy, or over-divided
- whether the screen asks the user to read too much before acting
- whether the screen feels generic because every component has equal visual weight
- whether motion, color, and type are helping orientation or just adding noise
- Preserve:
- product logic
- user muscle memory where it still helps
- good existing component contracts
- any design token investment already paying off
- Improve by reordering emphasis, merging weak sections, clarifying labels, strengthening whitespace, and making calls to action unmissable without increasing clutter.

### Fix mode specifics
- Diagnose:
- clipped or truncated text
- buttons, chips, tabs, and snackbars that cannot survive longer text
- actions hidden behind the keyboard or system bars
- broken sheet heights or unsafe fixed heights
- weak contrast or tiny touch targets
- awkward spacing that makes dense screens harder to scan
- empty, loading, or error states that do not explain what to do next
- visual imbalance where everything is the same size, weight, and urgency
- "cheap" feeling UI caused by weak rhythm, inconsistent corners, inconsistent elevation, or accidental color usage
- Fix layout stress first, then aesthetics. A screen that looks premium in English but breaks in German or Arabic is not finished.

## Localization And Overflow Best Practices
### Localization-aware design
- Expect long-string expansion as the default, not an edge case.
- Design labels, chips, and CTA text to survive translation without depending on the English string length.
- Think multiline first for buttons, filters, banners, onboarding copy, and empty states when the content must remain explicit.
- Treat pseudolocalization as a design tool, not just a QA tool. If the hierarchy collapses under pseudolocalized strings, the design is too brittle.
- Support per-app languages and locale shifts without assuming one global density or one formatting style.
- Respect locale-specific numerals, date/time formats, currency width, and reading rhythm when designing narrow cells and summary rows.
- Ensure visual emphasis does not depend on a specific word order that disappears in translation.

### RTL-aware design
- Mirror directional layout safely instead of hardcoding left/right assumptions.
- Keep iconography, chevrons, progress cues, and text alignment honest in RTL contexts.
- Do not rely on asymmetric padding or a left-anchored visual trick that breaks when mirrored.
- When mixed-direction content appears inside the same screen, design for readability rather than assuming the entire surface behaves as one direction.

### Overflow and cutoff safety
- Do not use fixed-height text containers unless the design has been proven safe under long strings, font scaling, and multiline content.
- No clipped CTA labels, chips, tabs, sheets, snackbars, toolbar titles, or bottom-sheet handles because of decorative layout decisions.
- Prefer safe wrapping and adaptive reflow before silent truncation.
- Use ellipsis only when the hidden content is genuinely noncritical or there is a clear recovery path such as expansion, detail view, or tooltip-equivalent help.
- Ensure keyboards, IME insets, cutouts, taskbars, and system bars never obscure primary actions or active inputs.
- On large screens and foldables, avoid placing crucial content in hinge, fold, or awkward dead-zone regions.
- Do not put tap or drag-critical UI directly under gesture-inset conflict zones in edge-to-edge layouts.

## Adaptive And Edge-To-Edge Best Practices
- Design edge-to-edge by default: scrolling and background content may live behind system bars, but critical tappable UI must honor insets and gesture conflicts.
- For target SDK 35+ and newer devices, assume edge-to-edge is part of the normal Android experience rather than an optional flourish.
- Use window size classes and adaptive structure so layouts can recompose into rails, multi-column sections, or list-detail arrangements without becoming stretched phone UIs.
- Large screens should at least meet "optimized" Android large-screen quality expectations: no letterboxing mindset, no dead wasted space, and no phone-only navigation assumptions.
- Foldables and multi-window states are layout tests, not post-launch surprises.

## Compose And View-System Guidance
### Compose
- Compose is best when the redesign needs flexible hierarchy, adaptive structure, expressive spacing, and reusable UI primitives.
- Use Compose when the screen benefits from compositional sections, easier adaptive branching, and modern motion/visual polish.
- Hand off to `android-compose-foundations` once the design intent is settled and the remaining work is component architecture and layout execution.

### Views and XML
- View-system screens still deserve strong design work; do not assume only Compose screens can feel premium.
- Respect Fragment, RecyclerView, ViewBinding, and ConstraintLayout realities when planning improvements.
- Prefer responsive/adaptive View-system layouts over static, dimension-locked XML that only looks correct on one device profile.
- Hand off to `android-viewsystem-foundations` once the screen direction is clear and the remaining work is XML/Fragment execution.

## Guardrails
- Mobile-first always wins over desktop habits. Do not import dashboard-web density or pointer-driven assumptions into phone screens without justification.
- Brand-forward styling must remain localization-safe, overflow-safe, and accessibility-safe.
- Keep touch targets reachable and obvious; visual minimalism is not an excuse for tiny interactive areas.
- Design for edge-to-edge layouts and real insets from the start rather than bolting them on after the fact.
- Support phones, tablets, foldables, and multi-window states through adaptive structure, not one hardcoded canvas.
- Preserve the product's mental model when improving existing screens; do not redesign the workflow unless the request asks for it.
- Prefer clear hierarchy and recovery UX over decorative motion, ornamental surfaces, or extra chrome.
- At least one of spacing, typography, depth, or color emphasis should make the screen memorable; "safe but generic" is not the target outcome for this skill.

## Anti-Patterns
- Designing a phone screen like a compressed web dashboard with tiny controls and dense top-level navigation.
- Using fixed widths or heights that cause clipping, truncation, or overlap as soon as text length changes.
- Treating English-only screenshots as proof that the design is complete.
- Hiding the primary action below the fold, behind the keyboard, or in visually weak chrome.
- Making every section a card, divider, or boxed surface until the screen becomes noisy and slow to scan.
- Relying on symmetry tricks that collapse in RTL or on narrow widths.
- Solving a layout bug by shortening copy instead of making the design structurally resilient.
- Rebuilding everything visually when a focused repair would fix the real issue.
- Mistaking visual busyness for premium quality.
- Shipping a screen that looks good statically but becomes fragile under IME, font scaling, or long-form content.

## Examples
### Happy path
- Scenario: Create a new Android mobile screen from scratch with a strong hierarchy, clear primary action, adaptive shell, and brand-forward visual posture.
- Command: `bash skills/android-mobile-frontend-design/scripts/run_examples.sh`

### Edge case
- Scenario: Improve an existing OrbitTasks Compose screen so it survives long localized strings, keyboard insets, and RTL mirroring without clipped controls.
- Command: `cd examples/orbittasks-compose && ./gradlew :app:assembleDebug`

### Failure recovery
- Scenario: Fix an existing XML screen with truncation, cramped spacing, and weak empty/error states before escalating to a full redesign.
- Command: `cd examples/orbittasks-xml && ./gradlew :app:assembleDebug`

## Done Checklist
- The operating mode is explicit: `create`, `improve`, or `fix`.
- The information hierarchy and primary action placement are clear before implementation details begin.
- The design direction is brand-forward but still mobile-native and platform-usable.
- Localization, RTL, overflow, keyboard, and inset stress cases have been considered explicitly.
- Compose-vs-Views routing is clear, and implementation work is handed off to the correct neighboring skill.
- The resulting guidance can be used to create a screen from scratch, improve an existing one, or repair a broken one without leaving major design decisions unresolved.

## Official References
- [https://developer.android.com/develop/ui/compose](https://developer.android.com/develop/ui/compose)
- [https://developer.android.com/develop/ui/compose/designsystems/material3](https://developer.android.com/develop/ui/compose/designsystems/material3)
- [https://developer.android.com/develop/ui/compose/designsystems/custom](https://developer.android.com/develop/ui/compose/designsystems/custom)
- [https://developer.android.com/develop/ui/compose/system/material-insets](https://developer.android.com/develop/ui/compose/system/material-insets)
- [https://developer.android.com/develop/ui/compose/layouts/adaptive/use-window-size-classes](https://developer.android.com/develop/ui/compose/layouts/adaptive/use-window-size-classes)
- [https://developer.android.com/develop/ui/views/layout/responsive-adaptive-design-with-views](https://developer.android.com/develop/ui/views/layout/responsive-adaptive-design-with-views)
- [https://developer.android.com/training/basics/supporting-devices/languages.html](https://developer.android.com/training/basics/supporting-devices/languages.html)
- [https://developer.android.com/guide/topics/resources/localization](https://developer.android.com/guide/topics/resources/localization)
- [https://developer.android.com/guide/topics/resources/app-languages](https://developer.android.com/guide/topics/resources/app-languages)
- [https://developer.android.com/guide/topics/resources/internationalization](https://developer.android.com/guide/topics/resources/internationalization)
- [https://developer.android.com/guide/topics/ui/accessibility/apps](https://developer.android.com/guide/topics/ui/accessibility/apps)
- [https://developer.android.com/docs/quality-guidelines/large-screen-app-quality](https://developer.android.com/docs/quality-guidelines/large-screen-app-quality)
