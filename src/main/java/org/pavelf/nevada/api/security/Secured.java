package org.pavelf.nevada.api.security;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * General purpose annotation for narrowing method only to subset of requests that have 
 * appropriate token. For example:
* <p>{@code @SecurityScope(scope = Customer, access=Read/Write)}  </p>
* tells only tokens with scope "Customer" that have access level "Read/Write" allowed to 
 * execute this method.
 * @since 1.0
 * @author Pavel F.
 */
@Documented
@Retention(RUNTIME)
@Target(METHOD)
public @interface Secured {

	/**
	 * Enumerates all application specific scope that allowed to execute this method. 
	 * Application may want to have distinct class with constant Strings to describe scopes.
	 * */
	String[] scope();
	
	/**
	 * Application specific access level that describe set of actions.
	 * Greater number describes less restrictions.
	 * Application may want to have distinct class with constant integers to describe access levels.
	 * */
	int access();
	
}
