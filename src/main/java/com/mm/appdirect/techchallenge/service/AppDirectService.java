package com.mm.appdirect.techchallenge.service;

import java.io.IOException;
import java.io.InputStream;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.HttpHeaders;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
@Component
public class AppDirectService {
	private static final String consumerKey = "appdirecttechchallenge1-145423";
	private static final String secret = "Bqtfobhh1fAYK6mV";

	private static OAuthConsumer consumer = new DefaultOAuthConsumer(consumerKey, secret);

	public <T> T get(String address, Class<T> t) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, ClientProtocolException, IOException {

		String signedUrl = consumer.sign(address);
		HttpGet getRequest = new HttpGet(signedUrl);
		getRequest.addHeader(HttpHeaders.ACCEPT,"application/json");
		HttpClient client = HttpClientBuilder.create().build();
		
		InputStream stream = client.execute(getRequest).getEntity().getContent();
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper.readValue(stream, t);
	}
}
