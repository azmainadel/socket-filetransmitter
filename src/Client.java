import java.io.IOException;
import java.net.Socket;
import java.io.*;
import java.util.Scanner;

/**
 * Created by xyntherys on 9/29/17.
 */

public class Client {
    private Socket socket = null;

    private int senderStudentID = 0;
    private int receiverStudentID = 0;

    private PrintWriter printWriter = null;
    private BufferedReader bufferedReader = null;


    public Client() {

    }

    public void connect() {
        try {
            socket = new Socket("localHost", 4445);
            System.out.println("--------FILE TRANSMISSION SYSTEM RUNNING--------");

        } catch (IOException e) {
            System.err.println("> Server: \nCan not connect to the server. Please try again.");
            e.printStackTrace();
        }
    }

    public int checkActivity(int senderStudentID) throws IOException {
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        printWriter = new PrintWriter(socket.getOutputStream());

        printWriter.println(senderStudentID);
        printWriter.flush();

        String feedback = bufferedReader.readLine();

        if(feedback.equals("LOGGED_IN")) {
            System.out.println("> Server: \nYou are already logged in from another IP Address.");
            return 1;
        }
        else if(feedback.equals("NOT_LOGGED_IN")){
            return 2;
        }
        return 0;
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.connect();

        Scanner scanner = new Scanner(System.in);

        System.out.println("> Server: \nEnter your StudentID to log in: ");
        int studentID = scanner.nextInt();

        client.senderStudentID = studentID;

        if(client.checkActivity(studentID) == 2) {
            System.out.println("Press [s] to send a file or [r] to receive.");
            char choice = (char) System.in.read();

            if (choice == 's') {

//                System.out.println("Enter receiver StudentID: ");
//                client.receiverStudentID = scanner.nextInt();

                System.out.println("Enter the address of the file to be sent: ");
                FileHandler fileHandler = new FileHandler();
                fileHandler.setFileLocation(scanner.next());

//                FileSender fileSender = new FileSender(client.socket, client.receiverStudentID, fileHandler);

                FileSender fileSender = new FileSender(client.socket, fileHandler);
                fileSender.sendFile();

            }
            else if (choice == 'r'){

            }
        }

    }

}
