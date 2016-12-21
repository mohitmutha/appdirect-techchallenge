package com.mm.appdirect.techchallenge.service;

import java.io.IOException;
import java.io.InputStream;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.HttpHeaders;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mm.appdirect.techchallenge.exception.ApplicationException;

@Component
public class AppDirectService extends BaseService{
  @Value("${appdirect.key}")
  private String consumerKey;
  @Value("${appdirect.secret}")
  private String secret;

  private Logger logger = LoggerFactory.getLogger(AppDirectService.class);
  private OAuthConsumer oauthConsumer = null;

  public <T> T get(String address, Class<T> t) throws ApplicationException {
    try {
      if(oauthConsumer == null){
        oauthConsumer = new DefaultOAuthConsumer(consumerKey, secret);
      }
      String signedUrl = oauthConsumer.sign(address);
      logger.debug("Start request to apdirect with signedUrl");
      HttpGet getRequest = new HttpGet(signedUrl);
      getRequest.addHeader(HttpHeaders.ACCEPT, "application/json");
      HttpClient client = HttpClientBuilder.create().build();

      InputStream stream = client.execute(getRequest).getEntity().getContent();
      logger.debug("Receive response from appdirect");
      ObjectMapper mapper = new ObjectMapper();
      //Fail on unknown properties set to false as model not worked out completely
      mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      return mapper.readValue(stream, t);
    } catch (OAuthMessageSignerException | OAuthExpectationFailedException | OAuthCommunicationException
        | UnsupportedOperationException | IOException e) {
      logger.error("Exception while getting event", e);

      ApplicationException exception = new ApplicationException();
      exception.addSuppressed(e);
      throw exception;
    }
  }
}
