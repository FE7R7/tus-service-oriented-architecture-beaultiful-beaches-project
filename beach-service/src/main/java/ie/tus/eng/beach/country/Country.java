package ie.tus.eng.beach.country;

// This class is not a JPA entity (No annotations).
// It is just a Data Transfer Object (DTO), not an object that the beach-service database knows or stores.
// No local Persistence, it only "lives" in memory during the execution of that specific request.
// It is used to receive and organize information (JSON) coming from another microservice (country-service).
public class Country {

	private Long countryId;
	private String name;
	private String continent;
	private String capital;

	public Country() {
	}

	public Country(Long countryId, String name, String continent, String capital) {
		this.countryId = countryId;
		this.name = name;
		this.continent = continent;
		this.capital = capital;
	}

	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContinent() {
		return continent;
	}

	public void setContinent(String continent) {
		this.continent = continent;
	}

	public String getCapital() {
		return capital;
	}

	public void setCapital(String capital) {
		this.capital = capital;
	}

	@Override
	public String toString() {
		return "Country{countryId=" + countryId + ", name='" + name + "', continent='" + continent + "', capital='"
				+ capital + "'}";
	}
}
