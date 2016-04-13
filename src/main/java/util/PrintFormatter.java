package util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class PrintFormatter {

    public DecimalFormat df;

    public PrintFormatter() {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.CANADA);
        df = (DecimalFormat)nf;
        df.setMinimumIntegerDigits(1);
        df.setMinimumFractionDigits(1);
        df.setMaximumFractionDigits(10);
    }

    public DecimalFormat getDf() {
        return df;
    }

}
