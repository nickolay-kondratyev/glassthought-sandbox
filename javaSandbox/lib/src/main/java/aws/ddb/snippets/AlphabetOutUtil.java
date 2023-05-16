package aws.ddb.snippets;

import software.amazon.awssdk.services.dynamodb.model.QueryResponse;

public class AlphabetOutUtil {

    static void printAll(final QueryResponse queryResponse) {

        queryResponse.items().forEach(item -> {
            System.out.println(
                AlphabetUtil.alphabetItemToString(item));
        });
    }
}
