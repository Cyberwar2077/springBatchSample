package com.sample.springbatch.config;

import org.apache.commons.io.IOUtils;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;

import com.sample.springbatch.vo.FileDetails;

public class CustomItemReader extends AbstractItemCountingItemStreamItemReader<FileDetails> implements
ResourceAwareItemReaderItemStream<FileDetails> {

	private Resource resource;
	private boolean done=false;
		
	public CustomItemReader() {
		setName(ClassUtils.getShortName(CustomItemReader.class));
	}

	@Override
	public void setResource(Resource resource) {
		this.resource = resource;
	}

	@Override
	protected FileDetails doRead() throws Exception {
		if (done) {
			return null;
		}
		else {
			FileDetails fileDetails=new FileDetails();
			fileDetails.setFileName(resource.getFilename());
			fileDetails.setData(IOUtils.toByteArray(resource.getInputStream()));
			done=true;
			return fileDetails;
		}
	}

	@Override
	protected void doOpen() throws Exception {
		//NOP
	}

	@Override
	protected void doClose() throws Exception {
		done=false;
	}
}
