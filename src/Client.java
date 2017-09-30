import java.io.IOException;
import java.net.Socket;
import java.io.*;
import java.util.Scanner;

/**
 * Created by xyntherys on 9/29/17.
 */

public class Client {
    private Socket socket = null;
    private String sourceAddress = null;

    private FileHandler fileHandler = new FileHandler();

    private int senderStudentID = 0;
    private int receiverStudentID = 0;

    private PrintWriter printWriter = null;
    private BufferedReader bufferedReader = null;


    public Client() {

    }

    public void connect() {
        try {
            socket = new Socket("localHost", 4445);
            System.out.println("----FILE TRANSMISSION SYSTEM RUNNING----");

        } catch (IOException e) {
            System.err.println("Can not connect to the server. Please try again.");
            e.printStackTrace();
        }
    }


    public void sendFile() throws IOException {

//        String fileName = sourceAddress.substring(sourceAddress.lastIndexOf("/") + 1, sourceAddress.length());
//
//        File file = new File(sourceAddress);

        String fileName = fileHandler.getFileName();
        File file = new File(fileHandler.getFileLocation());

        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        printWriter = new PrintWriter(socket.getOutputStream());

//        printWriter.println(senderStudentID);
//        printWriter.println(receiverStudentID);

        printWriter.println(fileName);
        printWriter.println(file.length());
        printWriter.flush();

        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        FileInputStream fileInputStream = new FileInputStream(file);

        dataOutputStream.flush();


        byte[] fileBytes = new byte[4096];
        int fileSize = (int) file.length();
        int read;

        while (fileSize > 4096 ) {
            read = fileInputStream.read(fileBytes);
            fileSize -= read;
            dataOutputStream.write(fileBytes);

            System.out.println(fileSize);
        }

        fileInputStream.read(fileBytes, 0, fileSize);
        dataOutputStream.write(fileBytes, 0, fileSize);

        fileInputStream.close();
        dataOutputStream.close();

        System.out.println("File: " + fileHandler.getFileLocation() + " sent to Server successfully.");
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.connect();

        Scanner scanner = new Scanner(System.in);

//        System.out.println("Enter your StudentID: ");
//        client.senderStudentID = scanner.nextInt();
//
//        System.out.println("Enter receiver StudentID: ");
//        client.receiverStudentID = scanner.nextInt();

        System.out.println("Enter the address of the file to be sent: ");
//        client.sourceAddress = scanner.next();
        client.fileHandler.setFileLocation(scanner.next());
        client.sendFile();
    }
}
