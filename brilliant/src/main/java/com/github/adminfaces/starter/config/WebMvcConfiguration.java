/**
 * 
 */
package com.github.adminfaces.starter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import net.brilliant.common.CommonUtility;

/**
 * @author ducbq
 *
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver localeResolver = new SessionLocaleResolver();
		localeResolver.setDefaultLocale(CommonUtility.LOCALE_VIETNAMESE); // change this
		return localeResolver;
	}

}
