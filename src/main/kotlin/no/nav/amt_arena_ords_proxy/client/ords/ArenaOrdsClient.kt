package no.nav.amt_arena_ords_proxy.client.ords

interface ArenaOrdsClient {

	fun hentFnr(personId: String): String

	fun hentVirksomhetsnummer(arbeidsgiverId: String): String

}
