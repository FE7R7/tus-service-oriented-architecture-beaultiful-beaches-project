package ie.tus.eng.beach.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "beach")
public class Beach {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long beachId;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String description;

	// Rating out of 10 (e.g. 9.7)
	@Column(nullable = false)
	private Double rating;

	@Column(nullable = false)
	private String city;

	// Foreign key referencing the id in country-service (cross-service reference)
	@Column(nullable = false)
	private Long countryId;

	public Beach() {
	}

	public Beach(Long beachId, String name, String description, Double rating, String city, Long countryId) {
		this.beachId = beachId;
		this.name = name;
		this.description = description;
		this.rating = rating;
		this.city = city;
		this.countryId = countryId;
	}

	public Long getBeachId() {
		return beachId;
	}

	public void setBeachId(Long beachId) {
		this.beachId = beachId;
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

	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	@Override
	public String toString() {
		return "Beach{beachId=" + beachId + ", name='" + name + "', rating=" + rating + ", city='" + city
				+ "', countryId=" + countryId + "}";
	}
}
