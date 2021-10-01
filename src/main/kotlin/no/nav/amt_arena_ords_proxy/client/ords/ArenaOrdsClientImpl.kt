package no.nav.amt_arena_ords_proxy.client.ords

import com.fasterxml.jackson.databind.ObjectMapper
import no.nav.amt_arena_ords_proxy.utils.JsonUtils.getObjectMapper
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.RuntimeException

class ArenaOrdsClientImpl(
	private val tokenProvider: ArenaOrdsTokenProvider,
	private val arenaOrdsUrl: String,
	private val httpClient: OkHttpClient = OkHttpClient(),
	private val objectMapper: ObjectMapper = getObjectMapper(),
) : ArenaOrdsClient {

	override fun hentFnr(personId: String): String {
		val request = Request.Builder()
			.url("$arenaOrdsUrl/some/path?personId=$personId")
			.addHeader("Authorization", "Bearer ${tokenProvider.getArenaOrdsToken()}")
			.get()
			.build()

		httpClient.newCall(request).execute().use { response ->
			if (!response.isSuccessful) {
				throw RuntimeException("Klarte ikke å hente fnr for personId. Status: ${response.code}")
			}

			val body = response.body?.string() ?: throw RuntimeException("Body is missing from ORDS token request")

			return objectMapper.readValue(body, HentFnrResponse::class.java).fnr
		}
	}

	override fun hentVirksomhetsnummer(arbeidsgiverId: String): String {
		val request = Request.Builder()
			.url("$arenaOrdsUrl/some/other/path?arbeidsgiverId=$arbeidsgiverId")
			.addHeader("Authorization", "Bearer ${tokenProvider.getArenaOrdsToken()}")
			.get()
			.build()

		httpClient.newCall(request).execute().use { response ->
			if (!response.isSuccessful) {
				throw RuntimeException("Klarte ikke å hente virksomhetsnummer for arbeidsgiverId. Status: ${response.code}")
			}

			val body = response.body?.string() ?: throw RuntimeException("Body is missing from ORDS token request")

			return objectMapper.readValue(body, HentVirksomhetsnummerResponse::class.java).virksomhetsnummer
		}
	}

	private data class HentFnrResponse(
		val fnr: String,
	)

	private data class HentVirksomhetsnummerResponse(
		val virksomhetsnummer: String,
	)

}
