import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.OptionalInt;

public class Main {

    static Path pathDXF = Path.of("C:\\Users\\user\\Desktop\\ssss\\DXF");
    static File fileCSV = new File("C:\\Users\\user\\Desktop\\ssss\\ftftyfyfyy.csv");
    static File spisokGotogo = new File("C:\\Users\\user\\Desktop\\ssss\\Gotogo.txt");

    public static void main(String[] args) {
        Util util = new Util();
        List<String> list = util.searchDXF(pathDXF);
//        for (String s : list) {
//            System.out.println(s);
//        }

        // находим самую длинную строку в списке
        OptionalInt oi = list.stream()
                .mapToInt(String::length)
                .max();
        int max = oi.getAsInt();
       // System.out.println(max);

        List<String> listWithCol =  util.readCSV(fileCSV,list, max);
        Collections.sort(listWithCol);

//        for (String s:listWithCol) {
//            System.out.println(s);
//        }

        util.printInFile(spisokGotogo, listWithCol );





    }}