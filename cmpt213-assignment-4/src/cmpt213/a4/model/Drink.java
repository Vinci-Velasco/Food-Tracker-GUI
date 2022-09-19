package cmpt213.a4.model;

import java.time.format.DateTimeFormatter;

/**
 * A specific type of consumable that represents drink. Contains all what Consumable has,
 * but includes an extra field to record its volume.
 */
public class Drink extends Consumable {
    private double volume;

    @Override
    public void setExtraDoubleField(double value) {
        volume = value;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        int daysBeforeExpir = daysBeforeExpiration();
        String expirationDateMessage = "";

        // expired
        if (daysBeforeExpir < 0) {
            daysBeforeExpir *= -1;
            expirationDateMessage = "This drink item is expired for " + daysBeforeExpir +
                    " days(s)";
        }

        // NOT expired
        else if (daysBeforeExpir > 0) {
            expirationDateMessage = "This drink item will expire in " + daysBeforeExpir +
                    " day(s)";
        } else {
            expirationDateMessage = "This drink item will expire today";
        }

        return "<html>Name: " + name + "<br>" +
                "Notes: " + notes + "<br>" +
                "Price: " + String.format("%.2f", price) + "<br>" +
                "Volume: " + String.format("%.2f", volume) + "<br>" +
                "Expiry Date: " + expiryDate.format(formatter) + "<br>" +
                expirationDateMessage + "<br></html>";
    }
}
