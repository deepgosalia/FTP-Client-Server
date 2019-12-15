import java.io.*;
public class CopyHandler {
    public static void main(String[] args) throws IOException{
       FileInputStream fileInputStream = null;
       FileOutputStream fileOutputStream = null;

       try{
           fileInputStream = new FileInputStream("src/input.txt");
           fileOutputStream = new FileOutputStream("src/output.txt");

           int c;

           while ((c=fileInputStream.read())!=-1){
               System.out.println(c);
               fileOutputStream.write(c);
           }
       }
       finally {
            if (fileInputStream!=null){
                fileInputStream.close();
            }
            if (fileOutputStream!=null){
                fileOutputStream.close();
            }
       }
    }
}
