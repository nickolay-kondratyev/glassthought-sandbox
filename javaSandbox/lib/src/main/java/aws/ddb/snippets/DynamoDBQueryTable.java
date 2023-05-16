package aws.ddb.snippets;

import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;

public class DynamoDBQueryTable {

    private static final String TABLE_NAME = Constants.HashKeyRangeKeyTable.TABLE_NAME;
    private static final String HASH_KEY_VALUE = "alphabet_200KB_per_row_with_GUID_in_SortKey";
    private static final DynamoDbClient ddb;

    static {
        ddb = software.amazon.awssdk.services.dynamodb.DynamoDbClient.builder()
            .region(Region.US_WEST_2)
            .build();
    }

    public static void main(String[] args) {

        queryTableInOrder(SortOrder.ASCENDING);
        queryTableInOrder(SortOrder.DESCENDING);
    }

    private static void queryTableInOrder(final SortOrder sortOrder) {

        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("Querying in " + sortOrder + " order of sort_key");

        final Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":v1", AttributeValue.builder().s(HASH_KEY_VALUE).build());

        final QueryRequest queryRequest = QueryRequest.builder()
            .tableName(TABLE_NAME)
            .keyConditionExpression("hash_key = :v1")
            .expressionAttributeValues(expressionAttributeValues)
            .scanIndexForward(sortOrder == SortOrder.ASCENDING)
            .build();

        final QueryResponse queryResponse = ddb.query(queryRequest);

        queryResponse.items().forEach(item -> {
            System.out.println(
                "sort_key: " + item.get("sort_key").s() +
                ", letter: " + item.get("letter").s() +
                ", value: " + item.get("value").s().substring(0, 10) +
                "... value length=(" + item.get("value").s().length() + ")");
        });
    }
}
