package com.sample.springbatch.config;

import java.io.IOException;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.sample.springbatch.vo.FileDetails;
import com.sample.springbatch.vo.Person;

@Configuration
public class BatchConfiguration {

	@Bean
	@StepScope
	public FlatFileItemReader<Person> reader() {
		BeanWrapperFieldSetMapper<Person> mapper = new BeanWrapperFieldSetMapper<>();
		mapper.setTargetType(Person.class);
		return new FlatFileItemReaderBuilder<Person>().name("personItemReader")
				.resource(new ClassPathResource("sample-data.csv")).delimited().names("firstName", "lastName")
				.fieldSetMapper(mapper).build();
	}

//	@Bean
//	public FlatFileItemReader<FileDetails> multireader(MultiResourceItemReader<FileDetails> delegator) {
//		FlatFileItemReader<FileDetails> reader = new FlatFileItemReader<>();
////		DefaultRecordSeparatorPolicy
//		reader.setRecordSeparatorPolicy(new RecordSeparatorPolicy() {
//			@Override
//			public String preProcess(String record) {
//				return record+"\r\n";
//			}
//			
//			@Override
//			public String postProcess(String record) {
//				return record;
//			}
//			
//			@Override
//			public boolean isEndOfRecord(String record) {
//				return false;
//			}
//		});
//		reader.setLineMapper(new LineMapper<FileDetails>() {
//			@Override
//			public FileDetails mapLine(String line, int lineNumber) throws Exception {
//				String fileNmae=delegator.getCurrentResource().getFilename();
//				FileDetails fileDetails=new FileDetails();
//				fileDetails.setFileName(fileNmae);
//				fileDetails.setData(line.getBytes());
//				return fileDetails;
//			}
//		});
//		return reader;
//	}

	@Bean
	@StepScope
	public MultiResourceItemReader<FileDetails> multiResourceItemReader() throws IOException {
		MultiResourceItemReader<FileDetails> resourceItemReader = new MultiResourceItemReader<>();
		Resource[] resources = new PathMatchingResourcePatternResolver().getResources("file:D:/spring batch test/sample*.csv");
		resourceItemReader.setResources(resources);
		resourceItemReader.setDelegate(test());
		return resourceItemReader;
	}

	private CustomItemReader test() {
		return new CustomItemReader();
}

	@Bean
	@StepScope
	public PersonItemProcessor processor() {
		return new PersonItemProcessor();
	}

	@Bean
	@StepScope
	public ByteItemProcessor byteItemProcessor() {
		return new ByteItemProcessor();
	}
	
	@Bean
	@StepScope
	public JpaItemWriter<FileDetails> fileWriter(EntityManagerFactory emf) {
		JpaItemWriter<FileDetails> writer = new JpaItemWriter<>();
		writer.setEntityManagerFactory(emf);
		return writer;
	}

	@Bean
	@StepScope
	public JpaItemWriter<Person> writer(EntityManagerFactory emf) {
		JpaItemWriter<Person> writer = new JpaItemWriter<>();
		writer.setEntityManagerFactory(emf);
		return writer;
	}
	
	@Bean(name = "importUserJob")
	public Job importUserJob(JobBuilderFactory jobBuilderFactory,@Qualifier("step")Step step1) {
		return jobBuilderFactory.get("importUserJob").listener(jobListener()).incrementer(new RunIdIncrementer()).flow(step1).end().build();
	}

	@Bean(name = "step")
	public Step step(StepBuilderFactory stepBuilderFactory, JpaItemWriter<Person> writer){
		return stepBuilderFactory.get("step").<Person, Person>chunk(10).reader(reader())
				.processor(processor()).writer(writer).build();
	}
	
	@Bean(name = "getFileDetails")
	public Job getFileDetails(JobBuilderFactory jobBuilderFactory,@Qualifier("byteStep")Step byteStep) {
		return jobBuilderFactory.get("getFileDetails").incrementer(new RunIdIncrementer()).flow(byteStep).end().build();
	}
	
	@Bean(name = "byteStep")
	public Step byteStep(StepBuilderFactory stepBuilderFactory, JpaItemWriter<FileDetails> fileWriter) throws IOException {
		return stepBuilderFactory.get("byteStep").<FileDetails, FileDetails>chunk(3).reader(multiResourceItemReader())
				.processor(byteItemProcessor()).writer(fileWriter).build();
	}
	
	@Bean
	public ListenerConfig jobListener(){
		return new ListenerConfig();
	}
}
