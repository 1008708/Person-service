package telran.java2022.person.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import telran.java2022.person.dto.CityPopulationDto;
import telran.java2022.person.dto.PersonDto;
import telran.java2022.person.model.Person;

public interface PersonRepository extends CrudRepository<Person, Integer> {

// HQL	
//	@Query("select p from Person p where p.address.city=:city") 
	Stream<Person> findByAddressCity(@Param("city") String city);
	
//	@Query("select p from Person p where p.name=?1")
	Stream<Person> findByName(String name);
	
	Stream<Person> findByBirthDateBetween(LocalDate from, LocalDate to);
	
	@Query("select new telran.java2022.person.dto.CityPopulationDto(p.address.city, count(p)) from Person p group by p.address.city order by count(p) desc")
	List<CityPopulationDto> getCitiesPopulation();

	@Query("select new telran.java2022.person.dto.EmployeeDto(p.id, p.name, p.birthDate, p.company,p.salary) from Person p where p.salary between ?1 and ?2")
	Iterable<PersonDto> findEmployeeBySalary(int min, int max);

	@Query("select new telran.java2022.person.dto.ChildDto(p.id, p.name, p.birthDate, p.kindergarten) from Person p where p.kindergarten is not null")
	Iterable<PersonDto> getChildren();
}
