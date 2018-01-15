/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Mach3 Comment: This code only works with given URL as it capture data
 * according to website structure. If website structure changes code need to
 * updates as per new structure.
 */
public class HTMLCapturer extends HttpServlet {

    private final String BASE_DATA_URL = "https://www.metoffice.gov.uk/climate/uk/summaries/datasets#yearOrdered";
    List<String> cityList = new ArrayList<String>();
    List<String> UKList = new ArrayList<>();
    List<String> EnglandList = new ArrayList<>();
    List<String> WalesList = new ArrayList<>();
    List<String> ScotlandList = new ArrayList<>();
    List<String> tempType = new ArrayList<>();

    String csvFile = "weather.csv";
    FileWriter writer;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        initializeData();
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */

            getURLs(out);
            startTXTProcessing(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }

    //Defined Methods
    private void initializeData() {
        cityList.add("UK".toLowerCase());
        cityList.add("England".toLowerCase());
        cityList.add("Wales".toLowerCase());
        cityList.add("Scotland".toLowerCase());
        tempType.add("Max temp");
        tempType.add("Min temp");
        tempType.add("Mean temp");
        tempType.add("Sunshine");
        tempType.add("Rainfall");
    }

    private void getURLs(PrintWriter out) throws IOException {
        //out.println("Reading Webpage" + "<br>");
        //reading HTML document for getting Download URLs
        Document doc = Jsoup.connect(BASE_DATA_URL).get();
        //getting all Table Tags and their contents
        Elements table = doc.getElementsByClass("table");
        //getting required table
        Element requiredTable = table.last();
        //reading all <tr> for Download URLs
        Elements rows = requiredTable.getElementsByTag("tr");

        //out.println("Getting URL from Webpage" + "<br>");
        //fething Download URLs
        for (int i = 1; i < rows.size(); i++) {
            //if(tr.getAllElements().first())
            if (checkCityMatch(rows.get(i).child(0).text())) {
                System.out.println(rows.get(i).child(1).getAllElements().get(1).attr("href"));
            }
            switch (rows.get(i).child(0).text().toLowerCase().trim()) {
                case "uk":
                    UKList.add(rows.get(i).child(1).getAllElements().get(1).attr("href"));
                    UKList.add(rows.get(i).child(2).getAllElements().get(1).attr("href"));
                    UKList.add(rows.get(i).child(3).getAllElements().get(1).attr("href"));
                    UKList.add(rows.get(i).child(4).getAllElements().get(1).attr("href"));
                    UKList.add(rows.get(i).child(5).getAllElements().get(1).attr("href"));
                    break;
                case "england":
                    EnglandList.add(rows.get(i).child(1).getAllElements().get(1).attr("href"));
                    EnglandList.add(rows.get(i).child(2).getAllElements().get(1).attr("href"));
                    EnglandList.add(rows.get(i).child(3).getAllElements().get(1).attr("href"));
                    EnglandList.add(rows.get(i).child(4).getAllElements().get(1).attr("href"));
                    EnglandList.add(rows.get(i).child(5).getAllElements().get(1).attr("href"));
                    break;
                case "wales":
                    WalesList.add(rows.get(i).child(1).getAllElements().get(1).attr("href"));
                    WalesList.add(rows.get(i).child(2).getAllElements().get(1).attr("href"));
                    WalesList.add(rows.get(i).child(3).getAllElements().get(1).attr("href"));
                    WalesList.add(rows.get(i).child(4).getAllElements().get(1).attr("href"));
                    WalesList.add(rows.get(i).child(5).getAllElements().get(1).attr("href"));
                    break;
                case "scotland":
                    ScotlandList.add(rows.get(i).child(1).getAllElements().get(1).attr("href"));
                    ScotlandList.add(rows.get(i).child(2).getAllElements().get(1).attr("href"));
                    ScotlandList.add(rows.get(i).child(3).getAllElements().get(1).attr("href"));
                    ScotlandList.add(rows.get(i).child(4).getAllElements().get(1).attr("href"));
                    ScotlandList.add(rows.get(i).child(5).getAllElements().get(1).attr("href"));
                    break;
            }
        }
        //out.println("Download URLs captured" + "<br>");
    }

    private boolean checkCityMatch(String city) {
        if (cityList.contains(city.toLowerCase().trim())) {
            return true;
        } else {
            return false;
        }
    }

