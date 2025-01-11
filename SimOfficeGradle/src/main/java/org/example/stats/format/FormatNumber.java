package org.example.stats.format;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class FormatNumber {
    private static final DecimalFormat decimalFormat = new DecimalFormat("0.0");

    public static String formatNumber(double number) {
        BigDecimal num = BigDecimal.valueOf(number);

        if (num.compareTo(BigDecimal.valueOf(Constants.NOTGILLION)) >= 0) {
            return num.divide(BigDecimal.valueOf(Constants.NOTGILLION), 2, RoundingMode.HALF_UP).toString() + "Notg";
        } else if (num.compareTo(BigDecimal.valueOf(Constants.OCTGILLION)) >= 0) {
            return num.divide(BigDecimal.valueOf(Constants.OCTGILLION), 1, RoundingMode.HALF_UP).toString() + "Octg";
        } else if (num.compareTo(BigDecimal.valueOf(Constants.SPTGILLION)) >= 0) {
            return num.divide(BigDecimal.valueOf(Constants.SPTGILLION), 1, RoundingMode.HALF_UP).toString() + "Sptg";
        } else if (num.compareTo(BigDecimal.valueOf(Constants.SXTGILLION)) >= 0) {
            return num.divide(BigDecimal.valueOf(Constants.SXTGILLION), 1, RoundingMode.HALF_UP).toString() + "Sxtg";
        } else if (num.compareTo(BigDecimal.valueOf(Constants.QITGILLION)) >= 0) {
            return num.divide(BigDecimal.valueOf(Constants.QITGILLION), 1, RoundingMode.HALF_UP).toString() + "Qitg";
        } else if (num.compareTo(BigDecimal.valueOf(Constants.QATGILLION)) >= 0) {
            return num.divide(BigDecimal.valueOf(Constants.QATGILLION), 1, RoundingMode.HALF_UP).toString() + "Qatg";
        } else if (num.compareTo(BigDecimal.valueOf(Constants.TTGILLION)) >= 0) {
            return num.divide(BigDecimal.valueOf(Constants.TTGILLION), 1, RoundingMode.HALF_UP).toString() + "Ttg";
        } else if (num.compareTo(BigDecimal.valueOf(Constants.DTGILLION)) >= 0) {
            return num.divide(BigDecimal.valueOf(Constants.DTGILLION), 1, RoundingMode.HALF_UP).toString() + "Dtg";
        } else if (num.compareTo(BigDecimal.valueOf(Constants.UTGILLION)) >= 0) {
            return num.divide(BigDecimal.valueOf(Constants.UTGILLION), 1, RoundingMode.HALF_UP).toString() + "Utg";
        } else if (num.compareTo(BigDecimal.valueOf(Constants.TGILLION)) >= 0) {
            return num.divide(BigDecimal.valueOf(Constants.TGILLION), 1, RoundingMode.HALF_UP).toString() + "Tg";
        } else if (num.compareTo(BigDecimal.valueOf(Constants.NOVGILLION)) >= 0) {
            return num.divide(BigDecimal.valueOf(Constants.NOVGILLION), 1, RoundingMode.HALF_UP).toString() + "Novg";
        } else if (num.compareTo(BigDecimal.valueOf(Constants.OCVGILLION)) >= 0) {
            return num.divide(BigDecimal.valueOf(Constants.OCVGILLION), 1, RoundingMode.HALF_UP).toString() + "Ocvg";
        } else if (num.compareTo(BigDecimal.valueOf(Constants.SPVGILLION)) >= 0) {
            return num.divide(BigDecimal.valueOf(Constants.SPVGILLION), 1, RoundingMode.HALF_UP).toString() + "Spvg";
        } else if (num.compareTo(BigDecimal.valueOf(Constants.SXVGILLION)) >= 0) {
            return num.divide(BigDecimal.valueOf(Constants.SXVGILLION), 1, RoundingMode.HALF_UP).toString() + "Sxvg";
        } else if (num.compareTo(BigDecimal.valueOf(Constants.QIVGILLION)) >= 0) {
            return num.divide(BigDecimal.valueOf(Constants.QIVGILLION), 1, RoundingMode.HALF_UP).toString() + "Qivg";
        } else if (num.compareTo(BigDecimal.valueOf(Constants.QAVGILLION)) >= 0) {
            return num.divide(BigDecimal.valueOf(Constants.QAVGILLION), 1, RoundingMode.HALF_UP).toString() + "Qavg";
        } else if (num.compareTo(BigDecimal.valueOf(Constants.TVGILLION)) >= 0) {
            return num.divide(BigDecimal.valueOf(Constants.TVGILLION), 1, RoundingMode.HALF_UP).toString() + "Tvg";
        } else if (num.compareTo(BigDecimal.valueOf(Constants.DVGILLION)) >= 0) {
            return num.divide(BigDecimal.valueOf(Constants.DVGILLION), 1, RoundingMode.HALF_UP).toString() + "Dvg";
        } else if (num.compareTo(BigDecimal.valueOf(Constants.UVGILLION)) >= 0) {
            return num.divide(BigDecimal.valueOf(Constants.UVGILLION), 1, RoundingMode.HALF_UP).toString() + "Uvg";
        } else if (num.compareTo(BigDecimal.valueOf(Constants.VGILLION)) >= 0) {
            return num.divide(BigDecimal.valueOf(Constants.VGILLION), 1, RoundingMode.HALF_UP).toString() + "Vg";
        } else if (num.compareTo(BigDecimal.valueOf(Constants.NODILLION)) >= 0) {
            return num.divide(BigDecimal.valueOf(Constants.NODILLION), 1, RoundingMode.HALF_UP).toString() + "Nod";
        } else if (num.compareTo(BigDecimal.valueOf(Constants.OCDILLION)) >= 0) {
            return num.divide(BigDecimal.valueOf(Constants.OCDILLION), 1, RoundingMode.HALF_UP).toString() + "Ocd";
        } else if (num.compareTo(BigDecimal.valueOf(Constants.SPDILLION)) >= 0) {
            return num.divide(BigDecimal.valueOf(Constants.SPDILLION), 1, RoundingMode.HALF_UP).toString() + "Spd";
        } else if (num.compareTo(BigDecimal.valueOf(Constants.SXDILLION)) >= 0) {
            return num.divide(BigDecimal.valueOf(Constants.SXDILLION), 1, RoundingMode.HALF_UP).toString() + "Sxd";
        } else if (num.compareTo(BigDecimal.valueOf(Constants.QIDILLION)) >= 0) {
            return num.divide(BigDecimal.valueOf(Constants.QIDILLION), 1, RoundingMode.HALF_UP).toString() + "Qid";
        } else if (num.compareTo(BigDecimal.valueOf(Constants.QADILLION)) >= 0) {
            return num.divide(BigDecimal.valueOf(Constants.QADILLION), 1, RoundingMode.HALF_UP).toString() + "Qad";
        } else if (num.compareTo(BigDecimal.valueOf(Constants.TEDILLION)) >= 0) {
            return num.divide(BigDecimal.valueOf(Constants.TEDILLION), 1, RoundingMode.HALF_UP).toString() + "Td";
        } else if (num.compareTo(BigDecimal.valueOf(Constants.DEDILLION)) >= 0) {
            return num.divide(BigDecimal.valueOf(Constants.DEDILLION), 1, RoundingMode.HALF_UP).toString() + "Dd";
        } else if (num.compareTo(BigDecimal.valueOf(Constants.UDILLION)) >= 0) {
            return num.divide(BigDecimal.valueOf(Constants.UDILLION), 1, RoundingMode.HALF_UP).toString() + "Ud";
        } else if (num.compareTo(BigDecimal.valueOf(Constants.DECILLION)) >= 0) {
            return num.divide(BigDecimal.valueOf(Constants.DECILLION), 1, RoundingMode.HALF_UP).toString() + "Dc";
        } else if (num.compareTo(BigDecimal.valueOf(Constants.NONILLION)) >= 0) {
            return num.divide(BigDecimal.valueOf(Constants.NONILLION), 1, RoundingMode.HALF_UP).toString() + "No";
        } else if (num.compareTo(BigDecimal.valueOf(Constants.OCTILLION)) >= 0) {
            return num.divide(BigDecimal.valueOf(Constants.OCTILLION), 1, RoundingMode.HALF_UP).toString() + "Oc";
        } else if (num.compareTo(BigDecimal.valueOf(Constants.SEPTILLION)) >= 0) {
            return num.divide(BigDecimal.valueOf(Constants.SEPTILLION), 1, RoundingMode.HALF_UP).toString() + "Sp";
        } else if (num.compareTo(BigDecimal.valueOf(Constants.SEXTILLION)) >= 0) {
            return num.divide(BigDecimal.valueOf(Constants.SEXTILLION), 1, RoundingMode.HALF_UP).toString() + "Sx";
        } else if (num.compareTo(BigDecimal.valueOf(Constants.QUINTILLION)) >= 0) {
            return num.divide(BigDecimal.valueOf(Constants.QUINTILLION), 1, RoundingMode.HALF_UP).toString() + "Qi";
        } else if (num.compareTo(BigDecimal.valueOf(Constants.QUADRILLION)) >= 0) {
            return num.divide(BigDecimal.valueOf(Constants.QUADRILLION), 1, RoundingMode.HALF_UP).toString() + "Qa";
        } else if (num.compareTo(BigDecimal.valueOf(Constants.TRILLION)) >= 0) {
            return num.divide(BigDecimal.valueOf(Constants.TRILLION), 1, RoundingMode.HALF_UP).toString() + "T";
        } else if (num.compareTo(BigDecimal.valueOf(Constants.BILLION)) >= 0) {
            return num.divide(BigDecimal.valueOf(Constants.BILLION), 1, RoundingMode.HALF_UP).toString() + "B";
        } else if (num.compareTo(BigDecimal.valueOf(Constants.MILLION)) >= 0) {
            return num.divide(BigDecimal.valueOf(Constants.MILLION), 1, RoundingMode.HALF_UP).toString() + "M";
        } else if (num.compareTo(BigDecimal.valueOf(Constants.THOUSAND)) >= 0) {
            return num.divide(BigDecimal.valueOf(Constants.THOUSAND), 1, RoundingMode.HALF_UP).toString() + "K";
        } else {
            return decimalFormat.format(number);
        }
    }

    public static double parseValue(String value) throws NumberFormatException {
        String[] suffixes = {"Notg", "Octg", "Sptg", "Sxtg", "Qitg", "Qatg", "Ttg", "Dtg", "Utg", "Tg", "Novg", "Ocvg", "Spvg", "Sxvg", "Qivg", "Qavg", "Tvg", "Dvg", "Uvg", "Vg", "Nod", "Ocd", "Spd", "Sxd", "Qid", "Qad", "Td", "Dd", "Ud", "Dc", "No", "Oc", "Sp", "Sx", "Qi", "Qa", "T", "B", "M", "K"};
        BigDecimal[] values = {
                new BigDecimal(new BigInteger("1000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000")),
                new BigDecimal(new BigInteger("1000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000")),
                new BigDecimal(new BigInteger("1000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000")),
                new BigDecimal(new BigInteger("1000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000")),
                new BigDecimal(new BigInteger("1000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000")),
                new BigDecimal(new BigInteger("1000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000")),
                new BigDecimal(new BigInteger("1000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000")),
                new BigDecimal(new BigInteger("1000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000")),
                new BigDecimal(new BigInteger("1000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000")),
                new BigDecimal(new BigInteger("1000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000")),
                new BigDecimal(new BigInteger("1000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000")),
                new BigDecimal(new BigInteger("1000000000000000000000000000000000000000000000000000000000000000000000000000000000000000")),
                new BigDecimal(new BigInteger("1000000000000000000000000000000000000000000000000000000000000000000000000000000000000")),
                new BigDecimal(new BigInteger("1000000000000000000000000000000000000000000000000000000000000000000000000000000000")),
                new BigDecimal(new BigInteger("1000000000000000000000000000000000000000000000000000000000000000000000000000000")),
                new BigDecimal(new BigInteger("1000000000000000000000000000000000000000000000000000000000000000000000000000")),
                new BigDecimal(new BigInteger("1000000000000000000000000000000000000000000000000000000000000000000000000")),
                new BigDecimal(new BigInteger("1000000000000000000000000000000000000000000000000000000000000000000000")),
                new BigDecimal(new BigInteger("1000000000000000000000000000000000000000000000000000000000000000000")),
                new BigDecimal(new BigInteger("1000000000000000000000000000000000000000000000000000000000000000")),
                new BigDecimal(new BigInteger("1000000000000000000000000000000000000000000000000000000000000")),
                new BigDecimal(new BigInteger("1000000000000000000000000000000000000000000000000000000000")),
                new BigDecimal(new BigInteger("1000000000000000000000000000000000000000000000000000000")),
                new BigDecimal(new BigInteger("1000000000000000000000000000000000000000000000000000")),
                new BigDecimal(new BigInteger("1000000000000000000000000000000000000000000000000")),
                new BigDecimal(new BigInteger("1000000000000000000000000000000000000000000000")),
                new BigDecimal(new BigInteger("1000000000000000000000000000000000000000000")),
                new BigDecimal(new BigInteger("1000000000000000000000000000000000000000")),
                new BigDecimal(new BigInteger("1000000000000000000000000000000000000")),
                new BigDecimal(new BigInteger("1000000000000000000000000000000000")),
                new BigDecimal(new BigInteger("1000000000000000000000000000000")),
                new BigDecimal(new BigInteger("1000000000000000000000000000")),
                new BigDecimal(new BigInteger("1000000000000000000000000")),
                new BigDecimal(new BigInteger("1000000000000000000000")),
                new BigDecimal(new BigInteger("1000000000000000000")),
                new BigDecimal(new BigInteger("1000000000000000")),
                new BigDecimal(new BigInteger("1000000000000")),
                new BigDecimal(new BigInteger("1000000000")),
                new BigDecimal(new BigInteger("1000000")),
                BigDecimal.valueOf(Constants.THOUSAND),
                BigDecimal.valueOf(Constants.ONE)
        };
        for (int i = 0; i < suffixes.length; i++) {
            if (value.endsWith(suffixes[i])) {
                return new BigDecimal(value.replace(suffixes[i], "")).multiply(values[i]).doubleValue();
            }
        }
        return Double.parseDouble(value);
    }
}
