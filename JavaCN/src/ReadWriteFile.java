import java.io.*;

public class ReadWriteFile {
    public static void main(String[] args) {
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        String directory = System.getProperty("user.home");
        String fileName = "sample.txt";
        String finalPath = directory + File.separator + fileName;
        System.out.println(finalPath);



        try{
            bufferedWriter = new BufferedWriter(new FileWriter(finalPath));
            String input = "This is the sample";
            bufferedWriter.write(input);
            bufferedWriter.close();
        }catch (Exception e){
            System.out.println("Error generated while writing");
        }


        try{
            bufferedReader = new BufferedReader(new FileReader(finalPath));
            String input;

            while ((input=bufferedReader.readLine())!=null){
                System.out.println(input);
            }
            bufferedReader.close();
        }catch (Exception e){

        }
    }
}
