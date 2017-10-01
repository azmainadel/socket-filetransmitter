import java.io.*;
import java.net.Socket;

/**
 * Created by xyntherys on 10/1/17.
 */
public class FileSender {
    private Socket socket = null;
    private FileHandler fileHandler = new FileHandler();

    private int receiverStudentID = 0;

    private PrintWriter printWriter = null;
    private BufferedReader bufferedReader = null;

    public FileSender(Socket socket, int receiverStudentID, FileHandler fileHandler) {
        this.socket = socket;
        this.receiverStudentID = receiverStudentID;
        this.fileHandler = fileHandler;
    }

    public FileSender(Socket socket, FileHandler fileHandler) throws IOException {
        this.socket = socket;
        this.fileHandler = fileHandler;
    }

    public void sendFile() throws IOException {

        String fileName = fileHandler.getFileName();
        File file = new File(fileHandler.getFileLocation());

        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        printWriter = new PrintWriter(socket.getOutputStream());

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

            System.out.println("Read " + fileSize + " bytes");
        }

        fileInputStream.read(fileBytes, 0, fileSize);
        dataOutputStream.write(fileBytes, 0, fileSize);

        fileInputStream.close();
        dataOutputStream.close();

        System.out.println("File: " + fileHandler.getFileLocation() + " sent to Server successfully.");
    }
}
