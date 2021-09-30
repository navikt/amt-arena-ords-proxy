package no.nav.amt_arena_ords_proxy.config

import no.nav.amt_arena_ords_proxy.client.ords.ArenaOrdsClient
import no.nav.amt_arena_ords_proxy.client.ords.ArenaOrdsClientImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ApplicationConfig {

	@Bean
	fun arenaOrdsClient(): ArenaOrdsClient {
		return ArenaOrdsClientImpl()
	}

}
