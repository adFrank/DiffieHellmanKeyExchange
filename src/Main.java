import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.Future;

/**
 * Created by oezert on 24/01/2017.
 */
public class Main {
    public static void main(String[] args) {
        int port1 = 8000;
        int port2 = 8001;

        BigInteger p = new BigInteger(128, 64, new Random());
        BigInteger g = new BigInteger("32");

        Communicator alice = new Communicator();
        Communicator bob = new Communicator();

        //Bob listens for Alice's combined key
        Future<BigInteger> bobsFutureReceivedMessageFromAlice = bob.listen(port1);
        //Alice sends her combined Key to Bob
        alice.send(port1, alice.calculateGpowSecretModP(g, p).toString());

        //Alice listens for Bob's combined key
        Future<BigInteger> alicesFutureReceivedMessageFromBob = alice.listen(port2);
        //Bob sends his combined Key to Alice
        bob.send(port2, bob.calculateGpowSecretModP(g, p).toString());

        try {
            BigInteger bobsReceivedMessageFromAlice = bobsFutureReceivedMessageFromAlice.get();
            BigInteger alicesReceivedMessageFromBob = alicesFutureReceivedMessageFromBob.get();


            BigInteger calculatedCommonSecretBob = bob.calculateCommonSecret(bobsReceivedMessageFromAlice, p);
            BigInteger calculatedCommonSecretAlice = alice.calculateCommonSecret(alicesReceivedMessageFromBob, p);

            // have to be different
            System.out.println("bobsReceivedMessageFromAlice: " + bobsReceivedMessageFromAlice);
            System.out.println("alicesReceivedMessageFromBob: " + alicesReceivedMessageFromBob);

            // have to be the same
            System.out.println("calculatedCommonSecretBob:   " + calculatedCommonSecretBob);
            System.out.println("calculatedCommonSecretAlice: " + calculatedCommonSecretAlice);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
