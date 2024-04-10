package com.connected.car.user.configuration;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.connected.car.user.exceptions.custom.DatabaseConnectionException;

import lombok.Data;

import javax.sql.DataSource;
import java.sql.Connection;

@Configuration
@Data
public class DatabaseConfig {

	@Value("${spring.datasource.url}")
	private String dbUrl;

	@Value("${spring.datasource.username}")
	private String username;

	@Value("${spring.datasource.password}")
	private String password;

	@Bean
	DataSource dataSource() {

		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		dataSource.setUrl(dbUrl);
		dataSource.setUsername(username);
		dataSource.setPassword(password);

		try (Connection connection = dataSource.getConnection()) {
			return dataSource;
		} catch (Exception e) {
			throw new DatabaseConnectionException();
		}
	}
}
