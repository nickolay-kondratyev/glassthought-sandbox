package aws.ddb.snippets.lsi;

import java.util.HashMap;
import java.util.Map;

import aws.ddb.snippets.AlphabetOutUtil;
import aws.ddb.snippets.Constants;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;

public class QueryLSI {
    
    private static final DynamoDbClient DDB = DynamoDbClient.builder()
        .region(Region.US_WEST_2)
        .build();

    public static void main(String[] args) {

        queryTable();
    }

    private static void queryTable() {
        final String tableName = Constants.LSI_BY_TIMESTAMP_TABLE_NAME;
        final String lsiName = "timestamp-index";
        final String hashKeyValue = "alphabet_200KB_per_row";
        final String timestampValue = "2017-07-09T15:00:00Z";  // Replace with the timestamp value you want to query

        Map<String, String> expressionAttributeNames = new HashMap<>();
        expressionAttributeNames.put("#ts", "timestamp");

        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":v1", AttributeValue.builder().s(hashKeyValue).build());
        expressionAttributeValues.put(":v2", AttributeValue.builder().s(timestampValue).build());

        QueryRequest queryRequest = QueryRequest.builder()
            .tableName(tableName)
            .indexName(lsiName)
            .keyConditionExpression("hash_key = :v1 and #ts >= :v2")  // Changed "=" to "<" to query for items with a timestamp less than the given value
            .expressionAttributeNames(expressionAttributeNames)
            .expressionAttributeValues(expressionAttributeValues)
            .build();

        QueryResponse queryResponse = DDB.query(queryRequest);

        AlphabetOutUtil.printAll(queryResponse);

    }


}
