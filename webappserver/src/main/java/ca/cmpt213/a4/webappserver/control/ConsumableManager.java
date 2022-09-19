package ca.cmpt213.a4.webappserver.control;

import ca.cmpt213.a4.webappserver.model.Consumable;
import ca.cmpt213.a4.webappserver.model.ConsumableFactory;
import ca.cmpt213.a4.webappserver.model.Drink;
import ca.cmpt213.a4.webappserver.model.Food;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class that manages Consumables in the server side. Contains the logic of sorting, adding,
 * and deleting consumables and saving it to a JSON file.
 */
public class ConsumableManager {
    private ArrayList<Consumable> itemList;
    private Gson myGson;
    private AtomicInteger atomicInteger = new AtomicInteger();

    public ConsumableManager() {
        itemList = new ArrayList<>();

        // Configure Gson class to support LocalDate objects
        myGson = new GsonBuilder().registerTypeAdapter(LocalDate.class,
                    new TypeAdapter<LocalDate>() {
            @Override
            public void write(JsonWriter jsonWriter,
                    LocalDate localDate) throws IOException {
                jsonWriter.value(localDate.toString());
            }

            @Override
            public LocalDate read(JsonReader jsonReader) throws IOException {
                return LocalDate.parse(jsonReader.nextString());
            }
        }).create();

        File file = new File("itemList.json");

        // Manually go through each json object to specify what concrete implementation
        // an object is before putting it in the itemList
            try {
            JsonElement fileElement = JsonParser.parseReader(new FileReader(file));
            JsonArray jsonArrayOfConsumables = fileElement.getAsJsonArray();

            for (JsonElement consumableElement : jsonArrayOfConsumables) {
                JsonObject consumableJsonObject = consumableElement.getAsJsonObject();

                long id = consumableJsonObject.get("id").getAsLong();
                String name = consumableJsonObject.get("name").getAsString();
                String notes = consumableJsonObject.get("notes").getAsString();
                double price = consumableJsonObject.get("price").getAsDouble();

                String stringExpiryDate = consumableJsonObject.get("expiryDate").getAsString();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate expiryDate = LocalDate.parse(stringExpiryDate, formatter);

                ConsumableFactory consumableFactory = new ConsumableFactory();

                // object is a food instance
                if (consumableJsonObject.has("weight")) {
                    double weight = consumableJsonObject.get("weight").getAsDouble();
                    Consumable newConsumable = consumableFactory.getInstance("Food");

                    newConsumable.setId(id);
                    newConsumable.setName(name);
                    newConsumable.setNotes(notes);
                    newConsumable.setPrice(price);
                    newConsumable.setExtraDoubleField(weight);
                    newConsumable.setExpiryDate(expiryDate);

                    itemList.add(newConsumable);

                } else { // object is a drink instance
                    double volume = consumableJsonObject.get("volume").getAsDouble();
                    Consumable newConsumable = consumableFactory.getInstance("Drink");

                    newConsumable.setName(name);
                    newConsumable.setNotes(notes);
                    newConsumable.setPrice(price);
                    newConsumable.setExtraDoubleField(volume);
                    newConsumable.setExpiryDate(expiryDate);

                    itemList.add(newConsumable);
                }
            }

        } catch (FileNotFoundException e) {
            // file was not found so itemList is empty
        }
    }

    /**
     * Method that returns the all items in order from expired to non-expired
     *
     * @return ArrayList that consists of all the items ordered
     */
    public ArrayList<Consumable> getItems() {
        Collections.sort(itemList);
        return itemList;
    }

    /**
     * Method that returns the all expired items
     *
     * @return ArrayList that consists of all the items ordered
     */
    public ArrayList<Consumable> getExpiredItems() {
        Collections.sort(itemList);
        ArrayList<Consumable> expiredItems = new ArrayList<>();

        for (Consumable item : itemList) {

            // item is expired if daysBeforeExpiration() returns a negative int
            if (item.daysBeforeExpiration() < 0) {
                expiredItems.add(item);
            }
        }
        return expiredItems;
    }

    /**
     * Method that returns all items that have NOT been expired
     *
     * @return ArrayList that contains all non-expired items
     */
    public ArrayList<Consumable> getNonExpiredItems() {
        Collections.sort(itemList);
        ArrayList<Consumable> nonExpiredItems = new ArrayList<>();

        for (Consumable item : itemList) {
            // item is non-expired if daysBeforeExpiration() returns a non-negative int
            if (item.daysBeforeExpiration() >= 0) {
                nonExpiredItems.add(item);
            }
        }
        return nonExpiredItems;
    }

    /**
     * Method that returns all items that expire within 7 days
     *
     * @return ArrayList that contains all items expiring within 7 days
     */
    public ArrayList<Consumable> getItemsExpiringWithin7Days() {
        Collections.sort(itemList);
        ArrayList<Consumable> itemsExpiringSoon = new ArrayList<>();

        for (Consumable item : itemList) {
            // All item that expires within 7 days are added
            if (item.daysBeforeExpiration() > 0 && item.daysBeforeExpiration() <= 7) {
                itemsExpiringSoon.add(item);
            }
        }
        return itemsExpiringSoon;
    }


    /**
     * Method that reads in an item (as a json string), adds it into itemList and returns it
     * @param jsonBody The item as a json string
     * @param type Food or Drink
     * @return ArrayList that contains all items
     */
    public ArrayList<Consumable> addItem(String jsonBody, String type) {
        Consumable item;

        if (Objects.equals(type, "Food")) {
           item = myGson.fromJson(jsonBody, Food.class);
        } else if (Objects.equals(type, "Drink")){
            item = myGson.fromJson(jsonBody, Drink.class);
        } else {
            item = new Food();
        }

        item.setId(atomicInteger.getAndIncrement());

        itemList.add(item);
        return itemList;
    }

    /**
     * Method that removes an item from itemList from its id and returns the newly
     * updated itemList
     * @param id The id of the object to be deleted
     * @return ArrayList that contains all items
     */
    public ArrayList<Consumable> removeItem(long id) {
        for (Consumable item : itemList) {
            if (item.getId() == id) {
                itemList.remove(item);
                break;
            }
        }

        return itemList;
    }

    /**
     * Method that saves the itemList into a json file
     */
    public void saveToJsonFile() {
        try {
            FileWriter file = new FileWriter("itemList.json");

            // Convert to array first, as reading arrays from json files are easier
            myGson.toJson(itemList.toArray(), file);
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
