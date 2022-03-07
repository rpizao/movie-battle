package com.rpizao.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "br.com.rpizao.repositories")
@EntityScan("br.com.rpizao.entities")
@ComponentScan({"br.com.rpizao.services", "br.com.rpizao.repositories", "br.com.rpizao.controllers"})
@PropertySource("application-test.properties")
@EnableAutoConfiguration
@EnableTransactionManagement
public class MovieBattleJpaControllerConfig { }