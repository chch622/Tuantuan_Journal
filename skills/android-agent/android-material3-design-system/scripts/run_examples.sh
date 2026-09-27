#!/usr/bin/env bash
    set -euo pipefail

    cat <<'EOF'
    Skill: Android Material3 Design System
    Canonical path: skills/android-material3-design-system
    Example commands:
    Happy path: cd examples/orbittasks-compose && ./gradlew :app:assembleDebug
Edge case: cd examples/orbittasks-xml && ./gradlew :app:assembleDebug
Failure recovery: python3 scripts/eval_triggers.py --skill android-material3-design-system
    EOF
