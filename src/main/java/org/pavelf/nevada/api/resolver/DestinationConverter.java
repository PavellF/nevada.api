package org.pavelf.nevada.api.resolver;

import org.pavelf.nevada.api.domain.Destination;
import org.springframework.core.convert.converter.Converter;

/**
 * Converter for {@link Destination}.
 * @author Pavel F.
 * @since 1.0
 * */
public class DestinationConverter implements Converter<String, Destination> {

	@Override
	public Destination convert(String source) {
		return Destination.valueOf(source.toUpperCase());
	}

}
