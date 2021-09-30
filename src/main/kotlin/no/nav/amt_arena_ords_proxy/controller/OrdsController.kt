package no.nav.amt_arena_ords_proxy.controller

import no.nav.amt_arena_ords_proxy.controller.dto.FnrDto
import no.nav.amt_arena_ords_proxy.controller.dto.VirksomhetsnummerDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/ords")
class OrdsController {

	@GetMapping("/fnr")
	fun hentFnrForPersonId(@RequestParam("personId") personId: String): FnrDto  {
		return FnrDto("TODO")
	}

	@GetMapping("/virksomhetsnummer")
	fun hentVirksomhetsnummerForArbeidsgiverId(@RequestParam("arbeidsgiverId") arbeidsgiverId: String): VirksomhetsnummerDto  {
		return VirksomhetsnummerDto("TODO")
	}

}
