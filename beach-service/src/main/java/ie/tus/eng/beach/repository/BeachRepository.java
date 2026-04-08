package ie.tus.eng.beach.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ie.tus.eng.beach.model.Beach;

public interface BeachRepository extends JpaRepository<Beach, Long> {
}