    private void startTXTProcessing(PrintWriter out) throws Exception {
        //out.println("Creating/opening file for writting CSV data" + "<br>");
        //Creating/opening file for writting CSV data
        File file = new File(csvFile);
        writer = new FileWriter(file.getAbsolutePath());

        //out.println("Downloading and parsing data" + "<br>");
        for (int i = 0; i < UKList.size() ; i++) {
            //out.println("UK List " + i + "<br><hr>");
            renderTXT(out, UKList.get(i), "UK", tempType.get(i));
        }
        for (int i = 0; i < EnglandList.size(); i++) {
            //out.println("England List " + i + "<br><hr>");
            renderTXT(out, EnglandList.get(i), "England", tempType.get(i));
        }
        for (int i = 0; i < WalesList.size(); i++) {
            //out.println("Wales List " + i + "<br><hr>");
            renderTXT(out, WalesList.get(i), "Wales", tempType.get(i));
        }
        for (int i = 0; i < ScotlandList.size(); i++) {
            //out.println("Scotland List " + i + "<br><hr>");
            renderTXT(out, ScotlandList.get(i), "Scotland", tempType.get(i));
        }
        //out.println("Downloading and parsing data Completed" + "<br>");
        out.println("<center><h3>Downloading and Parsing Completed<br>");
        out.println("<a href='DownloadFile'>Download File</a></h3></center>" + "<br>");
    }

    private void renderTXT(PrintWriter out, String dataURL, String city, String tempType) throws IOException {
        //getting Data from Download URL
        Document doc = Jsoup.connect(dataURL).get();

        //replacing all unnecessary spaces for proper parsing
        String data = doc.wholeText().replaceAll(" +", ":");

        //removing all unnecessary data
        int index = data.indexOf("Year:JAN");
        data = data.substring(index);

        //separating each row
        Scanner scanner = new Scanner(data);
        scanner.useDelimiter("\n");
        scanner.next();

        while (scanner.hasNext()) {
            //parse line to get weather Object
            WeatherModel weather = parseCSVLine(scanner.next(), out);
            System.err.println(weather.toString());
            //writting object to file
            for (int i = 0; i < 12; i++) {
                writeData(weather, i, out, city, tempType);
            }
        }
        scanner.close();
    }

    private WeatherModel parseCSVLine(String line, PrintWriter out) {
        ArrayList<String> yearData = new ArrayList<>();
        String tempData = "";
        //System.out.println(line);
        //getting each row and separating in required formate
        Scanner scanner = new Scanner(line);
        scanner.useDelimiter(":");
        String year = scanner.next();
        //System.out.println(year);
        while (scanner.hasNext()) {
            tempData = scanner.next();
            System.out.println("Temp" + tempData);
            //handling missing data by putting N/A
            if (tempData.equals("---")) {
                System.out.println("Empty Data Found");
                yearData.add("N/A");
            } else {
                yearData.add(tempData);
            }
        }
        return new WeatherModel(year, yearData);
    }

    private void writeData(WeatherModel weather, int month, PrintWriter out, String city, String tempType) throws IOException {
        switch (month) {
            case 0:
                CSVWriter.writeLineInFile(writer, Arrays.asList(city, tempType, weather.getYear(), "JAN", weather.getDataMonthWise().get(month)));
                break;
            case 1:
                CSVWriter.writeLineInFile(writer, Arrays.asList(city, tempType, weather.getYear(), "FEB", weather.getDataMonthWise().get(month)));
                break;
            case 2:
                CSVWriter.writeLineInFile(writer, Arrays.asList(city, tempType, weather.getYear(), "MAR", weather.getDataMonthWise().get(month)));
                break;
            case 3:
                CSVWriter.writeLineInFile(writer, Arrays.asList(city, tempType, weather.getYear(), "APR", weather.getDataMonthWise().get(month)));
                break;
            case 4:
                CSVWriter.writeLineInFile(writer, Arrays.asList(city, tempType, weather.getYear(), "MAY", weather.getDataMonthWise().get(month)));
                break;
            case 5:
                CSVWriter.writeLineInFile(writer, Arrays.asList(city, tempType, weather.getYear(), "JUN", weather.getDataMonthWise().get(month)));
                break;
            case 6:
                CSVWriter.writeLineInFile(writer, Arrays.asList(city, tempType, weather.getYear(), "JUL", weather.getDataMonthWise().get(month)));
                break;
            case 7:
                CSVWriter.writeLineInFile(writer, Arrays.asList(city, tempType, weather.getYear(), "AUG", weather.getDataMonthWise().get(month)));
                break;
            case 8:
                CSVWriter.writeLineInFile(writer, Arrays.asList(city, tempType, weather.getYear(), "SEP", weather.getDataMonthWise().get(month)));
                break;
            case 9:
                CSVWriter.writeLineInFile(writer, Arrays.asList(city, tempType, weather.getYear(), "OCT", weather.getDataMonthWise().get(month)));
                break;
            case 10:
                CSVWriter.writeLineInFile(writer, Arrays.asList(city, tempType, weather.getYear(), "NOV", weather.getDataMonthWise().get(month)));
                break;
            case 11:
                CSVWriter.writeLineInFile(writer, Arrays.asList(city, tempType, weather.getYear(), "DEC", weather.getDataMonthWise().get(month)));
                break;

        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void init()
            throws ServletException {
        super.init(); //To change body of generated methods, choose Tools | Templates.

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
