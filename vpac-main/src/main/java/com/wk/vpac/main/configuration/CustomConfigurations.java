
package com.wk.vpac.main.configuration;

import com.base.components.cache.CacheManager;
import com.base.components.common.dto.auth.AuthorizationPropertiesMap;
import com.base.components.common.interceptors.TokenIdempotentInterceptor;
import com.base.components.common.interceptors.TokenObjInterceptor;
import com.base.components.common.service.validation.ApiMapValidateRequestMappingJoinPointHandler;
import com.base.components.common.token.TokenManager;
import com.base.components.common.tools.ServletTempPathChecker;
import com.wk.vpac.common.token.TokenType;
import com.wk.vpac.main.constants.admin.BaseAdminProperties;
import com.wk.vpac.main.interceptors.AdminAuthInterceptor;
import com.wk.vpac.main.interceptors.AdminOnlyLoginInterceptor;
import com.wk.vpac.main.interceptors.ContextPathInterceptor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;

/**
 * 启动相关配置
 *
 * @author <a href="drakelee1221@gmail.com">LiGeng</a>
 * @version 1.0.0, 2017-07-10
 */
@Configuration
@EnableConfigurationProperties(BaseAdminProperties.class)
@Import(ApiMapValidateRequestMappingJoinPointHandler.class)
public class CustomConfigurations {
  @Bean
  @RefreshScope
  public TokenObjInterceptor tokenObjInterceptor(TokenManager tokenManager,
                                                 AuthorizationPropertiesMap authorizationPropertiesMap){
    return new TokenObjInterceptor(tokenManager, authorizationPropertiesMap, Collections.singleton(TokenType.class));
  }

  @Bean
  public WebMvcConfigurer crossConfiguration(TokenObjInterceptor tokenObjInterceptor,
                                             AdminAuthInterceptor adminAuthInterceptor,
                                             AdminOnlyLoginInterceptor adminOnlyLoginInterceptor,
                                             CacheManager cacheManager) {
    return new WebMvcConfigurer() {
      /**
       * token对象对象拦截
       */
      @Override
      public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ContextPathInterceptor()).addPathPatterns("/**");

        //获取上游服务传递过来的token对象
        registry.addInterceptor(tokenObjInterceptor).addPathPatterns("/**");
//        registry.addInterceptor(apiCircleCheckInterceptor).addPathPatterns("/api/**");
        //根据登录token在控制幂等性
        registry.addInterceptor(TokenIdempotentInterceptor.createByTokenObjIdStrategy(
          cacheManager, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE))
                .addPathPatterns("/**")
                .excludePathPatterns("/admin/attachment/**");

        //登录后就能直接访问的地址
        registry.addInterceptor(adminOnlyLoginInterceptor).
          addPathPatterns(adminOnlyLoginInterceptor.getOnlyLoginAuthPath());
        //登录后权限验证
        registry.addInterceptor(adminAuthInterceptor).addPathPatterns("/admin/**")
                .excludePathPatterns(adminAuthInterceptor.getClearSkipAuthUri());
      }
    };
  }

  @Bean
  public ServletTempPathChecker servletTempPathChecker(ApplicationContext applicationContext){
    return new ServletTempPathChecker(applicationContext);
  }

}
