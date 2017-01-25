import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.Future;

/**
 * Created by oezert on 24/01/2017.
 */
public class Main {

    public static void main(String[] args) {

        BigInteger p = new BigInteger(128, 64, new Random());
        BigInteger g = new BigInteger("32");

        Communicator alice = new Communicator(p, g);
        Communicator bob = new Communicator(p, g);
        Communicator eve = new Communicator(p, g);

        diffieHellmanKeyExchange(alice, bob);
        manInTheMiddleAttack(alice, bob, eve);

    }

    public static void diffieHellmanKeyExchange(Communicator alice, Communicator bob) {

        int port1 = 8000;
        int port2 = 8001;

        //Bob listens for Alice's public key
        Future<BigInteger> alicesPublicKeyReceivedByBob = bob.listen(port1);
        //Alice sends her public key to Bob
        alice.send(port1, alice.calculatePublicKey().toString());

        //Alice listens for Bob's public key
        Future<BigInteger> bobsPublicKeyReceivedByAlice = alice.listen(port2);
        //Bob sends his public key to Alice
        bob.send(port2, bob.calculatePublicKey().toString());

        try {
            BigInteger alicesPublicKey = alicesPublicKeyReceivedByBob.get();
            BigInteger bobsPublicKey = bobsPublicKeyReceivedByAlice.get();

            // have to be the same
            System.out.println("###################### Diffie-Hellman key exchange ######################\n");
            System.out.println("Common key calculated by Bob:   " + bob.calculateCommonKey(alicesPublicKey));
            System.out.println("Common key calculated by Alice: " + alice.calculateCommonKey(bobsPublicKey) + "\n");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void manInTheMiddleAttack(Communicator alice, Communicator bob, Communicator eve) {

        int port1 = 9000;
        int port2 = 9001;
        int port3 = 9002;
        int port4 = 9003;

        //Eve listens for Alice's public key
        Future<BigInteger> alicesPublicKeyReceivedByEve = eve.listen(port1);
        //Alice sends her public key to Bob (Eve)
        alice.send(port1, alice.calculatePublicKey().toString());

        //Eve listens for Bob's public key
        Future<BigInteger> bobsPublicKeyReceivedByEve = eve.listen(port2);
        //Bob sends his public key to Alice (Eve)
        bob.send(port2, bob.calculatePublicKey().toString());

        //Bob listens for Alice's public key
        Future<BigInteger> evesPublicKeyReceivedByBob = bob.listen(port3);
        //Eve sends her public key to Bob
        eve.send(port3, eve.calculatePublicKey().toString());

        //Alice listens for Bob's public key
        Future<BigInteger> evesPublicKeyReceivedByAlice = alice.listen(port4);
        //Eve sends her public key to Alice
        eve.send(port4, eve.calculatePublicKey().toString());

        try {
            BigInteger alicesPublicKey = alicesPublicKeyReceivedByEve.get();
            BigInteger bobsPublicKey = bobsPublicKeyReceivedByEve.get();
            BigInteger evesPublicKeyForBob = evesPublicKeyReceivedByBob.get();
            BigInteger evesPublicKeyForAlice = evesPublicKeyReceivedByAlice.get();

            // have to be the same
            System.out.println("########################### Man-in-the-middle ###########################\n");
            System.out.println("Alice and Eve's common key calculated by Eve:   " + eve.calculateCommonKey(alicesPublicKey));
            System.out.println("Alice and Eve's common key calculated by Alice: " + alice.calculateCommonKey(evesPublicKeyForAlice) + "\n");
            // have to be the same
            System.out.println("Bob and Eve's common key calculated by Eve: " + eve.calculateCommonKey(bobsPublicKey));
            System.out.println("Bob and Eve's common key calculated by Bob: " + bob.calculateCommonKey(evesPublicKeyForBob));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

}
