package utils;

import java.sql.Date;
import java.sql.Time;

public class Utilidade {
    public static Integer converterInteger(String dado) {
        try {
            return Integer.parseInt(dado);
        } catch (Exception e) {
            return null;
        }
    }

    public static Double converterDouble(String dado) {
        try {
            return Double.parseDouble(dado.replace(",", "."));
        } catch (Exception e) {
            return null;
        }
    }

    public static Date converterDate(String dado) {
        try {
            return Date.valueOf(dado);
        } catch (Exception e) {
            return null;
        }
    }

    public static Time converterTime(String dado) {
        try {
            return Time.valueOf(dado.length() == 5 ? dado + ":00" : dado);
        } catch (Exception e) {
            return null;
        }
    }
}
