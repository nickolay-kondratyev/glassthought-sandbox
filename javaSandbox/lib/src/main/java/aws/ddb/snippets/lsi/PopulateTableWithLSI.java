package aws.ddb.snippets.lsi;


/** Uses table created with {@link aws.ddb.snippets.lsi.CreateLSITable} */

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import aws.ddb.snippets.Constants;
import aws.ddb.snippets.DynamoSnippetUtil;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

public class PopulateTableWithLSI {

    private static final DynamoDbClient DDB = DynamoDbClient.builder()
        .region(Region.US_WEST_2)
        .build();

    public static void main(String[] args) {

        populateTable();
    }

    private static void populateTable() {

        final String tableName = Constants.LSI_BY_TIMESTAMP_TABLE_NAME;

        final String hashKeyValue = "alphabet_200KB_per_row";
        final ZonedDateTime startDate = ZonedDateTime.parse("2017-07-06T15:00:00.000Z");
        final DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;

        for (char letter = 'A'; letter <= 'Z'; letter++) {
            final String sortKeyValue = String.valueOf((int) letter);
            final String timestampValue = startDate.plusDays(letter - 'A').format(formatter);
            final String value = DynamoSnippetUtil.repeatChar(letter, 199 * 1024);

            Map<String, AttributeValue> item = new HashMap<>();
            item.put("hash_key", AttributeValue.builder().s(hashKeyValue).build());
            item.put("sort_key", AttributeValue.builder().s(sortKeyValue).build());
            item.put("letter", AttributeValue.builder().s(String.valueOf(letter)).build());
            item.put("value", AttributeValue.builder().s(value).build());
            item.put("timestamp", AttributeValue.builder().s(timestampValue).build());

            PutItemRequest request = PutItemRequest.builder()
                .tableName(tableName)
                .item(item)
                .build();


            DDB.putItem(request);
            System.out.println("Put item for letter: " + letter);
        }

        System.out.println("Table populated: " + tableName);
    }
}
