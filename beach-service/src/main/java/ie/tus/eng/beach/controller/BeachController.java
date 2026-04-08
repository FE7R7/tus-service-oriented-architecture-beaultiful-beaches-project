package ie.tus.eng.beach.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

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

import ie.tus.eng.beach.country.CountryClient;
import ie.tus.eng.beach.model.Beach;
import ie.tus.eng.beach.model.BeachResponse;
import ie.tus.eng.beach.repository.BeachRepository;

@RestController @RequestMapping("/beaches")
public class BeachController {

	private final BeachRepository repository;
	private final CountryClient countryClient;

	public BeachController(BeachRepository repository, CountryClient countryClient) {
		this.repository = repository;
		this.countryClient = countryClient;
	}

	// GET all beaches - ETag caching is applied to this endpoint via ETagConfig
	// filter. ShallowEtagHeaderFilter computes a hash of the response body.
	// The endpoint now returns CompletableFuture to fetch Country details for all
	// beaches concurrently.
	@GetMapping
	public CompletableFuture<ResponseEntity<List<BeachResponse>>> getAllBeaches() {
		List<Beach> beaches = repository.findAll();

		// Asynchronously fetch country details for all beaches using an isolated thread pool (supplyAsync)
		List<CompletableFuture<BeachResponse>> futures = beaches.stream()
				.map(beach -> CompletableFuture.supplyAsync(() -> {
					try {
						var country = countryClient.getCountryById(beach.getCountryId());
						return new BeachResponse(beach, country);
					} catch (Exception e) {
						// Graceful Degradation: If country-service is down or returns 404 Not Found,
						// we still return the Beach data, leaving the Country nested object as null.
						return new BeachResponse(beach, null);
					}
				}))
				.toList();

		// Wait for all remote calls to finish and combine into one list response
		return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
				.thenApply(v -> futures.stream()
						.map(CompletableFuture::join)
						.toList())
				.thenApply(ResponseEntity::ok);
	}

	// GET one beach - enriched with full Country data from country-service via
	// Feign. Returns a CompletableFuture to ensure the controller is non-blocking.
	@GetMapping("/{id}")
	public CompletableFuture<ResponseEntity<BeachResponse>> getOneBeach(@PathVariable Long id) {

		Optional<Beach> beach = repository.findById(id);

		if (beach.isEmpty()) {
			return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
		}

		// Call country-service asynchronously using a manual ForkJoin thread
		return CompletableFuture.supplyAsync(() -> {
			try {
				var country = countryClient.getCountryById(beach.get().getCountryId());
				return ResponseEntity.ok(new BeachResponse(beach.get(), country));
			} catch (Exception e) {
				// Graceful Degradation: return Beach with null country
				return ResponseEntity.ok(new BeachResponse(beach.get(), null));
			}
		});
	}

	// POST create a new beach record
	@PostMapping
	public ResponseEntity<Beach> createBeach(@RequestBody Beach beach) {
		Beach savedBeach = repository.save(beach);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedBeach.getBeachId()).toUri();

		return ResponseEntity.created(location).body(savedBeach);
	}

	// PUT update an existing beach record
	@PutMapping("/{id}")
	public ResponseEntity<Beach> editBeach(@PathVariable Long id, @RequestBody Beach beach) {

		if (!repository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}

		beach.setBeachId(id);
		Beach updatedBeach = repository.save(beach);

		return ResponseEntity.ok(updatedBeach);
	}

	// DELETE one beach record
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteBeach(@PathVariable Long id) {
		if (!repository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}

		repository.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
