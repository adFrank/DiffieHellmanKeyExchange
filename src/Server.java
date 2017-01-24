import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * Created by oezert on 24/01/2017.
 */
public class Server {
    String str;
    public String start(int port) throws InterruptedException, ExecutionException {
        (new Thread(() -> {
            try {

                ServerSocket ss = new ServerSocket(port);

                Socket s = ss.accept();

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(s.getInputStream()));
                String line;
                if ((line = in.readLine()) != null) {
                    System.out.println("line: " + line);
                    str = line;
                    Thread.yield();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        })).start();
        System.out.println("msg1: " + str);

        return str;
    }
}
