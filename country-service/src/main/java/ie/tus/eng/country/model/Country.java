package ie.tus.eng.country.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity @Table(name = "country")
public class Country {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long countryId;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String continent;

	@Column(nullable = false)
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
		return "Country{countryId=" + countryId + ", name='" + name + "', continent='" + continent + "', capital='" + capital + "'}";
	}
}
