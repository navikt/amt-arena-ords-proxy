package no.nav.amt_arena_ords_proxy.controller

import no.nav.amt_arena_ords_proxy.controller.dto.ArbeidsgiverDto
import no.nav.amt_arena_ords_proxy.controller.dto.FnrDto
import no.nav.amt_arena_ords_proxy.service.ArenaOrdsService
import no.nav.amt_arena_ords_proxy.type.ArbeidsgiverId
import no.nav.amt_arena_ords_proxy.type.PersonId
import no.nav.amt_arena_ords_proxy.utils.OrgNrUtils.orgNrtoStr
import no.nav.security.token.support.core.api.Protected
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/ords")
class OrdsController(
	private val arenaOrdsService: ArenaOrdsService
) {

	@Protected
	@GetMapping("/fnr")
	fun hentFnrForPersonId(@RequestParam("personId") personId: PersonId): FnrDto  {
		val personIdWithFnr = arenaOrdsService.hentFnr(personId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

		return FnrDto(
			fnr = personIdWithFnr.fnr
		)
	}

	@Protected
	@GetMapping("/arbeidsgiver")
	fun hentArbeidsgiver(@RequestParam("arbeidsgiverId") arbeidsgiverId: ArbeidsgiverId): ArbeidsgiverDto  {
		val arbeidsgiver = arenaOrdsService.hentArbeidsgiver(arbeidsgiverId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

		return ArbeidsgiverDto(
			virksomhetsnummer = orgNrtoStr(arbeidsgiver.bedriftsnr),
			organisasjonsnummerMorselskap = orgNrtoStr(arbeidsgiver.orgnrMorselskap)
		)
	}

}
