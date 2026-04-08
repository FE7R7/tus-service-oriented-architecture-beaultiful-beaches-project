package ie.tus.eng.beach.country;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// Feign generates a proxy at runtime that makes an HTTP GET call to country-service.
// The URL is read from application.properties (overridden by Docker env var COUNTRY_SERVICE_URL).
@FeignClient(name = "country-service", url = "${country.service.url}")
public interface CountryClient {

	@GetMapping("/countries/{country_id}")
	Country getCountryById(@PathVariable("country_id") long country_id);

}
