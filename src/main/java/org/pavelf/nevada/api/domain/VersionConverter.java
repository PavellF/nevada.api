package org.pavelf.nevada.api.domain;

import org.springframework.core.convert.converter.Converter;

/**
 * Converter for {@code Version}.
 * @author Pavel F.
 * @since 1.0
 * */
public class VersionConverter implements Converter<String, Version> {

	@Override
	public Version convert(String source) {
		return VersionImpl.valueOf(source);
	}

	
}
