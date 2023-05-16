package aws.ddb.snippets;

import java.util.Map;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class AlphabetUtil {

    static String alphabetItemToString(final Map<String, AttributeValue> item) {

        StringBuilder sb = new StringBuilder();

        sb.append( "sort_key: " + item.get("sort_key").s() +
                   ", letter: " + item.get("letter").s() +
                   ", value: " + item.get("value").s().substring(0, 10) +
                   "... value length=(" + item.get("value").s().length() + ")");

        if (item.containsKey("timestamp")){
            sb.append(", timestamp: " + item.get("timestamp").s());
        }
        return sb.toString();
    }
}
