# Android Mobile Frontend Design Patterns

## Mode Selection
- `create`
  Use when the screen is net-new or only partially specified and the main work is deciding hierarchy, shell, action placement, and mobile posture.
- `improve`
  Use when the flow exists but feels generic, cluttered, weak, or visually inconsistent.
- `fix`
  Use when the screen already exists and has concrete problems such as clipping, overflow, bad insets, weak hierarchy, or broken recovery states.

## Compose vs Views Routing
- Compose-heavy surface with flexible sections, adaptive branching, or modern reusable components:
  hand off to `android-compose-foundations` after the design direction is settled.
- XML, Fragment, RecyclerView, or ViewBinding-owned surface:
  hand off to `android-viewsystem-foundations` after the design direction is settled.
- Token, theming, or system-level visual language translation:
  hand off to `android-material3-design-system`.
- Semantics, focus, announcements, or assistive technology behavior:
  hand off to `android-compose-accessibility`.
- Screenshot validation, visual regression checks, or UI assertions:
  hand off to `android-testing-ui`.

## Material 3 vs Brand-Forward
- Strict Material 3:
  best when platform familiarity and low design risk matter more than memorability.
- Brand-forward:
  preferred for this skill by default, but only when localization, overflow safety, touch ergonomics, and clarity remain intact.
- Hybrid:
  keep Material 3 structure and behavior, but use stronger typography, surface contrast, spacing posture, and signature color emphasis.

## Amazing UI Heuristics
- One dominant action and one dominant visual idea per viewport region
- Strong spacing rhythm before decorative effects
- Typography that separates headline, action, body, and metadata jobs clearly
- Motion used for continuity and feedback, not novelty
- Surfaces that feel intentional, not card spam
- A screen that still feels premium under translation, RTL, font scaling, keyboard, and multi-window stress

## Localization and Overflow Stress Checklist
- Long translated strings on primary buttons, tabs, chips, banners, toolbars, and sheets
- Multiline empty states and onboarding copy
- RTL mirroring for spacing, icons, navigation cues, and directional affordances
- Keyboard/IME covering bottom actions or focused inputs
- System bars, taskbars, cutouts, and edge-to-edge padding
- Window-size changes across phones, tablets, foldables, and multi-window
- Font scaling, dense numerals, currency strings, and localized date/time widths
- Gesture-inset conflicts for bottom actions and draggable surfaces
