import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Year;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Start {


     static Path pathDXF = Path.of("c:\\Program Files\\AutoCAD 2010\\_DXF\\");
  // static Path pathDXF = Path.of("C:\\Users\\user\\Desktop\\dxf\\dxf\\dxf");

      static File fileCSV = new File("c:\\Users\\alexx\\Desktop\\ДеталиБК_все.csv");
  // static File fileCSV = new File("C:\\Users\\user\\Desktop\\dxf\\dxf\\ДеталиБК_все.csv");

  //  static  String fileOrdString = "z:\\BAZA\\Autonest\\" ;     //1506_24.Ord");

    static File spisokGotogo = new File("c:\\Users\\alexx\\Desktop\\Gotogo.txt");
   //static File spisokGotogo = new File("C:\\Users\\user\\Desktop\\dxf\\dxf\\Gotogo.txt");


    public static void main(String[] args) {

        Scanner scan  = new Scanner(System.in);
        System.out.print("Введите комплектовочную: ");
        String komplektovochnaya = scan.nextLine();

        int currentYear = Year.now().getValue()%100;
      //  fileOrdString = fileOrdString + komplektovochnaya + "_" + currentYear + "\\"  +  komplektovochnaya + "_" + currentYear + ".Ord";
         StringBuilder sb = new StringBuilder("z:\\BAZA\\Autonest\\");
        String fileOrdString = sb.append(komplektovochnaya).append("_").append(currentYear).append("\\").append(komplektovochnaya).append("_")
                .append(currentYear).append(".Ord").toString();


        File fileOrd = new File(fileOrdString);
        if (!fileOrd.exists()){
            System.out.println("нет такой комплектовочной " + fileOrdString);
            return;
        }

        System.out.println("1-добавляем количество деталей в файл ord");
        System.out.println("2-удаляем дубликать");
        System.out.println("3-exit");
        int chose = scan.nextInt();

        if (chose==3){
            System.exit(1);
        }

        if (chose==1){
            Util util = new Util();
            List<String> list = util.searchDXF(pathDXF);
            for (String s : list) {
                System.out.println(s);
            }
            System.out.println("kol =" + list.size());


            List<String> listWithCount =  util.readCSV(fileCSV, list);
            Collections.sort(listWithCount);

            util.printInFile(spisokGotogo, listWithCount );

            util.changeFileOrd(fileOrd, listWithCount);
        }

        if (chose==2){

            Util util = new Util();
            List<Path> listPathDXF;
            try {
                listPathDXF = Files.walk(pathDXF)
                        .filter(Files::isRegularFile)
                        .filter(x -> x.toFile().getName().endsWith(".dxf"))
                        .collect(Collectors.toList());

            } catch (
                    IOException e) {
                throw new RuntimeException(e);
            }

            util.deleteDublicati(fileOrd,listPathDXF);


        }



    }}