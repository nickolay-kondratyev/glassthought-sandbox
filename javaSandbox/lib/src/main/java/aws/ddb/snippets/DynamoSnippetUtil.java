package aws.ddb.snippets;

import java.util.stream.IntStream;

public class DynamoSnippetUtil {

    public static String repeatChar(char c, int size) {

        StringBuilder sb = new StringBuilder(size);
        IntStream.range(0, size).forEach(i -> sb.append(c));
        return sb.toString();
    }
}
