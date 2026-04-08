package ie.tus.eng.country.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import ie.tus.eng.country.model.Country;
import ie.tus.eng.country.repository.CountryRepository;

@RestController
@RequestMapping("/countries")
public class CountryController {

	private final CountryRepository repository;

	public CountryController(CountryRepository repository) {
		this.repository = repository;
	}

	// GET all countries
	@GetMapping
	public List<Country> getAllCountries() {
		return repository.findAll();
	}

	// GET one country by id
	@GetMapping("/{id}")
	public ResponseEntity<Country> getOneCountry(@PathVariable Long id) {
		Optional<Country> country = repository.findById(id);

		if (country.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(country.get());
	}

	// POST create a new country
	@PostMapping
	public ResponseEntity<Country> createCountry(@RequestBody Country country) {
		Country savedCountry = repository.save(country);

		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(savedCountry.getCountryId())
				.toUri();

		return ResponseEntity.created(location).body(savedCountry);
	}

	// PUT update an existing country
	@PutMapping("/{id}")
	public ResponseEntity<Country> editCountry(@PathVariable Long id, @RequestBody Country country) {

		if (!repository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}

		country.setCountryId(id);
		Country updatedCountry = repository.save(country);

		return ResponseEntity.ok(updatedCountry);
	}

	// DELETE one country
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCountry(@PathVariable Long id) {
		if (!repository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}

		repository.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
