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

package io.spring.taskusage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import io.spring.taskusage.configuration.Usage;
import javax.sql.DataSource;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
public class BillusageApplicationTests {

	@Autowired
	private DataSource dataSource;

	@Test
	public void testRepository() {
			List<Usage> usages = getResultsFromDB(dataSource);
			assertThat(usages.size()).isEqualTo(5);
			assertThat(usages.get(0).getId()).isEqualTo(1);
			assertThat(usages.get(0).getFirstName()).isEqualTo("jane");
	}

	public List<Usage> getResultsFromDB(DataSource dataSource) {
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
			return jdbcTemplate.query("select ID, FIRST_NAME, LAST_NAME, MINUTES, DATA_USAGE FROM BILL_USAGE", new UsageRowMapper());
	}

	private final class UsageRowMapper implements RowMapper<Usage> {
		@Override
		public Usage mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new Usage(rs.getLong("id"),
					rs.getString("FIRST_NAME"), rs.getString("LAST_NAME"),
					rs.getLong("MINUTES"), rs.getLong("DATA_USAGE"));
		}

	}
}
