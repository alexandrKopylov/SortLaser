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
                sb.append(inv);
                if (Character.isDigit(poz.charAt(0))) {
                    sb.append("p");
                } else {
                    sb.append("m");
                }
                sb.append(poz);
                sb.append("L_");       // добавляется для лазера
                sb.append(zakaz);
                pozALL= sb.toString();


                int dlinnaStr;
                for (String str : list) {

                    if (str.equals(pozALL)){
                        dlinnaStr = str.length();
                        //sb.append(str).append(" ".repeat(samayaDlinnayaStroka - dlinnaStr + 10)).append(count);

                        pozAllWithCount = sb.toString();
                        pozAllWithCount = pozAllWithCount+" ".repeat(samayaDlinnayaStroka - dlinnaStr + 10) + count;

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


    public void printInFile (File file, List<String> list){
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for(String str: list) {
            try {
                writer.write(str + System.lineSeparator());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}