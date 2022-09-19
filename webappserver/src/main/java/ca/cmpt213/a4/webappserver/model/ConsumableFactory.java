package ca.cmpt213.a4.webappserver.model;

/**
 * Factory class that encapsulates the creation of
 * all consumable objects
 */
public class ConsumableFactory {

    /**
     * @param type the integer specifying what consumable is to be instantiated.
     * @return returns a food object if type == "Food", a drink object if type == "Drink",
     * null otherwise
     */
    public Consumable getInstance(String type) {
        if (type == null) {
            return null;
        } else if (type.equals("Food")) {
            return new Food();
        } else if (type.equals("Drink")) {
            return new Drink();
        }

        return null;
    }
}
