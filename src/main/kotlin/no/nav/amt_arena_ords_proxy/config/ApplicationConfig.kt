package no.nav.amt_arena_ords_proxy.config

import no.nav.amt_arena_ords_proxy.client.ords.Arbeidsgiver
import no.nav.amt_arena_ords_proxy.client.ords.ArenaOrdsClient
import no.nav.amt_arena_ords_proxy.client.ords.ArenaOrdsClientImpl
import no.nav.amt_arena_ords_proxy.client.ords.PersonIdWithFnr
import no.nav.amt_arena_ords_proxy.type.ArbeidsgiverId
import no.nav.amt_arena_ords_proxy.type.PersonId
import no.nav.security.token.support.spring.api.EnableJwtTokenValidation
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("!local")
@EnableJwtTokenValidation
@EnableConfigurationProperties(EnvironmentProperties::class)
class ApplicationConfig {

	@Bean
	fun arenaOrdsClient(environmentProperties: EnvironmentProperties): ArenaOrdsClient {
		return object: ArenaOrdsClient {
			override fun hentFnr(personIdListe: List<PersonId>): List<PersonIdWithFnr> {
				return emptyList()
			}

			override fun hentArbeidsgiver(arbeidsgiverId: ArbeidsgiverId): Arbeidsgiver {
				return Arbeidsgiver(
					virksomhetsnummer = "TODO",
					moderSelskapOrgNr = "TODO"
					)
			}
		}
	}

}
