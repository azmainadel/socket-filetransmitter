import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by xyntherys on 9/29/17.
 */
public class Server {
    private ServerSocket serverSocket = null;
    private Socket socket = null;
    private ObjectInputStream inputStream = null;
    private DataInputStream dataInputStream = null;
    private FileOutputStream fileOutputStream = null;
    private FileHandler fileHandler;
    private File file = null;

    public Server() {

    }

    public void connect() {

        try {
            serverSocket = new ServerSocket(4445);
            socket = serverSocket.accept();
            inputStream = new ObjectInputStream(socket.getInputStream());
            dataInputStream = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void receiveFile() {

        try {
            fileHandler = (FileHandler) inputStream.readObject();

            if (fileHandler.getFileStatus().equals("Error")) {
                System.out.println("Error in reading the File...");
                System.exit(0);
            }
            
            String fileName = fileHandler.getFileDestination() + fileHandler.getFileName();
            
            if (!new File(fileHandler.getFileDestination()).exists()) {
                new File(fileHandler.getFileDestination()).mkdirs();
            }
            file = new File(fileName);
            fileOutputStream = new FileOutputStream(file);

            byte[] fileBytes = new byte[(int) file.length()];
            dataInputStream.read(fileBytes);
            fileOutputStream.write(fileBytes);
//            fileOutputStream.write(fileHandler.getFileData());
//            fileOutputStream.flush();
//            fileOutputStream.close();

            System.out.println("Output file: " + fileName + " has been successfully received.");
//            Thread.sleep(3000);
//            System.exit(0);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.connect();
        server.receiveFile();
    }
}
