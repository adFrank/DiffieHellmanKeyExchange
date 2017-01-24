import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Callable;

/**
 * Created by oezert on 24/01/2017.
 */
public class Server implements Callable<BigInteger> {
    private int port;

    public Server(int port) {
        this.port = port;
    }

    @Override
    public BigInteger call() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            Socket socket = serverSocket.accept();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            String line;
            if ((line = in.readLine()) != null) {
                return new BigInteger(line);
            }

            socket.close();
            serverSocket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
