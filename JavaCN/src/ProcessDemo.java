
import java.io.InputStream;

public class ProcessDemo {

    public static void main(String[] args) {
        try {
            // create a new process
            System.out.println("Creating Process...");
            Process p = Runtime.getRuntime().exec("notepad.exe");

            // get the input stream of the process and print it
            InputStream in = p.getInputStream();
            for (int i = 0; i < in.available(); i++) {
                System.out.println("" + in.read());
            }

            // wait for 10 seconds and then destroy the process
            Thread.sleep(10000);
            p.destroy();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}