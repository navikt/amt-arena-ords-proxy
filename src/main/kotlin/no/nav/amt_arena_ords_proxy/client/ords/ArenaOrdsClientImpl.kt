package no.nav.amt_arena_ords_proxy.client.ords

import com.fasterxml.jackson.databind.ObjectMapper
import no.nav.amt_arena_ords_proxy.type.ArbeidsgiverId
import no.nav.amt_arena_ords_proxy.type.Fnr
import no.nav.amt_arena_ords_proxy.type.PersonId
import no.nav.amt_arena_ords_proxy.utils.JsonUtils
import no.nav.common.rest.client.RestClient.baseClient
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class ArenaOrdsClientImpl(
	private val tokenProvider: ArenaOrdsTokenProvider,
	private val arenaOrdsUrl: String,
	private val httpClient: OkHttpClient = baseClient(),
	private val objectMapper: ObjectMapper = JsonUtils.objectMapper
) : ArenaOrdsClient {

	private val mediaTypeJson = "application/json".toMediaType()

	override fun hentFnr(personIdListe: List<PersonId>): List<PersonIdWithFnr> {
		val requestBody = objectMapper.writeValueAsString(toHentFnrRequest(personIdListe))

		val request = Request.Builder()
			.url("$arenaOrdsUrl/arena/api/v1/person/identListe")
			.addHeader("Authorization", "Bearer ${tokenProvider.getArenaOrdsToken()}")
			.post(requestBody.toRequestBody(mediaTypeJson))
			.build()

		httpClient.newCall(request).execute().use { response ->
			if (!response.isSuccessful) {
				throw RuntimeException("Klarte ikke å hente fnr for personId. Status: ${response.code}")
			}

			val body = response.body?.string() ?: throw RuntimeException("Body is missing from ORDS token request")

			return objectMapper.readValue(body, HentFnrResponse::class.java).toPersonIdWithFnrList()
		}
	}

	override fun hentArbeidsgiver(arbeidsgiverId: ArbeidsgiverId): Arbeidsgiver? {
		val request = Request.Builder()
			.url("$arenaOrdsUrl/arena/api/v1/arbeidsgiver/ident")
			.addHeader("Authorization", "Bearer ${tokenProvider.getArenaOrdsToken()}")
			.addHeader("arbgivId", arbeidsgiverId.toString())
			.get()
			.build()

		httpClient.newCall(request).execute().use { response ->
			if (!response.isSuccessful) {
				throw RuntimeException("Klarte ikke å hente virksomhetsnummer for arbeidsgiverId. Status: ${response.code}")
			}

			if (response.code == 204) {
				return null
			}

			val body = response.body?.string() ?: throw RuntimeException("Body is missing from ORDS token request")

			return objectMapper.readValue(body, HentVirksomhetsnummerResponse::class.java).toArbeidsgiver()
		}
	}

	private data class PersonIdRequestItem(
		val personId: PersonId,
	)

	private data class HentFnrRequest(
		val personListe: List<PersonIdRequestItem>
	)

	private data class PersonIdResponseItem(
		val personId: PersonId,
		val fnr: Fnr
	)

	private data class HentFnrResponse(
		val personListe: List<PersonIdResponseItem>,
	)

	private fun HentFnrResponse.toPersonIdWithFnrList(): List<PersonIdWithFnr> {
		return this.personListe.map {
			PersonIdWithFnr(
				personId = it.personId,
				fnr = it.fnr
			)
		}
	}


	private data class HentVirksomhetsnummerResponse(
		val bedriftsnr: Int,
		val orgnrMorselskap: Int,
	)

	private fun HentVirksomhetsnummerResponse.toArbeidsgiver(): Arbeidsgiver {
		return Arbeidsgiver(
			bedriftsnr = this.bedriftsnr,
			orgnrMorselskap = this.orgnrMorselskap
		)
	}

	private fun toHentFnrRequest(personIdListe: List<PersonId>): HentFnrRequest {
		return HentFnrRequest(
			personListe = personIdListe.map { PersonIdRequestItem(it) }
		)
	}

}
