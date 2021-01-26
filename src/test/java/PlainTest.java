//import com.sun.javafx.binding.StringFormatter;
//import org.junit.Test;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//public class PlainTest {
//
//    @Test
//    public void generateTicketId(){
//        long agentId = 435;
//        String ticketId = String.format("%05d", 19004);
//        String tickId = new StringBuilder().append("T")
//                .append(String.valueOf(new Date().getTime()).substring(1,10)).toString();
//        System.out.println(tickId);
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd");
//        try {
//            Date newDate = dateFormat.parse("2020-sep-22");
//            System.out.println(dateFormat.format(newDate));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        String test = "21024";
//        System.out.println(test.substring(1,4));
//    }
//
//    @Test
//    public void testDateFormatter(){
//
//        Date date = new Date();
//        String[] suffixes =
//                //    0     1     2     3     4     5     6     7     8     9
//                { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
//                        //    10    11    12    13    14    15    16    17    18    19
//                        "th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
//                        //    20    21    22    23    24    25    26    27    28    29
//                        "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
//                        //    30    31
//                        "th", "st" };
//
//        SimpleDateFormat formatDayOfMonth  = new SimpleDateFormat("d");
//        SimpleDateFormat formatMonthOfYear  = new SimpleDateFormat("MMMMM");
//        int day = Integer.parseInt(formatDayOfMonth.format(date));
//        String dayStr = day + suffixes[day];
//
//        System.out.println(dayStr+" of "+formatMonthOfYear.format(date));
//
//
//    }
//}
