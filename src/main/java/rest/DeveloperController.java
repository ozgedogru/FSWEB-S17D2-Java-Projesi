package rest;

import jakarta.annotation.PostConstruct;
import model.Developer;
import org.springframework.web.bind.annotation.*;
import tax.Taxable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/developers")
public class DeveloperController {
    private final Taxable developerTax;
    private Map<Integer, Developer> developers;

    public DeveloperController(Taxable developerTax) {
        this.developerTax = developerTax;
    }

    @PostConstruct
    private void init() {
        developers = new HashMap<>();
    }

    @GetMapping
    public List<Developer> getAllDevelopers() {
        return new ArrayList<>(developers.values());
    }

    @GetMapping("/{id}")
    public Developer getDeveloperById(@PathVariable int id) {
        return developers.get(id);
    }

    @PostMapping
    public Developer addDeveloper(@RequestBody Developer developer) {
        double salary = developer.getSalary();
        switch (developer.getExperience()) {
            case JUNIOR:
                salary -= developerTax.getSimpleTaxRate() * salary;
                break;
            case MID:
                salary -= developerTax.getMiddleTaxRate() * salary;
                break;
            case SENIOR:
                salary -= developerTax.getUpperTaxRate() * salary;
                break;
        }
        developer.setSalary(salary);
        developers.put(developer.getId(), developer);
        return developer;
    }

    @PutMapping("/{id}")
    public Developer updateDeveloper(@PathVariable int id, @RequestBody Developer updatedDeveloper) {
        developers.put(id, updatedDeveloper);
        return updatedDeveloper;
    }

    @DeleteMapping("/{id}")
    public void deleteDeveloper(@PathVariable int id) {
        developers.remove(id);
    }
}
