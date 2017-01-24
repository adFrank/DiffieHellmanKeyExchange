import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.ExecutionException;

/**
 * Created by oezert on 24/01/2017.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        int port = 8001;

        Random random = new Random();
        BigInteger p = new BigInteger(128, 64, random);
        BigInteger g = new BigInteger("32");

        Communicator alice = new Communicator();
        Communicator bob = new Communicator();

        String aliceMessage = alice.calculateGpowSecretModP(g, p);
        String bobMessage = bob.calculateGpowSecretModP(g, p);

        bob.listen(port);
        alice.send(port, aliceMessage);

        //alice.listen(port);
        //bob.send(port, bobMessage);
    }

}
