package telran.java2022.service;

import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import telran.java2022.person.dao.PersonRepository;
import telran.java2022.person.dto.AddressDto;
import telran.java2022.person.dto.ChildDto;
import telran.java2022.person.dto.CityPopulationDto;
import telran.java2022.person.dto.EmployeeDto;
import telran.java2022.person.dto.PersonDto;
import telran.java2022.person.dto.PersonNotFoundException;
import telran.java2022.person.model.Address;
import telran.java2022.person.model.Child;
import telran.java2022.person.model.Employee;
import telran.java2022.person.model.Person;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService, CommandLineRunner {

	final PersonRepository personRepository;
	final ModelMapper modelMapper;
	
	@Override
	@Transactional
	public Boolean addPerson(PersonDto personDto) {
		if (personRepository.existsById(personDto.getId())) {
			return false;
		}
		personRepository.save(modelMapper.map(personDto, getModelClass(personDto)));
		return true;
	}

	private Class<? extends Person> getModelClass(PersonDto personDto) {
		if(personDto instanceof EmployeeDto) {
			return Employee.class;
		}
		if(personDto instanceof ChildDto) {
			return Child.class;
		} 
		return Person.class;
	}

	@Override
	public PersonDto findPersonById(Integer id) {
		Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
		return modelMapper.map(person, getDtoClass(person));
	}

	@SuppressWarnings("unchecked")
	private Class<? extends PersonDto> getDtoClass(Person person) {
		String[] str = person.getClass().getName().split("\\.");
		str[str.length-2] = "dto";
		String className = String.join(".", str) + "Dto";
		try {
			return (Class<? extends PersonDto>) Class.forName(className);
		} catch (ClassNotFoundException e) {
			return PersonDto.class;
		}
	}

	@Override
	@Transactional
	public PersonDto removePerson(Integer id) {
		Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
		personRepository.deleteById(id);
		return modelMapper.map(person, getDtoClass(person));
	}

	@Override
	@Transactional
	public PersonDto updatePersonName(Integer id, String name) {
		Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
		person.setName(name);
		return modelMapper.map(person, getDtoClass(person));
	}

	@Override
	@Transactional
	public PersonDto updatePersonAddress(Integer id, AddressDto addressDto) {
		Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
		person.setAddress(modelMapper.map(addressDto, Address.class));
		return modelMapper.map(person, getDtoClass(person));
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<PersonDto> findPersonsByCity(String city) {
		try (Stream<Person> personList = personRepository.findByAddressCity(city)) {
			return personList.map(p -> modelMapper.map(p, PersonDto.class))
							.collect(Collectors.toList());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<PersonDto> findPersonsByName(String name) {
		try (Stream<Person> personList = personRepository.findByName(name)) {
			return personList.map(p -> modelMapper.map(p, PersonDto.class))
							.collect(Collectors.toList());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<PersonDto> findPersonsBetweenAge(Integer minAge, Integer maxAge) {
		LocalDate fromDate = LocalDate.now().minusYears(maxAge);
		LocalDate toDate = LocalDate.now().minusYears(minAge);
		return personRepository.findByBirthDateBetween(fromDate, toDate)
								.map(p -> modelMapper.map(p, PersonDto.class))
								.collect(Collectors.toList());
	}

	@Override
	public Iterable<CityPopulationDto> getCitiesPopulation() {
//		Map<String, Long> population = StreamSupport.stream(personRepository.findAll().spliterator(), false)
//				.collect(Collectors.groupingBy(p->p.getAddress().getCity(), Collectors.counting()));
//		return population.entrySet().stream()
//					.map(e->new CityPopulationDto(e.getKey(), e.getValue()))
//					.collect(Collectors.toList());
		return personRepository.getCitiesPopulation();
	}

	@Override
	public void run(String... args) throws Exception {
		Person person = new Person(1000, "John", LocalDate.of(1985, 04, 11), new Address("Tel Aviv", "Ben Gvirol", 87));
		Child child = new Child(2000, "Mosche", LocalDate.of(2018, 07, 05), new Address("Ashkelon", "Bar Kohva", 21), "Shalom");
		Employee employee = new Employee(3000, "Mary", LocalDate.of(1995, 11, 23), new Address("Rehovot", "Herzl", 7), "Motorola", 20000);
		personRepository.save(person);
		personRepository.save(child);
		personRepository.save(employee);
	}

	@Override
	public Iterable<PersonDto> findEmployeeBySalary(int min, int max) {
		return personRepository.findEmployeeBySalary(min, max);
	}

	@Override
	public Iterable<PersonDto> getChildren() {
		return personRepository.getChildren();
	}

}
