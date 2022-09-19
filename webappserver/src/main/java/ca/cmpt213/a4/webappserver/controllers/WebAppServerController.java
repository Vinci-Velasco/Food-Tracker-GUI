package ca.cmpt213.a4.webappserver.controllers;

import ca.cmpt213.a4.webappserver.control.ConsumableManager;
import ca.cmpt213.a4.webappserver.model.Consumable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class WebAppServerController {

    private ConsumableManager consumableManager;

    public WebAppServerController() {
        consumableManager = new ConsumableManager();
    }

    @GetMapping("/ping")
    public String systemStatus() {
        return "System is up!";
    }

    @GetMapping("/listAll")
    public ArrayList<Consumable> listAll() {
        return consumableManager.getItems();
    }

    @GetMapping("/listExpired")
    public ArrayList<Consumable> listExpired() {
        return consumableManager.getExpiredItems();
    }

    @GetMapping("/listNonExpired")
    public ArrayList<Consumable> listNonExpired() {
        return consumableManager.getNonExpiredItems();
    }

    @GetMapping("/listExpiringIn7Days")
    public ArrayList<Consumable> listExpiringIn7Days() {
        return consumableManager.getItemsExpiringWithin7Days();
    }

    // Did not use Jackson to convert into Consumable object because of polymorphism
    // issues (the requestBody can be a food or a drink and does not match the fields of
    // a Consumable)
    @PostMapping("/addItem/food")
    public ArrayList<Consumable> addFood(@RequestBody String jsonBody) {
        return consumableManager.addItem(jsonBody, "Food");
    }

    // Did not use Jackson to convert into Consumable object because of polymorphism
    // issues (the requestBody can be a food or a drink and does not match the fields of
    // a Consumable)
    @PostMapping("/addItem/drink")
    public ArrayList<Consumable> addDrink(@RequestBody String jsonBody) {
        return consumableManager.addItem(jsonBody, "Drink");
    }

    @PostMapping("/removeItem/{id}")
    public ArrayList<Consumable> removeItem(@PathVariable("id") long id) {
        return consumableManager.removeItem(id);
    }

    @GetMapping("/exit")
    public void saveToJsonFile() {
        consumableManager.saveToJsonFile();
    }

}
