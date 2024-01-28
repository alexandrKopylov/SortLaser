import java.io.File;
import java.nio.file.Path;
import java.time.Year;
import java.util.*;

public class Main {

   //  static Path pathDXF = Path.of("c:\\Program Files\\AutoCAD 2010\\_DXF\\");
   static Path pathDXF = Path.of("C:\\Users\\user\\Desktop\\dxf\\dxf\\dxf");

   //   static File fileCSV = new File("c:\\Users\\alexx\\Desktop\\ДеталиБК_все.csv");
   static File fileCSV = new File("C:\\Users\\user\\Desktop\\dxf\\dxf\\ДеталиБК_все.csv");

    static  String fileOrdString = "C:\\Users\\user\\Desktop\\dxf\\dxf\\" ;     //1506_24.Ord");

   // static File spisokGotogo = new File("c:\\Users\\alexx\\Desktop\\Gotogo.txt");
   static File spisokGotogo = new File("C:\\Users\\user\\Desktop\\dxf\\dxf\\Gotogo.txt");


    public static void main(String[] args) {

        Scanner scan  = new Scanner(System.in);
        System.out.print("Введите комплектовочную: ");
        String komplektovochnaya = scan.nextLine();

        int currentYear = Year.now().getValue();
        fileOrdString = fileOrdString + komplektovochnaya + "_" + currentYear + ".Ord";

        File fileOrd = new File(fileOrdString);
        if (!fileOrd.exists()){
            System.out.println("нет такой комплектовочной " + fileOrdString);
            return;
        }

        Util util = new Util();
        List<String> list = util.searchDXF(pathDXF);
        for (String s : list) {
            System.out.println(s);
        }
        System.out.println("kol =" + list.size());

        // находим самую длинную строку в списке
        OptionalInt oi = list.stream()
                .mapToInt(String::length)
                .max();
        int max = oi.getAsInt();
       // System.out.println(max);

        List<String> listWithCount =  util.readCSV(fileCSV,list, max);
        Collections.sort(listWithCount);

//        for (String s:listWithCount) {
//            System.out.println(s);
//        }

        util.printInFile(spisokGotogo, listWithCount );

        util.readOrd(fileOrd, listWithCount);

    }}