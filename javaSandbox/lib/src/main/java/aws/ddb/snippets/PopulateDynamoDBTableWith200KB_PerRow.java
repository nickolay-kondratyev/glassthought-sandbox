package aws.ddb.snippets;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

public class PopulateDynamoDBTableWith200KB_PerRow {


    public static void main(String[] args) {

        final DynamoDbClient ddb = Factory.getClient();

        final char[] alphabet = getUppercaseAlphabet();

        // Generate a 200KB string for each letter
        for (final char letter : alphabet) {
            // repeat letter to create a 200KB string
            final String value = DynamoSnippetUtil.repeatChar(letter, 200 * 1024);

            final Map<String, AttributeValue> item = new HashMap<>();
            item.put("hash_key", AttributeValue.builder().s("alphabet_200KB_per_row_with_GUID_in_SortKey").build());
            final String sortKey = (int) letter + "__" + UUID.randomUUID();
            item.put("sort_key", AttributeValue.builder().s(sortKey).build());
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

    private static char[] getUppercaseAlphabet() {

        return "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    }

}
