import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.BinaryOperator;

public class Communicator {
    BigInteger secret;

    public Future listen(int port) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        return executor.submit(new Server(port));
    }

    public void send(int port, String message) {
        new Client().send(port, message);
    }

    public BigInteger calculateGpowSecretModP(BigInteger g, BigInteger p) {
        secret = randomBigint(p);
        return g.modPow(secret, p);
    }

    private BigInteger randomBigint(BigInteger max) {
        Random randomGenerator = new Random();
        BigInteger result;
        do {
            result = new BigInteger(max.bitLength(), randomGenerator);
        } while (!(result.compareTo(BigInteger.valueOf(2)) >= 0 && result.compareTo(max) <= 0));
        return result;
    }

    public BigInteger calculateCommonSecret(BigInteger receivedCommbinedKey, BigInteger p) {
        return receivedCommbinedKey.modPow(secret, p);
    }
}
