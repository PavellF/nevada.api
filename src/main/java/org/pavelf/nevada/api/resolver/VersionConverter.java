package org.pavelf.nevada.api.resolver;

import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.domain.VersionImpl;
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
