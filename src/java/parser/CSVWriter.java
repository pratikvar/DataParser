/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 *
 * @author PRATIK
 */
public class CSVWriter {


    private static String formatAsCSV(String value) {

        String result = value;
        if (result.contains("\"")) {
            result = result.replace("\"", "\"\"");
        }
        return result;

    }

    public static void writeLineInFile(Writer w, List<String> values) throws IOException {
        boolean first = true;

        StringBuilder sb = new StringBuilder();
        for (String value : values) {
            if (!first) {
                sb.append(",");
            }
            sb.append(formatAsCSV(value));

            first = false;
        }
        sb.append("\n");
        w.append(sb.toString());

    }
}
