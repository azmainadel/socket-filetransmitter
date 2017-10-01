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

    private int senderStudentID;
    private int receiverStudentID;
    private String ipAddress;
    private int port;

    public BufferedReader getBufferedReader() {
        return bufferedReader;
    }

    private Map<Integer, NetworkAddress> clientMap = new HashMap<Integer, NetworkAddress>();

    public Server() {

    }

    public void connect() {

        try {
            serverSocket = new ServerSocket(4445);
            System.out.println("---SERVER STARTED----");

//            socket = serverSocket.accept();

            while(true) {
                socket = serverSocket.accept();

                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                printWriter = new PrintWriter(socket.getOutputStream());

                senderStudentID = Integer.parseInt(bufferedReader.readLine());

                ipAddress = socket.getInetAddress().toString();
                port = socket.getPort();

                NetworkAddress networkAddress = new NetworkAddress(ipAddress, port);

                System.out.println("----NEW CONNECTION----");
                System.out.println("> Student ID: " + senderStudentID + " | Address: " + ipAddress + "-" + port);

                if(clientMap.containsKey(senderStudentID)){
                    System.out.println("Student already logged in.");

                    printWriter.println("LOGGED_IN");
                    printWriter.flush();
                }
                else {
                    printWriter.println("NOT_LOGGED_IN");
                    printWriter.flush();

                    clientMap.put(senderStudentID, networkAddress);

                    FileReceiver fileReceiver = new FileReceiver(socket, senderStudentID, clientMap, bufferedReader);

                    Thread thread = new Thread(fileReceiver);
                    thread.start();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//    public void receiveFile() throws IOException {
//
//        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//        printWriter = new PrintWriter(socket.getOutputStream());
//
//        String fileName = bufferedReader.readLine();
//        long fileSize = Long.parseLong(bufferedReader.readLine());
//
//        printWriter.flush();
//
//        File file = new File(serverAddress + fileName);
//
//        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
//        FileOutputStream fileOutputStream = new FileOutputStream(file);
//
//        fileOutputStream.flush();
//
//        byte[] fileBytes = new byte[4096];
//
//        int read = 0;
//        int totalRead = 0;
//        int remaining = (int) fileSize;
//
//        System.out.println(remaining);
//
//        while(remaining > 4096) {
//            read = dataInputStream.read(fileBytes);
//            totalRead += read;
//            remaining -= read;
//            System.out.println("read " + totalRead + " bytes.");
//
//            fileOutputStream.write(fileBytes);
//        }
//
//        dataInputStream.read(fileBytes, 0, remaining);
//        fileOutputStream.write(fileBytes, 0, remaining);
//
//        System.out.println("read " + (totalRead + remaining) + " bytes.");
//
//        fileOutputStream.close();
//        dataInputStream.close();
//
//        System.out.println("File received successfully.");
//        System.out.println("File stored at location: " + serverAddress + fileName);
//
//    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.connect();
//        server.receiveFile();
    }
}
