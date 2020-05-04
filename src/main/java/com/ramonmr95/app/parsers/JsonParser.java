package com.ramonmr95.app.parsers;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ramonmr95.app.exceptions.InvalidUUIDFormatException;

public class JsonParser {

	public String getMapAsJsonFormat(Map<String, Set<String>> map) {
		ObjectMapper mapper = new ObjectMapper();
		String json = null;

		try {
			json = mapper.writeValueAsString(map);
		} catch (JsonProcessingException e) {
		}
		return json;
	}

	public String getErrorsAsJSONFormatString(String... strings) {
		return this.getStringAsJSONFormat("errors", strings);
	}

	public String getStringAsJSONFormat(String key, String... strings) {
		HashMap<String, Set<String>> map = new HashMap<>();
		Set<String> set = new HashSet<>();
		Collections.addAll(set, strings);
		map.put(key, set);
		return this.getMapAsJsonFormat(map);
	}

	public UUID parseUUIDFromString(String id) throws InvalidUUIDFormatException {
		UUID uuid = null;
		try {
			uuid = UUID.fromString(id);
		} catch (IllegalArgumentException e) {
			throw new InvalidUUIDFormatException(this.getErrorsAsJSONFormatString(e.getMessage()));
		}
		return uuid;

	}
}
