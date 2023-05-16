package aws.ddb.snippets;

import software.amazon.awssdk.services.dynamodb.model.QueryResponse;

public class AlphabetOutUtil {

    public static void printAll(final QueryResponse queryResponse) {

        System.out.println("Result count = " + queryResponse.count());
        queryResponse.items().forEach(item -> {
            System.out.println(
                AlphabetUtil.alphabetItemToString(item));
        });
    }
}
