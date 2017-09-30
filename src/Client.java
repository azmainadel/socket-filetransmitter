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

    private PrintWriter printWriter = null;
    private BufferedReader bufferedReader = null;

    public void setSourceAddress(String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    public String getSourceAddress() {
        return sourceAddress;
    }

    public Client() {

    }

    public void connect() {
        try {
            socket = new Socket("localHost", 4445);
        } catch (IOException e) {
            System.err.println("Can not connect to the server. Please try again.");
            e.printStackTrace();
        }
    }


    public void sendFile() throws IOException {

        String fileName = sourceAddress.substring(sourceAddress.lastIndexOf("/") + 1, sourceAddress.length());

        File file = new File(sourceAddress);

        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        printWriter = new PrintWriter(socket.getOutputStream());

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

        System.out.println("File: " + sourceAddress + " sent to Server successfully.");
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.connect();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter file address: ");
        client.setSourceAddress(scanner.next());

        System.out.println("Enter receiver StudentID: ");

        client.sendFile();
    }
}
