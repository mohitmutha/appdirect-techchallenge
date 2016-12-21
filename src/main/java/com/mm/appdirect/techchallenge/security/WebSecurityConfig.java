package com.mm.appdirect.techchallenge.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth.common.signature.SharedConsumerSecretImpl;
import org.springframework.security.oauth.provider.BaseConsumerDetails;
import org.springframework.security.oauth.provider.ConsumerDetailsService;
import org.springframework.security.oauth.provider.InMemoryConsumerDetailsService;
import org.springframework.security.oauth.provider.filter.OAuthProviderProcessingFilter;
import org.springframework.security.oauth.provider.filter.ProtectedResourceProcessingFilter;
import org.springframework.security.oauth.provider.token.InMemoryProviderTokenServices;
import org.springframework.security.oauth.provider.token.OAuthProviderTokenServices;
import org.springframework.security.openid.OpenIDAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Value("${appdirect.key}")
  private String consumerKey;
  @Value("${appdirect.secret}")
  private String secret;

  @Autowired
  private LogoutSuccessHandler logoutSuccessHandler;

  @Override
  protected void configure(final HttpSecurity http) throws Exception {
    http.headers().frameOptions().disable();
    http.csrf().disable();
    http.authorizeRequests().antMatchers("/appdirect/**", "/test/**").permitAll().anyRequest().authenticated();
    http.openidLogin().permitAll().authenticationUserDetailsService(new OAuthUserDetailsService())
        .defaultSuccessUrl("/accounts").attributeExchange("https://www.appdirect.com.*").attribute("email")
        .type("http://axschema.org/contact/email").required(true).and().attribute("firstname")
        .type("http://axschema.org/namePerson/first").required(true).and().attribute("lastname")
        .type("http://axschema.org/namePerson/last").required(true);
    http.logout().logoutSuccessHandler(logoutSuccessHandler);
    http.addFilterAfter(oAuthProviderProcessingFilter(), OpenIDAuthenticationFilter.class);
  }

  @Bean
  public OAuthProviderProcessingFilter oAuthProviderProcessingFilter() {
    System.out.println("Filter hit");
    List<RequestMatcher> requestMatchers = new ArrayList<>();
    requestMatchers.add(new AntPathRequestMatcher("/appdirect/**"));
    ProtectedResourceProcessingFilter filter = new OAuthProtectedResourceProcessingFilter(requestMatchers);

    filter.setConsumerDetailsService(consumerDetailsService());
    filter.setTokenServices(providerTokenServices());
    System.out.println("End Filter hit");
    return filter;
  }

  @Bean
  public ConsumerDetailsService consumerDetailsService() {
    System.out.println("Consumer details hit");
    InMemoryConsumerDetailsService consumerDetailsService = new InMemoryConsumerDetailsService();

    BaseConsumerDetails consumerDetails = new BaseConsumerDetails();
    consumerDetails.setConsumerKey(consumerKey);
    consumerDetails.setSignatureSecret(new SharedConsumerSecretImpl(secret));
    consumerDetails.setRequiredToObtainAuthenticatedToken(false);

    Map<String, BaseConsumerDetails> consumerDetailsStore = new HashMap<>();
    consumerDetailsStore.put(consumerKey, consumerDetails);

    consumerDetailsService.setConsumerDetailsStore(consumerDetailsStore);
    System.out.println("End Consumer details hit");
    return consumerDetailsService;
  }

  @Bean
  public OAuthProviderTokenServices providerTokenServices() {
    return new InMemoryProviderTokenServices();
  }

}
