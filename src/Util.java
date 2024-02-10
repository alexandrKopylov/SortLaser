import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
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

    public List<String> readCSV(File file, List<String> list) {

        List<String> res = new ArrayList<>();
        String[] mas = null;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            String inv = "";
            String poz = "";
            String zakaz = "";
            String count = "";

            String posWithoutFirstLetter;

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
                String poz_L_zakaz = poz + "L_" + zakaz;

                String finalInv = inv;
                Predicate<String> invPredicate = x -> x.startsWith(finalInv);

                String search1 = "p" + poz_L_zakaz;
                String search2 = "m" + poz_L_zakaz;
                posWithoutFirstLetter = poz.substring(1);
                String search3 = "m" + posWithoutFirstLetter + "L_" + zakaz;
                Predicate<String> pozPredicate = x -> x.contains(search1) || x.contains(search2) || x.contains(search3) || x.contains(poz_L_zakaz);


                Optional<String> optString =  list.stream()
                        .filter(invPredicate)
                        .filter(pozPredicate)
                      .findFirst();

                if(optString.isPresent()){
                   res.add ( optString.get() + "          " + count);
                }

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return res;
    }




    public void printInFile(File file, List<String> list) {

        try {
            FileWriter writer = new FileWriter(file);
            for (String str : list) {
                writer.write(str + System.lineSeparator());
            }

            writer.write("-".repeat(20));
            writer.write(System.lineSeparator());
            writer.write("kolvo = " + list.size());
            writer.write(System.lineSeparator());
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void changeFileOrd(File file, List<String> listWhithCount) {
        int k = 0;
        List<String> changeList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_16LE));) {
            String line;
            String probel = "       ";
            StringBuilder sb = new StringBuilder();
            String[] masLineOrd;
            String[] masStrok;

            while ((line = reader.readLine()) != null) {
                if (k == 0) {
                    changeList.add(line + System.lineSeparator());
                }
                if (k != 0) {
                    masLineOrd = line.split(" {7}");    // 7 пробелов
                    for (String str : listWhithCount) {
                        masStrok = str.split(" {10}");   // 10 пробелов

                        String s1 = masLineOrd[1];
                        String  s2 = masStrok[0];
                        boolean bool = s1.contains(s2);

                        if (masLineOrd[1].contains(masStrok[0])) {
                            sb.setLength(0);
                            String stroka = sb.append(masLineOrd[0]).append(probel).append(masLineOrd[1]).append(probel).append(masStrok[1]).append(probel).append(masStrok[1])
                                    .append(probel).append(masLineOrd[4]).append(probel).append(masLineOrd[5]).append(probel).append(masLineOrd[6]).append(probel)
                                    .append(masLineOrd[7]).append(probel).append(System.lineSeparator()).toString();
                            changeList.add(stroka);
                            listWhithCount.remove(str);
                            break;
                        }
                    }
                }
                k=1;
            }
            file.delete();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            PrintWriter writer = new PrintWriter(file, StandardCharsets.UTF_16LE);
            // writer.write("\uFEFF");
            for (String str : changeList) {
                writer.write(str);
            }
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}


