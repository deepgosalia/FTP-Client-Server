import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class Sample {
    public static void main(String[] args) throws IOException {
//        String dir = new java.io.File(".").getCanonicalPath();
//        Path path = FileSystems.getDefault().getPath(".").toAbsolutePath();
//        String cwd = System.getProperty("user.dir");
//        System.out.println(cwd);

        String temp = "                get file.txt";
        String newString = temp.replaceAll("^\\s+","");
        System.out.println(newString);
    }
}
