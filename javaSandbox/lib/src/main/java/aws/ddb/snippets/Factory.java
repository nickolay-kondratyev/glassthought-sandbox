package aws.ddb.snippets;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class Factory {

    public static DynamoDbClient getClient() {

        return DynamoDbClient.builder()
            .region(Region.US_WEST_2)
            .build();
    }
}
