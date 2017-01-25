import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Communicator {
    private BigInteger privateKey;
    private BigInteger p;
    private BigInteger g;

    public Communicator(BigInteger p, BigInteger g) {
        this.p = p;
        this.g = g;
        this.privateKey = randomBigint(p);
    }

    public Future listen(int port) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        return executor.submit(new Server(port));
    }

    public void send(int port, String message) {
        new Client().send(port, message);
    }

    public BigInteger calculatePublicKey() {
         return g.modPow(privateKey, p);
    }

    private BigInteger randomBigint(BigInteger max) {
        Random randomGenerator = new Random();
        BigInteger result;
        do {
            result = new BigInteger(max.bitLength(), randomGenerator);
        } while (!(result.compareTo(BigInteger.valueOf(2)) >= 0 && result.compareTo(max) <= 0));
        return result;
    }

    public BigInteger calculateCommonKey(BigInteger receivedPublicKey) {
        return receivedPublicKey.modPow(privateKey, p);
    }
}
