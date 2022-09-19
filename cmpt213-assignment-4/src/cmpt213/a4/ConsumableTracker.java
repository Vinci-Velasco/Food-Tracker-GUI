package cmpt213.a4;/*  CITATIONS

Converting arrays to ArrayList and vice versa (idea thought up to make using Gson easier):
    1. https://stackoverflow.com/questions/5374311/convert-arrayliststring-to-string-array
    2. https://stackoverflow.com/questions/157944/create-arraylist-from-array

Information about ChronoUnit:
    1. https://docs.oracle.com/javase/8/docs/api/java/time/temporal/ChronoUnit.html

Information about dateTimeFormatter (specifically .ofPattern()):
    1. https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html

Information about Gson and deserializing:
    1. https://www.youtube.com/watch?v=HSuVtkdej8Q

Information about Swing:
    1. https://stackoverflow.com/questions/21084312/jdialog-popup-too-small
    2. https://stackoverflow.com/questions/4089311/how-can-i-return-a-value-from-a-jdialog-box-to-the-parent-jframe
    3. https://stackoverflow.com/questions/58939/jcombobox-selection-change-listener
    4. https://docs.oracle.com/javase/tutorial/uiswing/components/border.html

Information about HttpUrlConnection:
    1. https://docs.oracle.com/javase/tutorial/networking/urls/readingWriting.html
    2. https://www.baeldung.com/httpurlconnection-post
 */

import cmpt213.a4.view.Menu;
import javax.swing.*;

public class ConsumableTracker {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Menu();
            }
        });
    }
}
