package org.pavelf.nevada.api.resolver;

import static org.pavelf.nevada.api.exception.ExceptionCases.*;

import org.pavelf.nevada.api.domain.Version;
import org.pavelf.nevada.api.domain.VersionImpl;
import org.pavelf.nevada.api.exception.WebApplicationException;
import org.pavelf.nevada.api.persistence.domain.Sorting;
import org.pavelf.nevada.api.service.PageAndSort;
import org.pavelf.nevada.api.service.impl.PageAndSortImpl;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Implementation to parse {@code PageAndSort} 
 * objects passed in controller methods.
 * @author Pavel F.
 * @since 1.0
 * */
public class PageAndSortResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(PageAndSort.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) throws Exception {
		try {
			
			String startParam = webRequest.getParameter("start");
			Integer startValue = (startParam == null) 
					? 0 : Integer.valueOf(startParam);
			
			String countParam = webRequest.getParameter("count");
			Integer countValue = (countParam == null) ? 
					15 : Integer.valueOf(countParam);
			
			if (countValue > 75) {
				new WebApplicationException(INVALID_REQUEST_PARAM);
			}
			
			String orderParam = webRequest.getParameter("order");
			Sorting order = (orderParam == null) 
					? Sorting.TIME_ASC : Sorting.valueOf(orderParam);
			
			String acceptHeader = webRequest.getHeader(HttpHeaders.ACCEPT);
			Version versionOfObject = VersionImpl.valueOf(acceptHeader);
			
			return PageAndSortImpl.valueOf(
					startValue, countValue, order, versionOfObject);
		} catch (IllegalArgumentException iae) {
			WebApplicationException wae = 
					new WebApplicationException(INVALID_REQUEST_PARAM);
			wae.initCause(iae);
			throw wae;
		}
	}

}
