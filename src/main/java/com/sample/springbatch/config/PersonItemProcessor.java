package com.sample.springbatch.config;

import org.springframework.batch.item.ItemProcessor;

import com.sample.springbatch.vo.Person;

public class PersonItemProcessor implements ItemProcessor<Person, Person> {

	@Override
	public Person process(final Person person) throws Exception {
		final String firstName = person.getFirstName().toUpperCase();
		final String lastName = person.getLastName().toUpperCase();
		return new Person(firstName, lastName);
	}

}
