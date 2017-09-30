import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xyntherys on 9/29/17.
 */

public class Server {
    private ServerSocket serverSocket = null;
    private Socket socket = null;

    private BufferedReader bufferedReader = null;
    private PrintWriter printWriter = null;

    private static String serverAddress = "/home/xyntherys/Downloads/Server/";

    private int studentID = 0;
    private String ipAddress = null;
    private Map<Integer, String> clientMap = new HashMap<Integer, String>();

    public Server() {

    }

    public void connect() {

        try {
            serverSocket = new ServerSocket(4445);
            System.out.println("---SERVER STARTED----");
            socket = serverSocket.accept();


//            while(true) {
//                socket = serverSocket.accept();
//
//                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                studentID = Integer.parseInt(bufferedReader.readLine());
//                ipAddress = socket.getInetAddress().toString();
//
//                System.out.println("Connected with: Student ID " + studentID + " IP Address " + ipAddress);
//
//                if(clientMap.containsKey(studentID)){
//                    System.out.println("Student already logged in.");
//                    printWriter = new PrintWriter(socket.getOutputStream());
//
//                    printWriter.println("LOGGED_IN");
//                    printWriter.flush();
//                }
//                else {
//                    clientMap.put(studentID, ipAddress);
//
//                    TransmissionHandler transmissionHandler = new TransmissionHandler(socket, studentID, clientMap);
//
//                    Thread thread = new Thread(transmissionHandler);
//                    thread.start();
//                }
//
//            }
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

        File file = new File(serverAddress + fileName);

        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        FileOutputStream fileOutputStream = new FileOutputStream(file);

        fileOutputStream.flush();

        byte[] fileBytes = new byte[4096];

        int read = 0;
        int totalRead = 0;
        int remaining = (int) fileSize;

        System.out.println(remaining);

        while(remaining > 4096) {
            read = dataInputStream.read(fileBytes);
            totalRead += read;
            remaining -= read;
            System.out.println("read " + totalRead + " bytes.");

            fileOutputStream.write(fileBytes);
        }

        dataInputStream.read(fileBytes, 0, remaining);
        fileOutputStream.write(fileBytes, 0, remaining);

        System.out.println("read " + (totalRead + remaining) + " bytes.");

        fileOutputStream.close();
        dataInputStream.close();

        System.out.println("File received successfully.");
        System.out.println("File stored at location: " + serverAddress + fileName);

    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.connect();
        server.receiveFile();
    }
}
