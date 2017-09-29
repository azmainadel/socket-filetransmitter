import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.io.*;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Created by xyntherys on 9/29/17.
 */

public class Client {
    private Socket socket = null;
    private ObjectOutputStream outputStream = null;
    private boolean isConnected = false;
    private String clientAddress;
//    private String sourceAddress = "/home/xyntherys/Downloads/Client/Prospectus.pdf";
    private String sourceAddress;
    private String destinationAddress = "/home/xyntherys/Downloads/Server/";
    private FileHandler fileHandler = null;

    public void setSourceAddress(String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    public String getSourceAddress() {
        return sourceAddress;
    }

    public String getClientAddress() throws UnknownHostException {
        return InetAddress.getLocalHost().toString();
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

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
                FileOutputStream fileOutputStream = new FileOutputStream(file);

//                long chunkSize = (8 * 1024);
//                long totalChunk = file.length() / chunkSize;
//                byte[] fileBytes = new byte[(int) totalChunk];
//                for(int i = 0; i <= totalChunk; i++){
//                    dataInputStream.read(fileBytes);
//                }
//
//                long remainingBytes = file.length() - (totalChunk * 1024);
//                byte[] remainingFileBytes = new byte[(int) remainingBytes];
//                dataInputStream.read(remainingFileBytes);
//
//                System.out.println("REMAINING");

//                byte[] fileBytes = new byte[(int) file.length()];
//                int read = 0;
//                int numRead = 0;
//                while ((fileBytes.length > read) && ((numRead = dataInputStream.read(fileBytes, read, fileBytes.length - read)) >= 0)) {
//                    read = read + numRead;
//                }

                byte[] fileBytes = new byte[(int) file.length()];
                dataInputStream.read(fileBytes);
                fileOutputStream.write(fileBytes);

                fileHandler.setFileSize(file.length());
//                fileHandler.setFileData(fileBytes);
                fileHandler.setFileStatus("Success");

                System.out.println("File sent to Server successfully.");

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

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter file address: ");
        client.setSourceAddress(scanner.next());

        System.out.println("Enter receiver StudentID: ");

        client.sendFile();
    }
}
