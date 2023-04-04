package no.nav.amt_arena_ords_proxy.client.ords

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ArenaOrdsTokenProviderImplTest {
	val server = MockWebServer()
	val serverUrl = server.url("").toString().removeSuffix("/")

	@Test
	fun `should make correct request when fetching ORDS token`() {
		val tokenProvider = ArenaOrdsTokenProviderImpl(
			"TEST",
			"TEST_SECRET",
			serverUrl
		)

		server.enqueue(
			MockResponse().setBody(
				"""
					{
						"access_token": "TOKEN",
						"expires_in": 100
					}
				""".trimIndent()
			)
		)

		assertEquals("TOKEN", tokenProvider.getArenaOrdsToken())
	}

	@Test
	fun `should cache ORDS token`() {
		val tokenProvider = ArenaOrdsTokenProviderImpl(
			"TEST",
			"TEST_SECRET",
			serverUrl
		)

		server.enqueue(
			MockResponse().setBody(
				"""
					{
						"access_token": "TOKEN",
						"expires_in": 100
					}
				""".trimIndent()
			)
		)

		tokenProvider.getArenaOrdsToken()
		tokenProvider.getArenaOrdsToken()

		assertEquals(1, server.requestCount)
	}

	@Test
	fun `should fetch new token if cached token is close to expired`() {
		val tokenProvider = ArenaOrdsTokenProviderImpl(
			"TEST",
			"TEST_SECRET",
			serverUrl
		)

		server.enqueue(
			MockResponse().setBody(
				"""
					{
						"access_token": "TOKEN",
						"expires_in": 1
					}
				""".trimIndent()
			)
		)
		server.enqueue(
			MockResponse().setBody(
				"""
					{
						"access_token": "TOKEN",
						"expires_in": 100
					}
				""".trimIndent()
			)
		)

		tokenProvider.getArenaOrdsToken()

		Thread.sleep(500)

		tokenProvider.getArenaOrdsToken()

		assertEquals(2, server.requestCount)
	}
}
