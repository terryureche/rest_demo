package com.terry.rest_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
public class RestDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestDemoApplication.class, args);
    }

}

class Coffee {
    private final String id;
    private String name;

    public Coffee(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Coffee(String name) {
        this(UUID.randomUUID().toString(), name);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

@RestController
@RequestMapping("/coffees") //declar the api for this endpoint
class RestApiDemoController {
    private List<Coffee> coffees = new ArrayList<>();

    public RestApiDemoController() {
        coffees.addAll(List.of(
                new Coffee("Cafe Cereza"),
                new Coffee("Cafe Ganador"),
                new Coffee("Cafe Lareno"),
                new Coffee("Cafe Tres Pontas"),
                new Coffee("Cafe Zacuscas")
        ));
    }

    //    @RequestMapping(value = "/coffees", method = RequestMethod.GET)
    //    @GetMapping("/coffees") //the same as above only it's directly a GET type
    @GetMapping //Since we have declared the general url to the class level
    Iterable<Coffee> getCoffees() {
        return coffees;
    }

    //    @GetMapping("/coffees/{id}")
    @GetMapping("/{id}") //Since we have declared the general url to the class level
    Optional<Coffee> getCoffeeById(@PathVariable String id) {
        for (Coffee c: coffees) {
            if(c.getId().equals(id)) {
                return Optional.of(c);
            }
        }

        return Optional.empty();
    }

    //    @PostMapping("/coffees")
    @PostMapping //Since we have declared the general url to the class level
    Coffee postCoffee(@RequestBody Coffee coffee) {
        coffees.add(coffee);

        return coffee;
    }

    //    @PutMapping("/coffees/{id}")
    //    Coffee putCoffee(@PathVariable String id, @RequestBody Coffee coffee) {
    @PutMapping("/{id}") //Since we have declared the general url to the class level
    ResponseEntity<Coffee> putCoffee(@PathVariable String id, @RequestBody Coffee coffee) {
        int coffeeIndex = -1;

        for (Coffee c: coffees) {
            if(c.getId().equals(id)) {
                coffeeIndex = coffees.indexOf(c);

                coffees.set(coffeeIndex, coffee);
            }
        }

        /* return (coffeeIndex == -1) ? postCoffee(coffee) : coffee; */
        return (coffeeIndex == -1)
            ? new ResponseEntity<>(postCoffee(coffee), HttpStatus.CREATED) //we will response with status code 201(created)
            : new ResponseEntity<>(coffee, HttpStatus.OK); //we will response with sattus code 200(ok - updated)
    }

    //    @DeleteMapping("/coffees/{id}")

    @DeleteMapping("/{id}") //Since we have declared the general url to the class level
    void deleteCoffee(@PathVariable String id) {
        coffees.removeIf(c -> c.getId().equals(id));
    }
}