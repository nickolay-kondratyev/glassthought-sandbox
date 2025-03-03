# Search for job configuration files that mention 'main' in
# Jenkins docker container.
#
# This script is a good example how to look into the container files.
echo "Searching for job configuration files mentioning 'main'..."

docker exec -it thorg-jenkins bash -c '
    # Look in jobs directory
    echo "=== Job Configuration Files ==="
    find /var/jenkins_home/jobs -type f -name "config.xml" -exec grep -l "main" {} \; | while read file; do
        echo "File: $file"
        grep -n "main" "$file" | head -10
        echo ""
    done

    # Count total occurrences
    echo "Total occurrences of \"main\" in job configurations:"
    find /var/jenkins_home/jobs -type f -name "config.xml" -exec grep -l "main" {} \; | wc -l
    echo ""
'
