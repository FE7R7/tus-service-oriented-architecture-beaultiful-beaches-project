package ie.tus.eng.beach.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

@Configuration
public class ETagConfig {

	// ShallowEtagHeaderFilter computes an MD5 hash of the response body and
	// attaches it as an ETag header. On subsequent requests that include
	// If-None-Match with the same hash value, Spring short-circuits and returns
	// HTTP 304 Not Modified with no body. This filter is applied only to the
	// beaches collection endpoint.
	@Bean
	public FilterRegistrationBean<ShallowEtagHeaderFilter> shallowEtagHeaderFilter() {
		FilterRegistrationBean<ShallowEtagHeaderFilter> filterRegistrationBean = new FilterRegistrationBean<>(new ShallowEtagHeaderFilter());
		filterRegistrationBean.addUrlPatterns("/beaches");
		return filterRegistrationBean;
	}
}
