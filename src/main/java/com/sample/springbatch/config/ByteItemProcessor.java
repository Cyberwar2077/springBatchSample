package com.sample.springbatch.config;

import org.springframework.batch.item.ItemProcessor;

import com.sample.springbatch.vo.FileDetails;

public class ByteItemProcessor implements ItemProcessor<FileDetails, FileDetails> {

	@Override
	public FileDetails process(FileDetails data) throws Exception {
		return data;
	}

}
