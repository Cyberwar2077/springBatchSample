package com.sample.springbatch.controller;

import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BatchController {

	@Autowired
    JobLauncher jobLauncher;

    @Autowired
    @Qualifier("importUserJob")
    Job job;
    
    @Autowired
    @Qualifier("getFileDetails")
    Job getFileDetailsJob;
	
	@GetMapping(path = "/importUserJob")
	public String importUserJob() throws Exception{
		JobParametersBuilder jobParameterBuilder = new JobParametersBuilder();
		jobParameterBuilder.addDate("startTime", new Date());
		JobParameters jobParamters =jobParameterBuilder.toJobParameters();
		JobExecution jobExecution = jobLauncher.run(job, jobParamters);
		return jobExecution.getExitStatus().getExitCode();
	}
	
	@GetMapping(path = "/getFileDetails")
	public String getFileDetailsJob() throws Exception{
		JobParametersBuilder jobParameterBuilder = new JobParametersBuilder();
		jobParameterBuilder.addDate("startTime", new Date());
		JobParameters jobParamters =jobParameterBuilder.toJobParameters();
		JobExecution jobExecution = jobLauncher.run(getFileDetailsJob, jobParamters);
		return jobExecution.getExitStatus().getExitCode();
	}
}
