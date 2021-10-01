package no.nav.amt_arena_ords_proxy.client.ords

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


@WireMockTest
class ArenaOrdsTokenProviderImplTest {

	@Test
	fun `should make correct request when fetching ORDS token`(wmRuntimeInfo: WireMockRuntimeInfo) {
		val tokenProvider = ArenaOrdsTokenProviderImpl(
			"TEST",
			"TEST_SECRET",
			wmRuntimeInfo.httpBaseUrl
		)

		givenThat(
			post(urlEqualTo("/arena/api/oauth/token"))
				.withBasicAuth("TEST", "TEST_SECRET")
				.withRequestBody(equalTo("grant_type=client_credentials"))
				.willReturn(aResponse()
					.withStatus(200)
					.withBody(
						"""
							{
								"access_token": "TOKEN",
								"expires_in": 100
							}
						""".trimIndent()
					)
				)
		)

		assertEquals("TOKEN", tokenProvider.getArenaOrdsToken())
	}

	@Test
	fun `should cache ORDS token`(wmRuntimeInfo: WireMockRuntimeInfo) {
		val tokenProvider = ArenaOrdsTokenProviderImpl(
			"TEST",
			"TEST_SECRET",
			wmRuntimeInfo.httpBaseUrl
		)

		givenThat(
			post(urlEqualTo("/arena/api/oauth/token"))
				.willReturn(aResponse()
					.withStatus(200)
					.withBody(
						"""
							{
								"access_token": "TOKEN",
								"expires_in": 100
							}
						""".trimIndent()
					)
				)
		)

		tokenProvider.getArenaOrdsToken()
		tokenProvider.getArenaOrdsToken()

		verify(exactly(1), postRequestedFor(urlEqualTo("/arena/api/oauth/token")))
	}

	@Test
	fun `should fetch new token if cached token is close to expired`(wmRuntimeInfo: WireMockRuntimeInfo) {
		val tokenProvider = ArenaOrdsTokenProviderImpl(
			"TEST",
			"TEST_SECRET",
			wmRuntimeInfo.httpBaseUrl
		)

		givenThat(
			post(urlEqualTo("/arena/api/oauth/token"))
				.willReturn(aResponse()
					.withStatus(200)
					.withBody(
						"""
							{
								"access_token": "TOKEN",
								"expires_in": 31
							}
						""".trimIndent()
					)
				)
		)

		tokenProvider.getArenaOrdsToken()

		Thread.sleep(2000)

		tokenProvider.getArenaOrdsToken()

		verify(exactly(2), postRequestedFor(urlEqualTo("/arena/api/oauth/token")))
	}

}
