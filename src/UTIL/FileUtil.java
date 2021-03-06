package UTIL;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static MAIN.main.PCNN_PATH;

public class FileUtil {
    public static void resetUnitario(String PCNN_PATH, File fo) {
        File F = new File(PCNN_PATH);
        delFile(F);
        makeFile(F, fo);
    }

    private static void delFile(File f) {
        File[] contents = f.listFiles();
        if (contents != null) {
            for (File file : contents) {
                delFile(file);
            }
        }
        f.delete();
    }

    private static void makeFile(File f, File fo) {
        f.mkdirs();
        if (fo.isFile()) {
            new File(f, fo.getName()).mkdirs();
        } else {
            for (File file : fo.listFiles()) {
                new File(f, file.getName()).mkdirs();
            }
        }
    }

    public static Map<String, Map<String, Map<String, Double>>> loadMetricas(File F) {
        Type mapType = new TypeToken<Map<String, Map<String, Map<String, Double>>>>() {
        }.getType();
        final String[] ln = {""};
        try {
            BufferedReader BR = new BufferedReader(new FileReader(F));
            BR.lines().forEach((l) -> {
                ln[0] += l;
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Map<String, Map<String, Map<String, Double>>> mp = new Gson().fromJson(ln[0], mapType);
        return mp;
    }

    public static Map<String, Map<String, Map<String, double[]>>> loadHistogramas(File F) {
        Type mapType = new TypeToken<Map<String, Map<String, Map<String, double[]>>>>() {
        }.getType();
        final String[] ln = {""};
        try {
            BufferedReader BR = new BufferedReader(new FileReader(F));
            BR.lines().forEach((l) -> {
                ln[0] += l;
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Map<String, Map<String, Map<String, double[]>>> mp = new Gson().fromJson(ln[0], mapType);
        return mp;
    }

    public static boolean evalFiles(File f) {
        File Fs[] = f.getParentFile().listFiles((dir, name) -> name.equals("Histogramas.json"));
        return Fs.length == 1 && f.exists() && f.isDirectory();
    }
    public static JFreeChart getChart(String Title, String ValX, String ValY, DefaultCategoryDataset Dataset, String Type) {
        return switch (Type) {
            case "BAR" -> ChartFactory.createBarChart(Title, ValX, ValY, Dataset,
                    PlotOrientation.VERTICAL, true, true, false);
            case "AREA" -> ChartFactory.createAreaChart(Title, ValX, ValY, Dataset);
            case "LINE" -> ChartFactory.createLineChart(Title, ValX, ValY, Dataset,
                    PlotOrientation.VERTICAL, true, true, false);
            default -> null;
        };
    }
}
