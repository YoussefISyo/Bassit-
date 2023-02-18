package com.optim.bassit.data;

import com.optim.bassit.models.Categorie;
import com.optim.bassit.models.SousCategorie;
import com.optim.bassit.models.Word;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AppData {
    private static AppData INSTANCE;


    int month;
    int year;

    public int getStatsyear() {
        return statsyear;
    }

    public void setStatsyear(int statsyear) {
        this.statsyear = statsyear;
    }

    int statsyear;

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month + 1;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    private AppData() {
    }

    public synchronized static AppData getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AppData();
            final Calendar cldr = Calendar.getInstance();
            INSTANCE.setMonth(cldr.get(Calendar.MONTH));
            INSTANCE.setYear(cldr.get(Calendar.YEAR));
            INSTANCE.setStatsyear(cldr.get(Calendar.YEAR));
        }

        return INSTANCE;
    }

    private List<SousCategorie> sousCategories;
    private List<Categorie> categories;

    public List<Categorie> getCategories() {
        return categories;
    }

    public void setCategories(List<Categorie> categories) {
        this.categories = categories;
    }

    public List<SousCategorie> getSousCategories() {
        return sousCategories;
    }

    public void setSousCategories(List<SousCategorie> sousCategories) {
        this.sousCategories = sousCategories;
    }

    public String fullMonthYear() {
        return month + "/" + year;
    }


    static Map<String, String> dict = new HashMap<>();
    static Map<String, String> dicten = new HashMap<>();

   public void setDict(List<Word> body, String lang) {
        dict = new HashMap<>();
        dicten = new HashMap<>();
        for (Word w : body) {
            try {
                if (lang.equals("ar"))
                    dict.put(w.getDesignation(), w.getD_ar());
                else if (lang.equals("en"))
                    dict.put(w.getDesignation(), w.getD_en());
                else
                    dict.put(w.getDesignation(), w.getDesignation());

                dicten.put(w.getDesignation(), w.getD_en());
            } catch (Exception ex) {

            }
        }
    }

    public static String TR(String s) {
        try {
            if (dict == null)
                return s;
            String a = dict.get(s);
            if (a == null || a.matches(""))
                return s;

            return a;

        } catch (Exception ex) {
            return s;
        }
    }
    public static String en(String s) {
        try {
            if (dicten == null)
                return s;
            String a = dicten.get(s);
            if (a == null || a.matches(""))
                return s;

            return a;

        } catch (Exception ex) {
            return s;
        }
    }
    public static String TR(String s, String separator) {

        try {

            String ss = "";


            for (String sx : s.split(separator)) {
                ss += TR(sx);
            }

            return ss;

        } catch (Exception ex) {
            return s;
        }
    }
}
