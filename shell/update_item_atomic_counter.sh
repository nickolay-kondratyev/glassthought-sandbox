aws dynamodb update-item \
--table-name counter \
--key '{"pk":{"S":"abc123"}}' \
--update-expression "ADD quantity :change" \
--expression-attribute-values "file://${GT_SANDBOX}/json/change_val.json"
