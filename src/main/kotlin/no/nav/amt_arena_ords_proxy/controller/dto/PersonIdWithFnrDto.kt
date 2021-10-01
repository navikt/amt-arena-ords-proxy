package no.nav.amt_arena_ords_proxy.controller.dto

import no.nav.amt_arena_ords_proxy.type.Fnr
import no.nav.amt_arena_ords_proxy.type.PersonId

data class PersonIdWithFnrDto(
	val personId: PersonId,
	val fnr: Fnr,
)
