package aws.ddb.snippets;

import java.util.HashMap;
import java.util.Map;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;

public class QueryWithStartSortKey {

    private static final String TABLE_NAME = Constants.HashKeyRangeKeyTable.TABLE_NAME;
    private static final DynamoDbClient DDB = DynamoDbClient.builder()
        .region(Region.US_WEST_2)
        .build();

    public static void main(String[] args) {
        queryItems();
    }

    private static void queryItems() {
        final Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":v1", AttributeValue.builder().s("alphabet_200KB_per_row").build());
        expressionAttributeValues.put(":v2", AttributeValue.builder().s("70").build()); // ASCII value for 'F'

        final QueryRequest queryRequest = QueryRequest.builder()
            .tableName(TABLE_NAME)
            .keyConditionExpression("hash_key = :v1 and sort_key >= :v2")
            .expressionAttributeValues(expressionAttributeValues)
            .scanIndexForward(true)
            .build();

        final QueryResponse queryResponse = DDB.query(queryRequest);

        AlphabetOutUtil.printAll(queryResponse);
    }
}
