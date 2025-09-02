#!/bin/bash
cd /home/kavia/workspace/code-generation/smart-document-scanner-151-160/camscanner_mobile_frontend
./gradlew lint
LINT_EXIT_CODE=$?
if [ $LINT_EXIT_CODE -ne 0 ]; then
   exit 1
fi

