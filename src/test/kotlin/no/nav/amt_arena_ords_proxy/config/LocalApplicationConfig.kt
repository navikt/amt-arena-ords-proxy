package no.nav.amt_arena_ords_proxy.config

import no.nav.amt_arena_ords_proxy.client.ords.ArenaOrdsClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class LocalApplicationConfig {

	@Bean
	fun arenaOrdsClient(): ArenaOrdsClient {
		return object: ArenaOrdsClient {
			override fun hentFnr(personId: String): String {
				return "TODO"
			}

			override fun hentVirksomhetsnummer(arbeidsgiverId: String): String {
				return "TODO"
			}
		}
	}

}
