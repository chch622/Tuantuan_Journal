#!/usr/bin/env bash
    set -euo pipefail

    cat <<'EOF'
    Skill: Android Testing UI
    Canonical path: skills/android-testing-ui
    Example commands:
    Happy path: cd examples/orbittasks-compose && ./gradlew :app:connectedDebugAndroidTest
Edge case: cd examples/orbittasks-xml && ./gradlew :app:connectedDebugAndroidTest
Failure recovery: python3 scripts/eval_triggers.py --skill android-testing-ui
    EOF
