package org.pavelf.nevada.api.resolver;

import static org.pavelf.nevada.api.exception.ExceptionCases.UNKNOWN_VERSION;

import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.domain.VersionImpl;
import org.pavelf.nevada.api.exception.WebApplicationException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.InvalidMediaTypeException;

/**
 * Converter for {@link Version}.
 * @author Pavel F.
 * @since 1.0
 * */
public class VersionConverter implements Converter<String, Version> {

	@Override
	public Version convert(String source) {
		
		Version version = null;
		
		try {
			//try to derive version from header:
			version = VersionImpl.valueOf(source);
			
		} catch (InvalidMediaTypeException imte) {
			//trying to derive from plain string value
			version = VersionImpl.getBy(source);
			
		}
		
		if (version == null) {
			throw new WebApplicationException(UNKNOWN_VERSION);
		}
		
		return version;
	}

	
}
