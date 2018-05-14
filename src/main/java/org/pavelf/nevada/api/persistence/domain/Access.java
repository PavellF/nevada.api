package org.pavelf.nevada.api.persistence.domain;

import java.beans.Visibility;

import com.fasterxml.jackson.databind.ser.std.EnumSerializer;

/**
 * Defines application specific access restrictions.
 * @since 1.0
 * @author Pavel F.
 * */
public enum Access {

	NONE,
	READ,
	READ_WRITE;
	
}
