import java.io.*;
import java.net.Socket;
import java.util.List;

public class FTPClient {
    private static Socket socket = null;
    private static FileInputStream fileInputStream;
    private static final int fileSize = 10240;

    public static void main(String[] args) throws IOException {
        PrintWriter printWriter = null;
        BufferedReader bufferedReader = null;
        BufferedReader clientInput = null;
        String hostName=" ";
        int portNumber=-1;
        System.out.println("Enter command");
        clientInput = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
                String data = clientInput.readLine();
                String[] networkData = data.split("\\s");
                if (networkData[0].equals("ftpclient") || networkData.length == 3) {
                    hostName = networkData[1];
                    portNumber = Integer.parseInt(networkData[2]);
                    try {
                        socket = new Socket(hostName, portNumber);
                        break;
                    }catch (Exception e){
                        System.out.println("Invalid hostname/port number");
                    }
                } else {
                    System.out.println("Invalid Input");
                }
        }

        try {
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            clientInput = new BufferedReader(new InputStreamReader(System.in));
            String data, ackMessage;
            boolean authorized = false;
            while (true) {
                if (!authorized) {
                    String message = bufferedReader.readLine();
                    System.out.println(message);
                    String user_name = clientInput.readLine();
                    printWriter.println(user_name);

                    message = bufferedReader.readLine();
                    System.out.println(message);
                    String password = clientInput.readLine();
                    printWriter.println(password);
                    message = bufferedReader.readLine();

                    if (message.equals("Invalid")) {
                        System.out.println("Invalid Username and Password");
                        continue;
                    } else {
                        System.out.println(message);
                        authorized = true;
                    }
                }

                data = clientInput.readLine();
                String[] splitData = data.split("\\s");
                switch (splitData[0]) {
                    case "upload":
                        if(splitData.length==2){
                            try {
                                String fileName;
                                fileName = splitData[1];
                                int ack = uploadFileToServer(fileName,printWriter);
                                if (ack == 1) {
                                    System.out.println("File Uploaded");
                                } else {
                                    System.out.println("File does not exist");
                                }


                            } catch (ArrayIndexOutOfBoundsException a) {
                                System.out.println("Invalid input");
                            } catch (Exception e) {
                                System.out.println(e);
                            }
                        }else{
                            System.out.println("Invalid input");
                        }

                        break;
                    case "dir":
                        if(splitData.length==1){
                            printWriter.println(data);
                            displayDir();
                        }else{
                            System.out.println("Invalid input");
                        }

                        break;
                    case "get":
                        if(splitData.length==2){
                            try {
                                String fileName;
                                fileName = splitData[1];
                                printWriter.println(data);
                                ackMessage = bufferedReader.readLine();

                                if (!ackMessage.equals("NotFound")) {
                                    System.out.println("File Downloaded");
                                    downloadFileFromServer(fileName);
                                } else {
                                    System.out.println("File does not exist");
                                }

                            } catch (ArrayIndexOutOfBoundsException a) {
                                System.out.println("Invalid input");
                            } catch (Exception e) {
                                System.out.println(e);
                            }
                        }else{
                            System.out.println("Invalid input");
                        }

                        break;
                    case "add":
                        if (splitData.length==3){
                            printWriter.println(data);
                            ackMessage = bufferedReader.readLine();
                            System.out.println(ackMessage);
                        }else{
                            System.out.println("Invalid input");
                        }

                        break;
                    default:
                        System.out.println("Invalid Command");
                        break;
                }

            }

        } catch (Exception e) {
            System.out.println("Unable to establish a connection");
        }
    }


    private static void displayDir() throws IOException, ClassNotFoundException {
        InputStream is = socket.getInputStream();
        ObjectInputStream objectInputStream = new ObjectInputStream(is);
        Object temp = objectInputStream.readObject();
        List<?> list = (List<?>) temp;
        for (Object o : list) {
            System.out.println(o);
        }
    }

    private static void downloadFileFromServer(String fileName) throws IOException {
        String dir = new java.io.File(".").getCanonicalPath();
        File fileDownload = new File(dir + "/" + fileName);
        byte[] uploadData = new byte[fileSize];
        InputStream is = socket.getInputStream();
        is.read(uploadData);
        FileOutputStream fileOutputStream = new FileOutputStream(fileDownload);
        fileOutputStream.write(uploadData);
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    private static int uploadFileToServer(String fileName,PrintWriter pw) throws IOException {
        try {
            String dir = new java.io.File(".").getCanonicalPath();
            File fileUpload = new File(dir + "/" + fileName);
            if (fileUpload.exists()) {
                pw.println("upload "+fileName);
                pw.flush();
                byte[] byteData = new byte[(int) fileUpload.length()];
                fileInputStream = new FileInputStream(fileUpload);
                fileInputStream.read(byteData);
                OutputStream os = socket.getOutputStream();
                os.write(byteData);
                os.flush();

                return 1;
            } else {
                return -1;
            }

        } catch (FileNotFoundException fileNotFound) {
            System.out.println("File does not exist");
            return -1;
        }

    }
}
