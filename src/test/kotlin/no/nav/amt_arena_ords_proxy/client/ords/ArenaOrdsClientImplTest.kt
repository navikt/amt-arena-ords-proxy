package no.nav.amt_arena_ords_proxy.client.ords

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ArenaOrdsClientImplTest {
	val server = MockWebServer()
	val serverUrl = server.url("").toString().removeSuffix("/")

	@Test
	fun `skal hente fnr for personid`() {
		val client = ArenaOrdsClientImpl(
			object : ArenaOrdsTokenProvider {
				override fun getArenaOrdsToken(): String {
					return "TOKEN"
				}
			},
			serverUrl
		)

		server.enqueue(
			MockResponse().setBody(
				"""
					{
						"personListe": [
							{ "personId": 12345, "fnr": "098765" },
							{ "personId": 6789, "fnr": "235876" }
						]
					}
				""".trimIndent()
			)
		)

		val personIdWithFnrList = client.hentFnr(listOf(12345, 6789))

		assertEquals(12345, personIdWithFnrList[0].personId)
		assertEquals("098765", personIdWithFnrList[0].fnr)

		assertEquals(6789, personIdWithFnrList[1].personId)
		assertEquals("235876", personIdWithFnrList[1].fnr)
	}

	@Test
	fun `skal hente arbeidsgiver for arbeidsgiver id`() {
		val client = ArenaOrdsClientImpl(
			object : ArenaOrdsTokenProvider {
				override fun getArenaOrdsToken(): String {
					return "TOKEN"
				}
			},
			serverUrl
		)

		server.enqueue(
			MockResponse().setBody(
				"""
					{ "bedriftsnr": 123456, "orgnrMorselskap": 98765 }
				""".trimIndent()
			)
		)

		val arbeidsgiver = client.hentArbeidsgiver(555555)

		assertNotNull(arbeidsgiver)
		assertEquals(123456, arbeidsgiver!!.bedriftsnr)
		assertEquals(98765, arbeidsgiver.orgnrMorselskap)
	}

	@Test
	fun `skal returnere null hvis ORDS svarer med status 204`() {
		val client = ArenaOrdsClientImpl(
			object : ArenaOrdsTokenProvider {
				override fun getArenaOrdsToken(): String {
					return "TOKEN"
				}
			},
			serverUrl
		)

		server.enqueue(MockResponse().setResponseCode(204))

		val arbeidsgiver = client.hentArbeidsgiver(555555)

		assertNull(arbeidsgiver)
	}
}
