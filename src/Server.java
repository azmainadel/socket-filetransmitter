import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by xyntherys on 9/29/17.
 */

public class Server {
    private ServerSocket serverSocket = null;
    private Socket socket = null;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;
    private File file = null;

    public Server() {

    }

    public void connect() {

        try {
            serverSocket = new ServerSocket(4445);
            socket = serverSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void receiveFile() throws IOException {

        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        printWriter = new PrintWriter(socket.getOutputStream());

        String fileName = bufferedReader.readLine();
        long fileSize = Long.parseLong(bufferedReader.readLine());

        printWriter.flush();

        file = new File("/home/xyntherys/Downloads/Server/" + fileName);

        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        FileOutputStream fileOutputStream = new FileOutputStream(file);

        byte[] fileBytes = new byte[4096];

        int read = 0;
        int totalRead = 0;
        int remaining = (int) fileSize;

        System.out.println(remaining);

//        while(remaining > 4096) {
//            read = dataInputStream.read(fileBytes);
//            totalRead += read;
//            remaining -= read;
//            System.out.println("read " + totalRead + " bytes.");
//
//            fileOutputStream.write(fileBytes);
//        }

        while((read = dataInputStream.read(fileBytes)) >= 0) {
            totalRead += read;
            remaining -= read;
            System.out.println("read " + totalRead + " bytes.");

            fileOutputStream.write(fileBytes);
        }

        dataInputStream.read(fileBytes, 0, remaining);
        fileOutputStream.write(fileBytes, 0, remaining);

        fileOutputStream.close();
        dataInputStream.close();
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.connect();
        server.receiveFile();
    }
}
