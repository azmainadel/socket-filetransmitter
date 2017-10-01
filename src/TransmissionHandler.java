import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xyntherys on 9/30/17.
 */

public class TransmissionHandler implements Runnable {
    private Socket socket = null;
    private InputStream inputStream = null;
    private FileOutputStream fileOutputStream = null;
    private BufferedReader bufferedReader = null;

    private int senderStudentID;
    private int receiverStudentID;
    private String fileName;
    private long fileSize;
    private String fileID;
    private String ipAddress;
    private int port;
    private static String serverAddress = "/home/xyntherys/Downloads/Server/";

    private static Map<Integer, String> map = new HashMap<Integer, String>();

    public TransmissionHandler(Socket socket, int senderStudentID, Map<Integer, NetworkAddress> map, BufferedReader bufferedReader) throws IOException {

        this.socket = socket;

        this.bufferedReader = bufferedReader;
        this.inputStream = this.socket.getInputStream();
        this.fileOutputStream = (FileOutputStream) this.socket.getOutputStream();
        this.senderStudentID = senderStudentID;
        this.ipAddress = socket.getInetAddress().toString();
        this.port = socket.getPort();

        map.putAll(map);

    }

    @Override
    public void run() {

        BufferedReader bufferedReader = this.bufferedReader;
        PrintWriter printWriter = new PrintWriter(this.fileOutputStream);

        try {
            fileName = bufferedReader.readLine();
            fileSize = Long.parseLong(bufferedReader.readLine());

            printWriter.flush();

            File file = new File(serverAddress + fileName);

            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            fileOutputStream.flush();

            byte[] fileBytes = new byte[4096];

            int read = 0;
            int totalRead = 0;
            int remaining = (int) fileSize;

            System.out.println(remaining);

            while (remaining > 4096) {
                read = inputStream.read(fileBytes);
                totalRead += read;
                remaining -= read;
                System.out.println("read " + totalRead + " bytes.");

                fileOutputStream.write(fileBytes);
            }

            inputStream.read(fileBytes, 0, remaining);
            fileOutputStream.write(fileBytes, 0, remaining);

            System.out.println("read " + (totalRead + remaining) + " bytes.");

            fileOutputStream.close();
            inputStream.close();

            System.out.println("File received successfully.");
            System.out.println("File stored at location: " + serverAddress + fileName);

        }
        catch (IOException e){
            e.printStackTrace();
        }

    }
}
