package no.nav.amt_arena_ords_proxy.config

import no.nav.amt_arena_ords_proxy.client.ords.ArenaOrdsClient
import no.nav.amt_arena_ords_proxy.client.ords.ArenaOrdsClientImpl
import no.nav.amt_arena_ords_proxy.client.ords.ArenaOrdsTokenProvider
import no.nav.amt_arena_ords_proxy.client.ords.ArenaOrdsTokenProviderImpl
import no.nav.common.log.LogFilter
import no.nav.common.utils.EnvironmentUtils
import no.nav.security.token.support.spring.api.EnableJwtTokenValidation
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("!local")
@EnableJwtTokenValidation
@EnableConfigurationProperties(EnvironmentProperties::class)
class ApplicationConfig {

	@Bean
	fun arenaOrdsTokenProvider(environmentProperties: EnvironmentProperties): ArenaOrdsTokenProvider {
		return ArenaOrdsTokenProviderImpl(
			clientId = environmentProperties.arenaOrdsClientId,
			clientSecret = environmentProperties.arenaOrdsClientSecret,
			arenaOrdsUrl = environmentProperties.arenaOrdsUrl
		)
	}

	@Bean
	fun arenaOrdsClient(environmentProperties: EnvironmentProperties, arenaOrdsTokenProvider: ArenaOrdsTokenProvider): ArenaOrdsClient {
		return ArenaOrdsClientImpl(
			tokenProvider = arenaOrdsTokenProvider,
			arenaOrdsUrl = environmentProperties.arenaOrdsUrl
		)
	}

	@Bean
	fun logFilterRegistrationBean(): FilterRegistrationBean<LogFilter> {
		val registration = FilterRegistrationBean<LogFilter>()
		registration.filter = LogFilter(
			EnvironmentUtils.requireApplicationName(), EnvironmentUtils.isDevelopment().orElse(false)
		)
		registration.order = 1
		registration.addUrlPatterns("/*")
		return registration
	}

}
