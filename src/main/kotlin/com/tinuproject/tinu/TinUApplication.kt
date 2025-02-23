package com.tinuproject.tinu

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class TinUApplication

fun main(args: Array<String>) {
	runApplication<TinUApplication>(*args)
}
