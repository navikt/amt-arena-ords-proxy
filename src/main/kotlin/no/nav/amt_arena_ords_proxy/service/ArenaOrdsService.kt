package no.nav.amt_arena_ords_proxy.service

import no.nav.amt_arena_ords_proxy.client.ords.Arbeidsgiver
import no.nav.amt_arena_ords_proxy.client.ords.ArenaOrdsClient
import no.nav.amt_arena_ords_proxy.client.ords.PersonIdWithFnr
import no.nav.amt_arena_ords_proxy.type.ArbeidsgiverId
import no.nav.amt_arena_ords_proxy.type.PersonId
import org.springframework.stereotype.Service

@Service
class ArenaOrdsService(
	private val arenaOrdsClient: ArenaOrdsClient
) {

	fun hentFnr(personId: PersonId): PersonIdWithFnr? {
		val personIdWithFnrList = arenaOrdsClient.hentFnr(listOf(personId))

		if (personIdWithFnrList.isEmpty()) {
			return null
		}

		return personIdWithFnrList[0]
	}

	fun hentArbeidsgiver(arbeidsgiverId: ArbeidsgiverId): Arbeidsgiver? {
		return arenaOrdsClient.hentArbeidsgiver(arbeidsgiverId)
	}

}
