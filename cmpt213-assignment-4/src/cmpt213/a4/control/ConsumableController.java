package cmpt213.a4.control;

import cmpt213.a4.model.Consumable;
import cmpt213.a4.model.ConsumableFactory;
import cmpt213.a4.model.Drink;
import cmpt213.a4.model.Food;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Handles the managing of Consumable objects. Contains methods that sends HTTP requests
 * to the server side (REST API) to retrieve and return the data.
 */
public class ConsumableController {
    private Gson myGson;
    private ArrayList<Consumable> itemList = new ArrayList<>();

    private static final String baseURL = "http://localhost:8080";
    HttpURLConnection connection = null;

    public ConsumableController() {

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
    }


    /**
     * Method that requests from the server of all items (oldest first) and returns it
     *
     * @return ArrayList that consists of all the items ordered
     */
    public ArrayList<Consumable> getItems() {
        return requestFromServerSide("/listAll/");
    }

    /**
     * Method that requests from the server of all items that have been expired
     * and returns it.
     *
     * @return ArrayList that contains all expired items
     */
    public ArrayList<Consumable> getExpiredItems() {
        return requestFromServerSide("/listExpired/");
    }

    /**
     * Method that requests from the server of all items that have not been expired
     * and returns it.
     *
     * @return ArrayList that contains all non-expired items
     */
    public ArrayList<Consumable> getNonExpiredItems() {
        return requestFromServerSide("/listNonExpired/");
    }

    /**
     * Method that requests from the server of all items that will expire in 7 days
     * and returns it.
     *
     * @return ArrayList that contains all items expiring within 7 days
     */
    public ArrayList<Consumable> getItemsExpiringWithin7Days() {
        return requestFromServerSide("/listExpiringIn7Days/");
    }

    /**
     * Method that sends a post request to the server that will delete an item and
     * returns the received list.
     * @param index The index of the item to be removed
     */
    public void deleteItem(int index) {
        long id = itemList.get(index).getId();
        itemList.clear();
        String endPath = "/removeItem/" + id;

        try {
            URL url = new URL(baseURL + endPath);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Although the server side returns the newly updated list of items, it does not be read
        // here because the list of items will be requested in a different method depending on the
        // current page.
    }

    /**
     * Method that sends a post request to the server, adding a new item (sent
     * as a json object)
     * @param item The item that is being added to the system.
     */
    public void addItem(Consumable item) {

        String endPath = "";
        if (item instanceof Food) {
            endPath = "/addItem/food";
        } else if (item instanceof Drink) {
            endPath = "/addItem/drink";
        }

        try {
            URL url = new URL(baseURL + endPath);
            connection = (HttpURLConnection) url.openConnection();

            // Necessary to make sure the request sent is a POST request with a JSON body
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            // Send the JSON body
            String consumableAsJson = myGson.toJson(item);
            try(OutputStream os = connection.getOutputStream()) {
                byte[] input = consumableAsJson.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Although the server side returns the newly updated list of items, it does not be read
        // here because the list of items will be requested in a different method depending on the
        // current page.
    }

    /**
     * Method that sends a request to the server to save the list into a json file
     */
    public void saveToJsonFile() {

        try {
            URL url = new URL(baseURL + "/exit");
            connection = (HttpURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper method that sends http requests to the server side and stores the data
     * @param endPath the ending of the url path
     */
    private ArrayList<Consumable> requestFromServerSide(String endPath) {
        itemList.clear();

        String jsonAsString = "";
        try {
            URL url = new URL(baseURL + endPath);
            connection = (HttpURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            jsonAsString = in.readLine();
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!jsonAsString.equals("[]")) {
            readJsonStringIntoList(jsonAsString);
        }

        return itemList;
    }


    /**
     * Helper method that takes a string of an array of Consumables (in JSON format)
     * converts into JSON using gson (so it's easy to parse), parse into Consumable
     * objects, then add to itemList.
     */
    private void readJsonStringIntoList(String itemsAsString) {
        JsonElement initialJsonElement = JsonParser.parseString(itemsAsString);
        JsonArray jsonArrayOfConsumables = initialJsonElement.getAsJsonArray();

        for (JsonElement consumableElement : jsonArrayOfConsumables) {
            JsonObject consumableJsonObject = consumableElement.getAsJsonObject();

            long id = consumableJsonObject.get("id").getAsLong();
            String name = consumableJsonObject.get("name").getAsString();
            String notes = consumableJsonObject.get("notes").getAsString();
            double price = consumableJsonObject.get("price").getAsDouble();

            String stringExpiryDate = consumableJsonObject.get("expiryDate").getAsString();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate expiryDate = LocalDate.parse(stringExpiryDate, formatter);

            double weightOrVolume = consumableJsonObject.get("extraDoubleField").getAsDouble();
            String type = consumableJsonObject.get("type").getAsString();
            type = type.substring(0,1).toUpperCase() + type.substring(1);

            ConsumableFactory consumableFactory = new ConsumableFactory();
            Consumable newConsumable = consumableFactory.getInstance(type);

            newConsumable.setId(id);
            newConsumable.setName(name);
            newConsumable.setNotes(notes);
            newConsumable.setPrice(price);
            newConsumable.setExtraDoubleField(weightOrVolume);
            newConsumable.setExpiryDate(expiryDate);

            itemList.add(newConsumable);
        }
    }
}
