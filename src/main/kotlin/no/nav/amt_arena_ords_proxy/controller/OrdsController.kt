package no.nav.amt_arena_ords_proxy.controller

import no.nav.amt_arena_ords_proxy.controller.dto.FnrDto
import no.nav.amt_arena_ords_proxy.controller.dto.VirksomhetsnummerDto
import no.nav.amt_arena_ords_proxy.service.ArenaOrdsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/ords")
class OrdsController(
	private val arenaOrdsService: ArenaOrdsService
) {

	@GetMapping("/fnr")
	fun hentFnrForPersonId(@RequestParam("personId") personId: String): FnrDto  {
		val fnr = arenaOrdsService.hentFnr(personId)
		return FnrDto(fnr)
	}

	@GetMapping("/virksomhetsnummer")
	fun hentVirksomhetsnummerForArbeidsgiverId(@RequestParam("arbeidsgiverId") arbeidsgiverId: String): VirksomhetsnummerDto  {
		val virksomhetsnummer = arenaOrdsService.hentVirksomhetsnummer(arbeidsgiverId)
		return VirksomhetsnummerDto(virksomhetsnummer)
	}

}
