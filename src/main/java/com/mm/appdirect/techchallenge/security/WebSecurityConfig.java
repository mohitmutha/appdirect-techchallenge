package com.mm.appdirect.techchallenge.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    
	private static final String consumerKey = "appdirecttechchallenge1-145423";
	private static final String secret = "Bqtfobhh1fAYK6mV";
	
	@Override
    protected void configure(final HttpSecurity http) throws Exception {
		http.headers().frameOptions().disable();
        http
            .csrf().disable();
        http
            .authorizeRequests()
                .antMatchers("/h2-console/**","/**", "/accounts/**", "/appdirect/**", "/test/**").permitAll()
                .anyRequest().authenticated();
        http
            .openidLogin()
                .permitAll()
                .authenticationUserDetailsService(new CustomUserDetailsService())
                .attributeExchange("https://www.appdirect.com.*")
                    .attribute("email")
                        .type("http://axschema.org/contact/email")
                        .required(true)
                        .and()
                    .attribute("firstname")
                        .type("http://axschema.org/namePerson/first")
                        .required(true)
                        .and()
                    .attribute("lastname")
                        .type("http://axschema.org/namePerson/last")
                        .required(true);
//        http
//            .logout()
//                .logoutSuccessHandler(new CustomLogoutSuccessHandler());
        http
            .addFilterAfter(oAuthProviderProcessingFilter(), OpenIDAuthenticationFilter.class);
    }
	
	@Bean
	public OAuthProviderProcessingFilter oAuthProviderProcessingFilter(){
		System.out.println("Filter hit");
		List<RequestMatcher> requestMatchers = new ArrayList<>();
        requestMatchers.add(new AntPathRequestMatcher("/appdirect/**"));
        ProtectedResourceProcessingFilter filter = new CustomProtectedResourceProcessingFilter(requestMatchers);

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


