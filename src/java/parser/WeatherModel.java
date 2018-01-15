/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.util.ArrayList;

/**
 *
 * @author Mach3
 */
public class WeatherModel {

    private String year;
    private ArrayList<String> dataMonthWise = new ArrayList<>();

    public WeatherModel(String year, ArrayList<String> dataMonthWise) {
        this.year = year;
        this.dataMonthWise = dataMonthWise;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public ArrayList<String> getDataMonthWise() {
        return dataMonthWise;
    }

    public void setDataMonthWise(ArrayList<String> dataMonthWise) {
        this.dataMonthWise = dataMonthWise;
    }
    
    

    @Override
    public String toString() {
        return "WeatherModel{" + "year=" + year + ", dataMonthWise=" + dataMonthWise + '}';
    }

}
