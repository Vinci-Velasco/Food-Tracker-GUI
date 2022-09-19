package cmpt213.a4.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Class that represents a consumables a user will want to track.
 * It includes the consumables name, general notes, price, and expiry date.
 */
public abstract class Consumable implements Comparable<Consumable> {
    protected long id;
    protected String name;
    protected String notes;
    protected double price;
    protected LocalDate expiryDate;


    /**
     * Getter for name field
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for name field
     */
    public long getId() {
        return id;
    }

    /**
     * Setter for name field
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter for id field
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Setter for notes field
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Setter for price field
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Setter for expiryDate field
     */
    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }


    /**
     * Setter that sets the value of an extra double field. All subclasses of Consumable
     * have their own specific field and will implement this method.
     */
    public abstract void setExtraDoubleField(double value);

    /**
     * Method that calculates days before expiration
     *
     * @return returns amount of days food item has before expiration. If food has
     * already expired, a negative number of days will be returned.
     */
    public int daysBeforeExpiration() {
        LocalDate currentDate = LocalDate.now();
        long daysBetween = ChronoUnit.DAYS.between(currentDate, expiryDate);

        return Math.toIntExact(daysBetween);
    }

    @Override
    public int compareTo(Consumable other) {
        // Makes sorting algorithms sort Consumables by oldest item
        return Integer.compare(daysBeforeExpiration(), other.daysBeforeExpiration());
    }
}
