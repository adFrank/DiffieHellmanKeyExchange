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

        Communicator alice = new Communicator(p, g);
        Communicator bob = new Communicator(p, g);
        Communicator eve = new Communicator(p, g);

        diffieHellmanKeyExchange(alice, bob, port1, port2);
        manInTheMiddleAttack(alice, bob, eve);
    }

    public static void diffieHellmanKeyExchange(Communicator alice, Communicator bob, int port1, int port2) {
        //Bob listens for Alice's public key
        Future<BigInteger> alicesPublicKeyReceivedByBob = bob.listen(port1);
        //Alice sends her public key to Bob
        alice.send(port1, alice.getPublicKey().toString());

        //Alice listens for Bob's public key
        Future<BigInteger> bobsPublicKeyReceivedByAlice = alice.listen(port2);
        //Bob sends his public key to Alice
        bob.send(port2, bob.getPublicKey().toString());

        try {
            BigInteger alicesPublicKey = alicesPublicKeyReceivedByBob.get();
            BigInteger bobsPublicKey = bobsPublicKeyReceivedByAlice.get();

            // have to be different
            System.out.println("Alice's public key received by Bob: " + alicesPublicKey);
            System.out.println("Bob's public key received by Alice: " + bobsPublicKey);

            BigInteger commonKeyCalculatedByBob = bob.calculateCommonKey(alicesPublicKey);
            BigInteger commonKeyCalculatedByAlice = alice.calculateCommonKey(bobsPublicKey);

            // have to be the same
            System.out.println("Common key calculated by Bob:   " + commonKeyCalculatedByBob);
            System.out.println("Common key calculated by Alice: " + commonKeyCalculatedByAlice);
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
        alice.send(port1, alice.getPublicKey().toString());

        //Eve listens for Bob's public key
        Future<BigInteger> bobsPublicKeyReceivedByEve = eve.listen(port2);
        //Bob sends his public key to Alice (Eve)
        bob.send(port2, bob.getPublicKey().toString());

        //Bob listens for Alice's public key
        Future<BigInteger> evesPublicKeyReceivedByBob = bob.listen(port3);
        //Eve sends her public key to Bob
        eve.send(port3, eve.getPublicKey().toString());

        //Alice listens for Bob's public key
        Future<BigInteger> evesPublicKeyReceivedByAlice = alice.listen(port4);
        //Eve sends her public key to Alice
        eve.send(port4, eve.getPublicKey().toString());

        try {
            BigInteger alicesPublicKey = alicesPublicKeyReceivedByEve.get();
            BigInteger bobsPublicKey = bobsPublicKeyReceivedByEve.get();
            BigInteger evesPublicKeyForBob = evesPublicKeyReceivedByBob.get();
            BigInteger evesPublicKeyForAlice = evesPublicKeyReceivedByAlice.get();


            // have to be different
            System.out.println("Alice's public key received by Eve: " + alicesPublicKey);
            System.out.println("Bob's public key received by Eve: " + bobsPublicKey);
            System.out.println();
            System.out.println("Eve's public key received by Bob: " + evesPublicKeyForBob);
            System.out.println("Eve's public key received by Alice: " + evesPublicKeyForAlice);
            System.out.println();

            BigInteger AliceAndEvesCommonKeyCalculatedByEve = eve.calculateCommonKey(alicesPublicKey);
            BigInteger AliceAndEvesCommonKeyCalculatedByAlice = alice.calculateCommonKey(evesPublicKeyForAlice);
            BigInteger BobAndEvesCommonKeyCalculatedByEve = eve.calculateCommonKey(bobsPublicKey);
            BigInteger BobAndEvesCommonKeyCalculatedByBob = bob.calculateCommonKey(evesPublicKeyForBob);


            // have to be the same
            System.out.println("Alice and Eve's common key calculated by Eve:   " + AliceAndEvesCommonKeyCalculatedByEve);
            System.out.println("Alice and Eve's common key calculated by Alice: " + AliceAndEvesCommonKeyCalculatedByAlice);
            System.out.println();
            System.out.println("Bob and Eve's common key calculated by Eve: " + BobAndEvesCommonKeyCalculatedByEve);
            System.out.println("Bob and Eve's common key calculated by Bob: " + BobAndEvesCommonKeyCalculatedByBob);

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

}
