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

/**
 * 
 * Class used to parse between types. (String, Map, UUID).
 * 
 * @author Ramón Moñino Rubio
 *
 */
public class JsonParser {

	/**
	 * 
	 * Parses a map to json string.
	 * 
	 * @param map to be parsed.
	 * @return string in json format.
	 */
	public String getMapAsJsonFormat(Map<String, Set<String>> map) {
		ObjectMapper mapper = new ObjectMapper();
		String json = null;

		try {
			json = mapper.writeValueAsString(map);
		} catch (JsonProcessingException e) {
		}
		return json;
	}

	/**
	 * 
	 * Parses an array of errors as json string.
	 * 
	 * @param strings Array of errors
	 * @return string in json format that contains the errors.
	 */
	public String getErrorsAsJSONFormatString(String... strings) {
		return this.getStringAsJSONFormat("errors", strings);
	}

	/**
	 * 
	 * Creates a string in json format given its key and values.
	 * 
	 * @param key     key of json attribute.
	 * @param strings values of the json key.
	 * @return a string in json format with the given key and values.
	 */
	public String getStringAsJSONFormat(String key, String... strings) {
		HashMap<String, Set<String>> map = new HashMap<>();
		Set<String> set = new HashSet<>();
		Collections.addAll(set, strings);
		map.put(key, set);
		return this.getMapAsJsonFormat(map);
	}

	/**
	 * 
	 * Parses string to UUID.
	 * 
	 * @param id id as string to be parsed.
	 * @return UUID parsed from the given string.
	 * @throws InvalidUUIDFormatException If the string cannot be parsed to UUID
	 *                                    format.
	 */
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
