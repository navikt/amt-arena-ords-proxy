package no.nav.amt_arena_ords_proxy.utils

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule

object JsonUtils {

	private val objectMapper = JsonMapper.builder()
		.addModule(KotlinModule())
		.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
		.build()

	fun getObjectMapper(): ObjectMapper {
		return objectMapper
	}

}
