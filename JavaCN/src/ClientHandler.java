import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private Socket socket;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;

    public ClientHandler(Socket clientSocket) throws IOException {
        this.socket = clientSocket;
        bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        printWriter = new PrintWriter(clientSocket.getOutputStream(),true);

    }

    @Override
    public void run() {
        try {

            String input;
            while (true) {
                //while ((input = bufferedReader.readLine()) != null) {
                input = bufferedReader.readLine();
                String[] splitInput = input.split("\\s");
                System.out.println(splitInput[0]);
                switch (splitInput[0]) {
//                    case "upload":
//                        Server.writeFile(splitInput[1]);
//                        break;
                    case "dir":
                       // Server.getFileList();
                        break;
//                    case "get":
//                        sendFile(splitInput[1]);
//                        break;
                    default:
                        printWriter.println("Invalid Command");
                        break;
                }
                //bufferedReader.close();

            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            printWriter.close();
        }
    }
}
