package aws.ddb.snippets.lsi;

import aws.ddb.snippets.Constants;
import aws.ddb.snippets.Factory;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.BillingMode;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.CreateTableResponse;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.KeyType;
import software.amazon.awssdk.services.dynamodb.model.LocalSecondaryIndex;
import software.amazon.awssdk.services.dynamodb.model.Projection;
import software.amazon.awssdk.services.dynamodb.model.ProjectionType;
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType;

public class CreateLSITable {

    public static void main(String[] args) throws Exception {

        createTable();
    }

    private static void createTable() {

        final String tableName = Constants.LSI_BY_TIMESTAMP_TABLE_NAME;  // replace with your table name
        final String lsiName = "timestamp-index";

        final CreateTableRequest request = CreateTableRequest.builder()
            .tableName(tableName)
            .keySchema(
                KeySchemaElement.builder().attributeName("hash_key").keyType(KeyType.HASH).build(),
                KeySchemaElement.builder().attributeName("sort_key").keyType(KeyType.RANGE).build())
            .attributeDefinitions(
                AttributeDefinition.builder().attributeName("hash_key").attributeType(ScalarAttributeType.S).build(),
                AttributeDefinition.builder().attributeName("sort_key").attributeType(ScalarAttributeType.S).build(),
                AttributeDefinition.builder().attributeName("timestamp").attributeType(ScalarAttributeType.S).build())
            .localSecondaryIndexes(LocalSecondaryIndex.builder()
                .indexName(lsiName)
                .keySchema(
                    KeySchemaElement.builder().attributeName("hash_key").keyType(KeyType.HASH).build(),
                    KeySchemaElement.builder().attributeName("timestamp").keyType(KeyType.RANGE).build())
                .projection(Projection.builder().projectionType(ProjectionType.ALL).build())
                .build())
            .billingMode(BillingMode.PAY_PER_REQUEST)
            .build();

        final DynamoDbClient ddb = Factory.getClient();

        final CreateTableResponse response = ddb.createTable(request);

        System.out.println("Table created: " + response.tableDescription().tableName());
    }

}
