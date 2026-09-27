#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../../.." && pwd)"

cd "$ROOT"
python3 scripts/eval_triggers.py --skill android-mobile-frontend-design

cd "$ROOT/examples/orbittasks-compose"
./gradlew :app:assembleDebug

cd "$ROOT/examples/orbittasks-xml"
./gradlew :app:assembleDebug
