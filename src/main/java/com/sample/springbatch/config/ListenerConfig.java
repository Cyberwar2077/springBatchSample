package com.sample.springbatch.config;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
 
public class ListenerConfig implements JobExecutionListener {
     
//    @Override
//    public void beforeChunk(ChunkContext context) {
//    }
// 
//    @Override
//    public void afterChunk(ChunkContext context) {
//         
//        int count = context.getStepContext().getStepExecution().getReadCount();
//        System.out.println("ItemCount: " + count);
//    }
//     
//    @Override
//    public void afterChunkError(ChunkContext context) {
//    }

	@Override
	public void beforeJob(JobExecution jobExecution) {
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		Date start = jobExecution.getCreateTime();
        Date end = jobExecution.getEndTime();
        long diff = end.getTime() - start.getTime();
	    System.out.println(TimeUnit.SECONDS.convert(diff, TimeUnit.MILLISECONDS));
	}
}
