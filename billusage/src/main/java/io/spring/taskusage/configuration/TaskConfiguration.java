/*
 * Copyright 2019 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.spring.taskusage.configuration;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;


import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.weaving.LoadTimeWeaverAware;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.instrument.classloading.LoadTimeWeaver;

@Configuration
@EnableTask
public class TaskConfiguration {

	@Bean
	PathMatchingResourcePatternResolver resourcePatternResolver(ResourcePatternResolver resourcePatternResolver) {
		return new PathMatchingResourcePatternResolver(resourcePatternResolver);
	}

	@Bean
	CommandLineRunner commandLineRunner(BillUsageProcessor billUsageProcessor) {
		return args -> {
			billUsageProcessor.processResources();
		};
	}

	@Bean
	public BillUsageProcessor s3Processor(
			PathMatchingResourcePatternResolver resourcePatternResolver,
			DataSource dataSource) {
		return new BillUsageProcessor(resourcePatternResolver,dataSource);
	}

	/**
	 * Work-around for https://github.com/spring-projects/spring-boot/issues/13042
	 */
	@Configuration
	protected static class DataSourceInitializerInvokerConfiguration implements LoadTimeWeaverAware {

		@Autowired
		private ListableBeanFactory beanFactory;

		@PostConstruct
		public void init() {
			String cls = "org.springframework.boot.autoconfigure.jdbc.DataSourceInitializerInvoker";
			if (beanFactory.containsBean(cls)) {
				beanFactory.getBean(cls);
			}
		}

		@Override
		public void setLoadTimeWeaver(LoadTimeWeaver ltw) {
		}
	}

}
