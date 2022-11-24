package telran.java2022.service;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java2022.person.dao.PersonRepository;
import telran.java2022.person.dto.AddressDto;
import telran.java2022.person.dto.CityPopulationDto;
import telran.java2022.person.dto.PersonDto;
import telran.java2022.person.dto.PersonNotFoundException;
import telran.java2022.person.model.Address;
import telran.java2022.person.model.Person;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

	final PersonRepository personRepository;
	final ModelMapper modelMapper;
	
	@Override
	public Boolean addPerson(PersonDto personDto) {
		if (personRepository.existsById(personDto.getId())) {
			return false;
		}
		personRepository.save(modelMapper.map(personDto, Person.class));
		return true;
	}

	@Override
	public PersonDto findPersonById(Integer id) {
		Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
		return modelMapper.map(person, PersonDto.class);
	}

	@Override
	public PersonDto removePerson(Integer id) {
		Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
		personRepository.deleteById(id);
		return modelMapper.map(person, PersonDto.class);
	}

	@Override
	public PersonDto updatePersonName(Integer id, String name) {
		Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
		person.setName(name);
		personRepository.save(person);
		return modelMapper.map(person, PersonDto.class);
	}

	@Override
	public PersonDto updatePersonAddress(Integer id, AddressDto addressDto) {
		Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
		person.setAddress(modelMapper.map(addressDto, Address.class));
		personRepository.save(person);
		return modelMapper.map(person, PersonDto.class);
	}

	@Override
	@Transactional
	public Iterable<PersonDto> findPersonsByCity(String city) {
		try (Stream<Person> personList = personRepository.findPersonsByAddressCity(city)) {
			return personList.map(p -> modelMapper.map(p, PersonDto.class))
							.collect(Collectors.toList());
		}
	}

	@Override
	@Transactional
	public Iterable<PersonDto> findPersonsByName(String name) {
		try (Stream<Person> personList = personRepository.findPersonsByName(name)) {
			return personList.map(p -> modelMapper.map(p, PersonDto.class))
							.collect(Collectors.toList());
		} 
	}

	@Override
	@Transactional
	public Iterable<PersonDto> findPersonsBetweenAge(Integer minAge, Integer maxAge) {
//FIXME
//		try (Stream<Person> personList = personRepository.findPersonsByStartDateBetween(minAge, maxAge)) {
//			return personList.map(p -> modelMapper.map(p, PersonDto.class))
//							.collect(Collectors.toList());
//		}
		return null;
	}

	@Override
	public Iterable<CityPopulationDto> getCitiesPopulation() {
// TODO 
		return null;
	}

}
