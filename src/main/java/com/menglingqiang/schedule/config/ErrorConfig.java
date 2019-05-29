package com.menglingqiang.schedule.config;

import org.springframework.context.annotation.Configuration;

/*
 * 不用配置错误页面springboot会根据错误自己找error下的页面
 */
@Configuration
public class ErrorConfig {

//	@Bean
//	public EmbeddedServletContainerCustomizer containerCustomizer()
//	{
//		return new EmbeddedServletContainerCustomizer() {
//			
//			@Override
//			public void customize(ConfigurableEmbeddedServletContainer container) {
////				container.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500"));
////				container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/402"));
////				container.addErrorPages(new ErrorPage(HttpStatus.METHOD_NOT_ALLOWED, "/405"));
//				
//			}
//		};
//	}
}
