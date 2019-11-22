import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class FTPServer implements Runnable {
    public Socket socket;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;
    private static final int fileSize = 10240;
    private static FileInputStream fileInputStream;
    private static ArrayList<FTPServer> clients = new ArrayList<>();
    private static ExecutorService pool = Executors.newFixedThreadPool(10);
    private static Hashtable<String, String> credentials = new Hashtable<>();

    private FTPServer(Socket clientSocket) throws IOException {
        this.socket = clientSocket;
        bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        printWriter = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    public static void main(String[] args) throws IOException {
        credentials.put("client1", "abc1");
        credentials.put("client2", "abc2");
        credentials.put("client3", "abc3");
        credentials.put("client4", "abc4");

        int portNumber = 8000;
        System.out.println("Waiting for connection...");
        ServerSocket serverSocket = new ServerSocket(portNumber);
        Socket temp;
        while (true) {
            temp = serverSocket.accept();
            FTPServer clientThread = new FTPServer(temp);
            Thread t = new Thread(clientThread);
            t.start();

        }

    }

    @Override
    public void run() {
        String final_username="server";
        try {
            while (true) {
                String user_name, password;
                printWriter.println("Enter Username");
                user_name = bufferedReader.readLine();
                printWriter.println("Enter Password");
                password = bufferedReader.readLine();
                if (credentials.containsKey(user_name)) {
                    String password_cred = credentials.get(user_name);
                    if (!password.equals(password_cred)) {
                        printWriter.println("Invalid");
                    } else {
                        System.out.println(user_name+" connected");
                        printWriter.println("User authorized");
                        final_username = user_name;
                        break;
                    }
                } else {
                    printWriter.println("Invalid");
                }
            }
            String input;
            while (true) {
                input = bufferedReader.readLine();
                String[] splitInput = input.split("\\s");
                switch (splitInput[0]) {
                    case "upload":
                        this.writeFile(splitInput[1]);
                        break;
                    case "dir":
                        this.getFileList();
                        break;
                    case "get":
                        this.sendFile(splitInput[1]);
                        break;
                    case "add":
                        if(splitInput.length==3){
                            this.addUser(splitInput[1],splitInput[2]);
                        }else{
                            printWriter.println("Invalid input");
                            printWriter.flush();
                        }
                        break;
                    default:
                        printWriter.println("Invalid Command");
                        break;
                }

            }
        } catch (Exception e) {
            System.out.println(final_username+" Disconnected");
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            printWriter.close();
        }
    }

    private void addUser(String u, String p) {
        //this is function is just an optional part of CN project. It will only add user
        if(credentials.containsKey(u)){
            printWriter.println("User already exists");
            printWriter.flush();
        }else{
            credentials.put(u,p);
            printWriter.println("User added");
            printWriter.flush();
        }
    }

    private void getFileList() throws IOException {
        try {
            String dir = new java.io.File(".").getCanonicalPath();
            File file = new File(dir + "/");
            List<String> filesList = Arrays.asList(Objects.requireNonNull(file.list()));
            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(filesList);
            objectOutputStream.flush();

        } catch (NullPointerException | FileNotFoundException exception) {
            System.out.println(exception);
        }
    }

    private void sendFile(String fileName) throws IOException {

        try {
            String dir = new java.io.File(".").getCanonicalPath();
            File fileUpload = new File(dir + "/" + fileName);
            if (fileUpload.exists()) {
                printWriter.println("Found");
                printWriter.flush();
                byte[] byteData = new byte[(int) fileUpload.length()];
                fileInputStream = new FileInputStream(fileUpload);
                fileInputStream.read(byteData);
                OutputStream os = this.socket.getOutputStream();
                os.write(byteData);
                os.flush();
            } else {
                printWriter.flush();
                printWriter.println("NotFound");
            }

        } catch (FileNotFoundException fileNotFound) {
            printWriter.println("NotFound");
        }

    }

    private void writeFile(String fileName) throws IOException {
        String dir = new java.io.File(".").getCanonicalPath();
        File fileUpload = new File(dir + "/" + fileName);
        byte[] uploadData = new byte[fileSize];
        InputStream is = this.socket.getInputStream();
        is.read(uploadData);
        FileOutputStream fileOutputStream = new FileOutputStream(fileUpload);
        fileOutputStream.write(uploadData);
        fileOutputStream.flush();
        fileOutputStream.close();
    }
}
