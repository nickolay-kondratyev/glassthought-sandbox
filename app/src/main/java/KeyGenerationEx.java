import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;

/** https://www.novixys.com/blog/how-to-generate-rsa-keys-java/ */
public class KeyGenerationEx {

    public static final String KEY_PREFIX = "/tmp/rsa";

    public static void main(String[] args) throws Exception {

        System.out.println("HI");

        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");

        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();

        Key pub = kp.getPublic();
        Key pvt = kp.getPrivate();

        savePrivate(pvt);
        savePublic(kp);
    }

    private static void savePrivate(final Key pvt) throws IOException {

        Base64.Encoder encoder = Base64.getEncoder();

        String outFile = KEY_PREFIX;
        final String privateKeyPath = outFile + ".private";
        Writer out = new FileWriter(privateKeyPath);
        out.write("-----BEGIN RSA PRIVATE KEY-----\n");
        out.write(encoder.encodeToString(pvt.getEncoded()));
        out.write("\n-----END RSA PRIVATE KEY-----\n");
        out.close();
        System.out.println("Wrote to " + privateKeyPath);
    }

    private static void savePublic(final KeyPair kp) throws IOException {

        Base64.Encoder encoder = Base64.getEncoder();

        final String publicKeyPath = KEY_PREFIX + ".pub";
        Writer out = new FileWriter(publicKeyPath);
        out.write("-----BEGIN RSA PUBLIC KEY-----\n");
        out.write(encoder.encodeToString(kp.getPublic().getEncoded()));
        out.write("\n-----END RSA PUBLIC KEY-----\n");
        out.close();
        System.out.println("Wrote to " + publicKeyPath);
    }

}
