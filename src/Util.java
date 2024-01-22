import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Util {


    public List<String> searchDXF(Path path) {
        List<String> listPathDXF;
        try {
            listPathDXF = Files.walk(path)
                    .filter(Files::isRegularFile)
                    .filter(x -> x.toFile().getName().endsWith(".dxf"))
                    .map(x -> x.getFileName())
                    .map(x -> x.toString())
                    .map(x -> x.replace(".dxf", ""))
                    .collect(Collectors.toList());
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
        return listPathDXF;
    }

    public List<String> readCSV(File file, List<String> list, int samayaDlinnayaStroka) {

        List<String> res = new ArrayList<>();
        String[] mas = null;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;


            String inv = "";
            String poz = "";
            String zakaz = "";
            String count = "";
            String pozALL = "";
            String pozALL_2 = "";    //  variant 2
            String pozALL_3 = "";    //  variant 3
            String posWithoutFirstLetter;

            String pozAllWithCount = "";
            while ((line = br.readLine()) != null) {
                if (line.equals("\t\t\t\t\t")
                        || line.equals("\t\t\t\t")
                        || line.equals("Инв.\tОбозначение\tКол.Т\tКол.Н\tГабариты\t")
                        || line.equals("Инв.\tОбозначение\tКол.Т\tКол.Н\tГабариты")) {
                    continue;
                }
                mas = line.split("\t");
                inv = mas[0];
                poz = mas[1].split("_")[0];
                zakaz = mas[1].split("_")[1];
                count = mas[2];

                StringBuilder sb = new StringBuilder();
                StringBuilder sb2 = new StringBuilder();
                StringBuilder sb3 = new StringBuilder();

                sb.append(inv);
                sb.append("p");
                sb.append(poz);
                sb.append("L_");       // добавляется для лазера
                sb.append(zakaz);
                pozALL = sb.toString();

                sb2 = new StringBuilder();
                sb2.append(inv);
                sb2.append("m");
                sb2.append(poz);
                sb2.append("L_");       // добавляется для лазера
                sb2.append(zakaz);
                pozALL_2 = sb2.toString();

                sb3 = new StringBuilder();
                sb3.append(inv);
               // sb3.append("m");


               // char firstLetter = poz.charAt(0);
               // const str = "Jamaica".substring(1)
                posWithoutFirstLetter = poz.substring(1);
                sb3.append("m");
                sb3.append(posWithoutFirstLetter);
                sb3.append("L_");       // добавляется для лазера
                sb3.append(zakaz);
                pozALL_3 = sb3.toString();

                int dlinnaStr;
                for (String str : list) {

                    if (str.equals(pozALL)){
                        dlinnaStr = str.length();
                        pozAllWithCount = sb.toString();
                        pozAllWithCount = pozAllWithCount + addSpaces(count, samayaDlinnayaStroka, dlinnaStr);
                        res.add(pozAllWithCount);
                        break;
                    } else if (str.equals(pozALL_2)) {
                        dlinnaStr = str.length();
                        pozAllWithCount = sb2.toString();
                        pozAllWithCount = pozAllWithCount + addSpaces(count, samayaDlinnayaStroka, dlinnaStr);
                        res.add(pozAllWithCount);
                        break;

                    } else if (str.equals(pozALL_3)) {
                        dlinnaStr = str.length();
                        pozAllWithCount = sb3.toString();
                        pozAllWithCount = pozAllWithCount + addSpaces(count, samayaDlinnayaStroka, dlinnaStr);
                        res.add(pozAllWithCount);
                        break;
                    } else {
                        continue;
                    }
                }

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return res;
    }

    private String addSpaces(String count, int samayaDlinnayaStroka, int dlinnaStr) {
String  pozAllWithCount ="";
        if (count.equals("1")) {
            pozAllWithCount = pozAllWithCount + " ".repeat(samayaDlinnayaStroka - dlinnaStr + 10) + count;
        } else {
            pozAllWithCount = pozAllWithCount + " ".repeat(samayaDlinnayaStroka - dlinnaStr + 12) + count;
        }

        return  pozAllWithCount;
    }


    public void printInFile (File file, List<String> list){

        try {
            FileWriter writer = new FileWriter(file);
        for(String str: list) {
            writer.write(str + System.lineSeparator());
        }

        writer.write("-".repeat(20));
        writer.write (System.lineSeparator());
        writer.write("kolvo = "+(int) list.stream().count());
        writer.write (System.lineSeparator());
        writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
