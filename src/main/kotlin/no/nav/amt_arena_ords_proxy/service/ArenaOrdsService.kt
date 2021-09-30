package no.nav.amt_arena_ords_proxy.service

import no.nav.amt_arena_ords_proxy.client.ords.ArenaOrdsClient
import org.springframework.stereotype.Service

@Service
class ArenaOrdsService(
	private val arenaOrdsClient: ArenaOrdsClient
) {

	fun hentFnr(personId: String): String {
		return arenaOrdsClient.hentFnr(personId)
	}

	fun hentVirksomhetsnummer(arbeidsgiverId: String): String {
		return arenaOrdsClient.hentVirksomhetsnummer(arbeidsgiverId)
	}

}
