#!/usr/bin/env bash
set -e

THIS="$(dirname "$(realpath "${BASH_SOURCE[0]}")")"

JAVA="$JAVA_HOME/bin/java"

"$JAVA" -jar "$THIS/dynapi.jar" "$@"
