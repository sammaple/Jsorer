
package soccer.access.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class tools {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Date date = new Date();
        System.out.println(date.getTime());

        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat format2 = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        String str = null;

        // String转Date
        str = "2013-10-7";
        str = "2013年9月29日 00时00分01秒";
        try {
            date = format2.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(date.getTime());
    }
}
