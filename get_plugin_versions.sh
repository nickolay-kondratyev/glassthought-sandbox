#!/usr/bin/env bash

set -e

# Path to Jenkins plugins directory
JENKINS_PLUGINS_DIR="$HOME/.jenkins/plugins"

# Check if plugins directory exists
if [ ! -d "$JENKINS_PLUGINS_DIR" ]; then
  echo "Jenkins plugins directory not found at $JENKINS_PLUGINS_DIR"
  exit 1
fi

# Create a temporary file for the updated plugins.txt
TEMP_FILE=$(mktemp)

# Process each plugin in plugins.txt
while IFS= read -r line || [[ -n "$line" ]]; do
  # Skip empty lines
  if [ -z "$line" ]; then
    continue
  fi
  
  # Extract plugin name (remove :latest or any version)
  plugin_name=$(echo "$line" | cut -d':' -f1)
  
  # Check if plugin directory exists
  if [ -d "$JENKINS_PLUGINS_DIR/$plugin_name" ]; then
    # Get version from the manifest file
    if [ -f "$JENKINS_PLUGINS_DIR/$plugin_name/META-INF/MANIFEST.MF" ]; then
      version=$(grep "Plugin-Version:" "$JENKINS_PLUGINS_DIR/$plugin_name/META-INF/MANIFEST.MF" | cut -d' ' -f2 | tr -d '\r')
      echo "$plugin_name:$version"
      echo "$plugin_name:$version" >> "$TEMP_FILE"
    else
      echo "Warning: Manifest file not found for $plugin_name"
      echo "$line" >> "$TEMP_FILE"
    fi
  else
    echo "Warning: Plugin directory not found for $plugin_name"
    echo "$line" >> "$TEMP_FILE"
  fi
done < plugins.txt

echo -e "\nUpdated plugins.txt with exact versions has been created at $TEMP_FILE"
echo "You can review it and then replace your plugins.txt with:"
echo "cp $TEMP_FILE plugins.txt" 