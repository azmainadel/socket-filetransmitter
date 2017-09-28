import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.io.*;
/**
 * Created by xyntherys on 9/29/17.
 */
public class Client {
    private Socket socket = null;
    private ObjectOutputStream outputStream = null;
    private boolean isConnected = false;
    private String sourceAddress = "/home/xyntherys/Downloads/Client/Prospectus.pdf";
    private String destinationAddress = "/home/xyntherys/Downloads/Server/";
    private FileHandler fileHandler = null;

    public Client() {

    }

    public void connect() {
        while (!isConnected) {
            try {
                socket = new Socket("localHost", 4445);
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                isConnected = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendFile() {
        fileHandler = new FileHandler();

        String fileName = sourceAddress.substring(sourceAddress.lastIndexOf("/") + 1, sourceAddress.length());
//        String fileLocation = sourceAddress.substring(0, sourceAddress.lastIndexOf("/") + 1);

        fileHandler.setFileDestination(destinationAddress);
        fileHandler.setFileName(fileName);
        fileHandler.setFileSource(sourceAddress);
        File file = new File(sourceAddress);

        if (file.isFile()) {
            try {
                DataInputStream dataInputStream = new DataInputStream(new FileInputStream(file));
                long len = (int) file.length();
                byte[] fileBytes = new byte[(int) len];
                int read = 0;
                int numRead = 0;
                while (fileBytes.length > read && (((numRead = dataInputStream.read(fileBytes, read, fileBytes.length - read))) >= 0)) {
                    read = read + numRead;
                }
                fileHandler.setFileSize(len);
                fileHandler.setFileData(fileBytes);
                fileHandler.setFileStatus("Success");

            } catch (Exception e) {
                e.printStackTrace();
                fileHandler.setFileStatus("Error");
            }
        }
        else {
            System.out.println("Wrong input path.\n Please check again.");
            fileHandler.setFileStatus("Error");
        }
        
        try {
            outputStream.writeObject(fileHandler);
            System.out.println("File sent to Server successfully.");
//            Thread.sleep(3000);
//            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        Client client = new Client();
        client.connect();
        client.sendFile();
    }
}
