import java.io.*;
import java.net.Socket;

/**
 * Created by oezert on 23/01/2017.
 */
public class Client {
    public void send(int port, String message) {
        (new Thread(() -> {
            try {
                Socket s = new Socket("localhost", port);
                BufferedWriter out = new BufferedWriter(
                        new OutputStreamWriter(s.getOutputStream()));

                out.write(message);
                out.newLine();
                out.flush();

            } catch (Exception e) {
                e.printStackTrace();
            }
        })).start();
    }
}
