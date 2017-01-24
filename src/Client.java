import java.io.*;
import java.net.Socket;

/**
 * Created by oezert on 23/01/2017.
 */
public class Client {

    public void send(int port, String message) {

        (new Thread(() -> {
            try {
                Socket socket = new Socket("localhost", port);
                BufferedWriter out = new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream()));

                out.write(message);
                out.newLine();
                out.flush();
                socket.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        })).start();

    }

}
