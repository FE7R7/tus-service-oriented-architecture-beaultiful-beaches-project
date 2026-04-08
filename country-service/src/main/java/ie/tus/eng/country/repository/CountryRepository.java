package ie.tus.eng.country.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ie.tus.eng.country.model.Country;

public interface CountryRepository extends JpaRepository<Country, Long> {
}
