package com.sample.springbatch.config;

import java.util.Collection;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import com.sample.springbatch.SpringBatchApplication;

@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@ContextConfiguration(classes = { SpringBatchApplication.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class SpringBatchIntegrationTest {

	@Autowired
	@Qualifier(value = "importUserJob")
	private Job job;

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private JobRepository jobRepository;

	private JobLauncherTestUtils jobLauncherTestUtils;

	@Before
	public void setUp() throws Exception {
		this.jobLauncherTestUtils = new JobLauncherTestUtils();
		this.jobLauncherTestUtils.setJobLauncher(jobLauncher);
		this.jobLauncherTestUtils.setJobRepository(jobRepository);
	}

	private JobParameters defaultJobParameters() {
		JobParametersBuilder paramsBuilder = new JobParametersBuilder();
		paramsBuilder.addDate("startTime", new Date());
		return paramsBuilder.toJobParameters();
	}

	@Test
	public void givenReferenceOutput_whenJobExecuted_thenSuccess() throws Exception {
		this.jobLauncherTestUtils.setJob(job);
		JobExecution jobExecution = jobLauncherTestUtils.launchJob(defaultJobParameters());
		ExitStatus actualJobExitStatus = jobExecution.getExitStatus();
		Assert.assertEquals("COMPLETED", actualJobExitStatus.getExitCode());
		Collection<StepExecution> actualStepExecutions = jobExecution.getStepExecutions();
		actualStepExecutions.forEach(stepExecution -> {
			Assert.assertEquals( 5,stepExecution.getWriteCount());
	    });
	}

}
