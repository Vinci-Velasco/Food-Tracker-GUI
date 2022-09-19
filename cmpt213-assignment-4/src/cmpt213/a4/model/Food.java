package cmpt213.a4.model;

import java.time.format.DateTimeFormatter;

/**
 * A specific type of consumable that represents food. Contains all what Consumable has,
 * but includes an extra field to record its weight
 */
public class Food extends Consumable {
    private double weight;

    @Override
    public void setExtraDoubleField(double value) {
        weight = value;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        int daysBeforeExpir = daysBeforeExpiration();
        String expirationDateMessage = "";

        // expired
        if (daysBeforeExpir < 0) {
            daysBeforeExpir *= -1;
            expirationDateMessage = "This food item is expired for " + daysBeforeExpir +
                    " days(s)";
        }

        // NOT expired
        else if (daysBeforeExpir > 0) {
            expirationDateMessage = "This food item will expire in " + daysBeforeExpir +
                    " day(s)";
        } else {
            expirationDateMessage = "This food item will expire today";
        }

        return "<html>Name: " + name + "<br>" +
                "Notes: " + notes + "<br>" +
                "Price: " + String.format("%.2f", price) + "<br>" +
                "Weight: " + String.format("%.2f", weight) + "<br>" +
                "Expiry Date: " + expiryDate.format(formatter) + "<br>" +
                expirationDateMessage + "<br></html>";
    }
}
