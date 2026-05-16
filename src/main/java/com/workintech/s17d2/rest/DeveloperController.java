package com.workintech.s17d2.rest;

import com.workintech.s17d2.model.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.workintech.s17d2.tax.Taxable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class DeveloperController {
    private final Taxable taxable;
    public Map<Integer, Developer> developers = new HashMap<>();

    @Autowired
    public DeveloperController(Taxable taxable) {
        this.taxable = taxable;

    }


    @PostConstruct
    public void init(){
        developers.put(1, new JuniorDeveloper(1,"someone", 28000));
    }

    @GetMapping("/developers")
    public List<Developer> getValue(){
        return new ArrayList<>(developers.values());
    }

    @GetMapping("/developers/{id}")
    public Developer getDevelopersById(@PathVariable Integer id){
        if(developers.containsKey(id)){

            return developers.get(id);
        }
        return null;
    }
    @PostMapping("/developers")
    @ResponseStatus(HttpStatus.CREATED)
    public Developer addDeveloper(@RequestBody Developer developer){

        int id = developer.getId();
        String name = developer.getName();
        double rawSalary = developer.getSalary();
        Experience exp = developer.getExperience();

        Developer savedDeveloper;
        int netSalary;

        switch (exp){
            case JUNIOR:
                netSalary = (int) (rawSalary -(rawSalary * taxable.getSimpleTaxRate()));
                savedDeveloper = new JuniorDeveloper(id, name, netSalary);
                break;
            case MID:
                netSalary = (int)(rawSalary - (rawSalary * taxable.getMiddleTaxRate()));
                savedDeveloper = new MidDeveloper(id, name,netSalary);
                break;
            case SENIOR:
                netSalary = (int)(rawSalary - (rawSalary * taxable.getUpperTaxRate()));
                savedDeveloper = new SeniorDeveloper(id, name, netSalary);
                break;
            default:
                savedDeveloper = developer;
        }
        developers.put(id, savedDeveloper);
        return savedDeveloper;
    }

    @PutMapping("/developers/{id}")
    public Developer updateDeveloper(@PathVariable Integer id, @RequestBody Developer developer) {
        if (!developers.containsKey(id)) {
            return null;
        }

        String name = developer.getName();
        double rawSalary = developer.getSalary();
        Experience exp = developer.getExperience();

        Developer updatedDeveloper;
        int netSalary;

        switch (exp) {
            case JUNIOR:
                netSalary = (int) (rawSalary - (rawSalary * taxable.getSimpleTaxRate()));
                updatedDeveloper = new JuniorDeveloper(id, name, netSalary);
                break;
            case MID:
                netSalary = (int) (rawSalary - (rawSalary * taxable.getMiddleTaxRate()));
                updatedDeveloper = new MidDeveloper(id, name, netSalary);
                break;
            case SENIOR:
                netSalary = (int) (rawSalary - (rawSalary * taxable.getUpperTaxRate()));
                updatedDeveloper = new SeniorDeveloper(id, name, netSalary);
                break;
            default:
                updatedDeveloper = developer;
                break;
        }

        developers.put(id, updatedDeveloper);
        return updatedDeveloper;
    }
    @DeleteMapping("/developers/{id}")
    public Developer deleteDeveloper(@PathVariable Integer id){
        if(developers.containsKey(id)){
            return developers.remove(id);
        }
        return null;
    }


}
