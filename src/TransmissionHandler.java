import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xyntherys on 9/30/17.
 */
public class TransmissionHandler implements Runnable {
    private Socket socket = null;
    private DataInputStream dataInputStream = null;
    private FileOutputStream fileOutputStream = null;

    private int senderStudentID = 0;
    private int receiverStudentID = 0;
    private String fileName = null;
    private long fileSize = 0;
    private String fileID = null;
    private String ipAddress = null;
    private static String serverAddress = "/home/xyntherys/Downloads/Server/";

    private static Map<Integer, String> map = new HashMap<Integer, String>();

    public TransmissionHandler(Socket socket, int senderStudentID, Map<Integer, String> map) throws IOException {
        this.socket = socket;

        this.dataInputStream = (DataInputStream) this.socket.getInputStream();
        this.fileOutputStream = (FileOutputStream) this.socket.getOutputStream();
        this.senderStudentID = senderStudentID;
        this.ipAddress = socket.getInetAddress().toString();
        map.putAll(map);
    }

    @Override
    public void run() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.dataInputStream));
        PrintWriter printWriter = new PrintWriter(this.fileOutputStream);

    }
}
