package no.nav.amt_arena_ords_proxy.client.ords

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@WireMockTest
class ArenaOrdsClientImplTest {

	@Test
	fun `skal hente fnr for personid`(wmRuntimeInfo: WireMockRuntimeInfo) {
		val client = ArenaOrdsClientImpl(
			object : ArenaOrdsTokenProvider {
				override fun getArenaOrdsToken(): String {
					return "TOKEN"
				}
			},
			wmRuntimeInfo.httpBaseUrl,
		)

		givenThat(
			post(urlEqualTo("/arena/api/v1/person/identListe"))
				.withHeader("Authorization", equalTo("Bearer TOKEN"))
				.withRequestBody(equalToJson(
					"""
						{
						 "personListe" : [
						 		{ "personId" : 12345 },
								{ "personId" : 6789 }
							]
						}
					""".trimIndent()
				))
				.willReturn(
					aResponse()
						.withStatus(200)
						.withBody(
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
		)

		val personIdWithFnrList = client.hentFnr(listOf(12345, 6789))

		assertEquals(12345, personIdWithFnrList[0].personId)
		assertEquals("098765", personIdWithFnrList[0].fnr)

		assertEquals(6789, personIdWithFnrList[1].personId)
		assertEquals("235876", personIdWithFnrList[1].fnr)
	}

	@Test
	fun `skal hente arbeidsgiver for arbeidsgiver id`(wmRuntimeInfo: WireMockRuntimeInfo) {
		val client = ArenaOrdsClientImpl(
			object : ArenaOrdsTokenProvider {
				override fun getArenaOrdsToken(): String {
					return "TOKEN"
				}
			},
			wmRuntimeInfo.httpBaseUrl,
		)

		givenThat(
			get(urlEqualTo("/arena/api/v1/arbeidsgiver/ident"))
				.withHeader("Authorization", equalTo("Bearer TOKEN"))
				.willReturn(
					aResponse()
						.withStatus(200)
						.withBody(
							"""
								{ "bedriftsnr": 123456, "orgnrMorselskap": 98765 }
							""".trimIndent()
						)
				)
		)

		val arbeidsgiver = client.hentArbeidsgiver(555555)

		assertNotNull(arbeidsgiver)
		assertEquals(123456, arbeidsgiver!!.bedriftsnr)
		assertEquals(98765, arbeidsgiver.orgnrMorselskap)
	}

	@Test
	fun `skal returnere null hvis ORDS svarer med status 204`(wmRuntimeInfo: WireMockRuntimeInfo) {
		val client = ArenaOrdsClientImpl(
			object : ArenaOrdsTokenProvider {
				override fun getArenaOrdsToken(): String {
					return "TOKEN"
				}
			},
			wmRuntimeInfo.httpBaseUrl,
		)

		givenThat(
			get(urlEqualTo("/arena/api/v1/arbeidsgiver/ident"))
				.withHeader("Authorization", equalTo("Bearer TOKEN"))
				.willReturn(
					aResponse()
						.withStatus(204)
				)
		)

		val arbeidsgiver = client.hentArbeidsgiver(555555)

		assertNull(arbeidsgiver)
	}


}
