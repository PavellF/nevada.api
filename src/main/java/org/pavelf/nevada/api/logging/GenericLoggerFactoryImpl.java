package org.pavelf.nevada.api.logging;

import org.springframework.stereotype.Component;

/**
 * Basic implementation for {@code LoggerFactory}.
 * @author Pavel F.
 * @since 1.0
 * */
@Component
public class GenericLoggerFactoryImpl implements LoggerFactory {

	@Override
	public Logger obtain(Class<?> forClass) {
		return new GenericLoggerImpl(forClass);
	}

	@Override
	public Logger obtain(String name) {
		return new GenericLoggerImpl(name);
	}

}
