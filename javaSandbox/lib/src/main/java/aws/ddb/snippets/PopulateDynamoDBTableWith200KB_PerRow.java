package aws.ddb.snippets;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

public class PopulateDynamoDBTableWith200KB_PerRow {


    public static void main(String[] args) {
        final DynamoDbClient ddb = Factory.getClient();

        final char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

        // Generate a 200KB string for each letter
        for (final char letter : alphabet) {
            // repeat letter to create a 200KB string
            final String value = repeatChar(letter, 200 * 1024);

            final Map<String, AttributeValue> item = new HashMap<>();
            item.put("hash_key", AttributeValue.builder().s("alphabet_200KB_per_row").build());
            item.put("sort_key", AttributeValue.builder().s(String.valueOf((int) letter)).build());
            item.put("letter", AttributeValue.builder().s(String.valueOf(letter)).build());
            item.put("value", AttributeValue.builder().s(value).build());

            final PutItemRequest request = PutItemRequest.builder()
                .tableName(Constants.HashKeyRangeKeyTable.TABLE_NAME)
                .item(item)
                .build();

            ddb.putItem(request);

            System.out.println("Put item for letter: " + letter);
        }
    }

    private static String repeatChar(char c, int size) {
        StringBuilder sb = new StringBuilder(size);
        IntStream.range(0, size).forEach(i -> sb.append(c));
        return sb.toString();
    }
}
