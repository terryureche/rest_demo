package com.terry.rest_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.persistence.Entity;
import javax.persistence.Id;
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

interface CoffeeRepository extends CrudRepository<Coffee, String>{};

@Entity
class Coffee {
    @Id
    private String id;
    private String name;

    public Coffee(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Coffee() {}

    public Coffee(String name) {
        this(UUID.randomUUID().toString(), name);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

//@Component
class DataLoader{
    private final CoffeeRepository coffeeRepositroy;

    public DataLoader(CoffeeRepository coffeeRepositroy) {
        this.coffeeRepositroy = coffeeRepositroy;
    }

    @PostConstruct //@PostContruct restores RestApiDemoController to its intended single purpose of providing an external API and makes the DataLoader responsible for its intended (and obvious) purpose.
    private void loadData() {
        this.coffeeRepositroy.saveAll(List.of(
                new Coffee("Gigel Cereza"),
                new Coffee("Gigel Ganador"),
                new Coffee("Gigel Lareno"),
                new Coffee("Gigel Tres Pontas"),
                new Coffee("Gigel Zacuscas")
        ));
    }
}

@RestController
@RequestMapping("/coffees") //declar the api for this endpoint
class RestApiDemoController {
    private final CoffeeRepository coffeeRepository;

    public RestApiDemoController(CoffeeRepository coffeeRepository) {
        this.coffeeRepository = coffeeRepository;

//        this.coffeeRepository.saveAll(List.of(
//                new Coffee("Cafe Cereza"),
//                new Coffee("Cafe Ganador"),
//                new Coffee("Cafe Lareno"),
//                new Coffee("Cafe Tres Pontas"),
//                new Coffee("Cafe Zacuscas")
//        ));
    }

    //    @RequestMapping(value = "/coffees", method = RequestMethod.GET)
    //    @GetMapping("/coffees") //the same as above only it's directly a GET type
    @GetMapping //Since we have declared the general url to the class level
    Iterable<Coffee> getCoffees() {
        return coffeeRepository.findAll();
    }

    //    @GetMapping("/coffees/{id}")
    @GetMapping("/{id}") //Since we have declared the general url to the class level
    Optional<Coffee> getCoffeeById(@PathVariable String id) {
        return coffeeRepository.findById(id);
    }

    //    @PostMapping("/coffees")
    @PostMapping //Since we have declared the general url to the class level
    Coffee postCoffee(@RequestBody Coffee coffee) {
        return coffeeRepository.save(coffee);
    }

    //    @PutMapping("/coffees/{id}")
    //    Coffee putCoffee(@PathVariable String id, @RequestBody Coffee coffee) {
    @PutMapping("/{id}") //Since we have declared the general url to the class level
    ResponseEntity<Coffee> putCoffee(@PathVariable String id, @RequestBody Coffee coffee) {
        /* return (coffeeIndex == -1) ? postCoffee(coffee) : coffee; */
        return (coffeeRepository.existsById(id))
            ? new ResponseEntity<>(coffeeRepository.save(coffee), HttpStatus.OK) //we will response with sattus code 200(ok - updated)
            : new ResponseEntity<>(coffeeRepository.save(coffee), HttpStatus.CREATED); //we will response with status code 201(created)
    }

    //    @DeleteMapping("/coffees/{id}")

    @DeleteMapping("/{id}") //Since we have declared the general url to the class level
    void deleteCoffee(@PathVariable String id) {
        coffeeRepository.deleteById(id);
    }
}
