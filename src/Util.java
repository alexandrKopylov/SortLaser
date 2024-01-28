import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
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
            String pozALL_4 = "";    //  variant 4
            String pozALL_5 = "";    //  variant 5
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
                inv = mas[0].replace(".", "");                 //удаляем точку в инвентарном
                poz = mas[1].split("_")[0];
                zakaz = mas[1].split("_")[1];
                count = mas[2];

                StringBuilder sb = new StringBuilder();
                StringBuilder sb2 = new StringBuilder();
                StringBuilder sb3 = new StringBuilder();
                StringBuilder sb4 = new StringBuilder();
                StringBuilder sb5 = new StringBuilder();

                sb.append(inv);
                sb.append("p");
                sb.append(poz);
                sb.append("L_");       // добавляется для лазера
                sb.append(zakaz);
                pozALL = sb.toString();


                sb4.append(inv);
                sb4.append(".p");
                sb4.append(poz);
                sb4.append("L_");       // добавляется для лазера
                sb4.append(zakaz);
                pozALL_4 = sb4.toString();


                sb2.append(inv);
                sb2.append("m");
                sb2.append(poz);
                sb2.append("L_");       // добавляется для лазера
                sb2.append(zakaz);
                pozALL_2 = sb2.toString();


                sb5.append(inv);
                sb5.append(".m");
                sb5.append(poz);
                sb5.append("L_");       // добавляется для лазера
                sb5.append(zakaz);
                pozALL_5 = sb5.toString();


                sb3.append(inv);
                posWithoutFirstLetter = poz.substring(1);
                sb3.append("m");
                sb3.append(posWithoutFirstLetter);
                sb3.append("L_");       // добавляется для лазера
                sb3.append(zakaz);
                pozALL_3 = sb3.toString();

                int dlinnaStr;

                for (String str : list) {
                    if (str.equals(pozALL)) {
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

                    } else if (str.equals(pozALL_4)) {
                        dlinnaStr = str.length();
                        pozAllWithCount = sb4.toString();
                        pozAllWithCount = pozAllWithCount + addSpaces(count, samayaDlinnayaStroka, dlinnaStr);
                        res.add(pozAllWithCount);
                        break;


                    } else if (str.equals(pozALL_5)) {
                        dlinnaStr = str.length();
                        pozAllWithCount = sb5.toString();
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
        String pozAllWithCount = "";
            pozAllWithCount = pozAllWithCount + " ".repeat(samayaDlinnayaStroka - dlinnaStr + 10) + count;
        return pozAllWithCount;
    }


    public void printInFile(File file, List<String> list) {

        try {
            FileWriter writer = new FileWriter(file);
            for (String str : list) {
                writer.write(str + System.lineSeparator());
            }

            writer.write("-".repeat(20));
            writer.write(System.lineSeparator());
            writer.write("kolvo = " + (int) list.stream().count());
            writer.write(System.lineSeparator());
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void readOrd(File file, List<String> listWhithCount) {
        Iterator<String> iter = listWhithCount.iterator();
        int k = 0;
        List<String> changeList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-16LE"));) {
            String line;
            String probel = "       ";
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                if (k == 0) {
                    changeList.add(line + System.lineSeparator());
                }
                if (k != 0) {
                    System.out.println(line);
                    String str = iter.next();
                    System.out.println(str);
                    String [] masStrok = str.split("          ");   // 10 пробелов
                    String []  masLineOrd = line.split("       ");    // 7 пробелов
                    System.out.println ();
                    sb.setLength(0);
                   changeList.add(sb.append(masLineOrd[0]).append(probel).append(masLineOrd[1]).append(probel).append(masStrok[1]).append(probel).append(masStrok[1])
                           .append(probel).append(masLineOrd[4]).append(probel).append(masLineOrd[5]).append(probel).append(masLineOrd[6]).append(probel)
                           .append(masLineOrd[7]).append(probel).append(System.lineSeparator()).toString());
                }

                System.out.println("---------------------------------");
                k++;
            }
            file.delete();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        try {
            PrintWriter writer = new PrintWriter (file , "UTF-16LE" );
           // writer.write("\uFEFF");
            for (String str : changeList) {
                writer.write(str );
            }
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}


