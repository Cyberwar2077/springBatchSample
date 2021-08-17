package com.sample.springbatch.config;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.sample.springbatch.controller.BatchController;

@RunWith(SpringRunner.class)
//@EnableAutoConfiguration
//@ContextConfiguration(classes = { SpringBatchApplication.class })
//@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class })
//@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class SpringControllerTest {

//	@Autowired
//	BatchController controller;
//	
//	@Test
//	public void testBatchController() throws Exception {
//		String result=controller.importUserJob();
//		Assert.assertEquals("COMPLETED", result);
//	}

	MockMvc mockMvc;

	@Mock
	JobLauncher jobLauncher;

	@Mock
	Job job;

	@Mock
	Job getFileDetailsJob;

	@InjectMocks
	BatchController controller;
	
	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	JobExecution execution;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		mockMvc=MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	public void testImportUser() throws Exception {
		Mockito.when(jobLauncher.run(Mockito.any(Job.class),Mockito.any(JobParameters.class))).thenReturn(execution);
		Mockito.when(execution.getExitStatus().getExitCode()).thenReturn("Success");
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/importUserJob"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
		Assert.assertEquals("Success", result.getResponse().getContentAsString());
	}
}
