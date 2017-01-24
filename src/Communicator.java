import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class Communicator {
    Server server;
    Client client;
    String receivedMessage;
    
    public void listen(int port) throws InterruptedException, ExecutionException {
        server = new Server();
        receivedMessage = server.start(port);
        System.out.println("receivedMessage: " + receivedMessage);
    }

    public void send(int port, String message) throws ExecutionException, InterruptedException {
        client = new Client();
        client.send(port, message);
    }

    public String calculateGpowSecretModP(BigInteger g, BigInteger p) {
        BigInteger secret = generateRandomSecret(p);
        return g.modPow(secret, p).toString();
    }

    private BigInteger generateRandomSecret(BigInteger p) {
        return randomBigint(p);
    }

    private BigInteger randomBigint(BigInteger max) {
        Random randomGenerator = new Random();
        BigInteger result;
        do {
            result = new BigInteger(max.bitLength(), randomGenerator);
        } while (!(result.compareTo(BigInteger.valueOf(2)) >= 0 && result.compareTo(max) <= 0));
        return result;
    }
}
