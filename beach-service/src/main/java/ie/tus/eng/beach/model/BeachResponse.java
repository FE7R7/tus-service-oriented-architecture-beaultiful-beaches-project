package ie.tus.eng.beach.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import ie.tus.eng.beach.country.Country;

// Controls JSON field ordering in the HTTP response body
@JsonPropertyOrder({ "id", "name", "description", "rating", "city", "country" })
public class BeachResponse {

	private Long id;
	private String name;
	private String description;
	private Double rating;
	private String city;

	// Embedded full Country object fetched from country-service via Feign
	private Country country;

	public BeachResponse() {
		super();
	}

	// Combination Constructor: combines JPA Beach entity with Feign fetched Country
	public BeachResponse(Beach beach, Country country) {
		this.id = beach.getBeachId();
		this.name = beach.getName();
		this.description = beach.getDescription();
		this.rating = beach.getRating();
		this.city = beach.getCity();
		this.country = country;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	@Override
	public String toString() {
		return "BeachResponse id=" + id + ", name=" + name + ", rating=" + rating + ", city=" + city + ", country="
				+ country;
	}
}
