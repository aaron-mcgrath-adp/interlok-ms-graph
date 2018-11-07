package com.adaptris.msgraph.auth;

import java.util.Arrays;

import org.hibernate.validator.constraints.NotBlank;

import com.adaptris.annotation.AdapterComponent;
import com.adaptris.annotation.AffectsMetadata;
import com.adaptris.annotation.AutoPopulated;
import com.adaptris.annotation.ComponentProfile;
import com.adaptris.annotation.InputFieldDefault;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.ConfiguredProduceDestination;
import com.adaptris.core.CoreException;
import com.adaptris.core.ProduceException;
import com.adaptris.core.ServiceException;
import com.adaptris.core.ServiceImp;
import com.adaptris.core.http.ConfiguredContentTypeProvider;
import com.adaptris.core.http.client.ConfiguredRequestMethodProvider;
import com.adaptris.core.http.client.RequestMethodProvider.RequestMethod;
import com.adaptris.core.http.client.net.ConfiguredRequestHeaders;
import com.adaptris.core.http.client.net.StandardHttpProducer;
import com.adaptris.core.util.LifecycleHelper;
import com.adaptris.util.KeyValuePair;
import com.adaptris.util.KeyValuePairSet;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ms-graph-raw-request-service")
@AdapterComponent
@ComponentProfile(summary = "Service that uses http to send REST style requests.", tag = "service,http,rest")
public class MSGraphRawRequestService extends ServiceImp {
  
  private static final long DEFAULT_TIMEOUT = 5000l;
  
  private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
  
  private static final String ACCEPT_HEADER_NAME = "Accept";
  
  private static final String ACCEPT_HEADER_VALUE = "application/json";

  private static final String DEFAULT_TOKEN_KEY = "Authorization";
  
  private static final String DEFAULT_CONTENT_TYPE = "application/json";
  
  private static final boolean DEFAULT_SEND_PAYLOAD = false;
  
  private static final RequestMethod DEFAULT_REQUEST_METHOD = RequestMethod.GET; 
  
  @NotBlank
  private String apiUrl;
  
  @AutoPopulated
  @AffectsMetadata
  @InputFieldDefault(value = "Authorization")
  private String tokenKey;
  
  @AutoPopulated
  @InputFieldDefault(value = "false")
  private Boolean sendPayload;
  
  @AutoPopulated
  @InputFieldDefault(value = "GET")
  private RequestMethod requestMethod;
  
  private transient StandardHttpProducer httpProducer;
  
  public MSGraphRawRequestService() {
    httpProducer = new StandardHttpProducer();
  }
  
  @Override
  public void doService(AdaptrisMessage msg) throws ServiceException {
    this.configureHttpProducerAuthAndDest(msg);
    
    try {
      this.getHttpProducer().request(msg, DEFAULT_TIMEOUT);
    } catch (ProduceException e) {
      throw new ServiceException(e);
    }
  }

  private void configureHttpProducerAuthAndDest(AdaptrisMessage msg) {
    this.getHttpProducer().setDestination(new ConfiguredProduceDestination(msg.resolve(this.getApiUrl())));
    this.getHttpProducer().setContentTypeProvider(new ConfiguredContentTypeProvider(DEFAULT_CONTENT_TYPE));
    this.getHttpProducer().setAlwaysSendPayload(this.sendPayload());
    this.getHttpProducer().setMethodProvider(new ConfiguredRequestMethodProvider(this.requestMethod()));
    
    KeyValuePairSet headers = new KeyValuePairSet(Arrays.asList(
        new KeyValuePair(AUTHORIZATION_HEADER_NAME, msg.getMetadataValue(this.tokenKey())), 
        new KeyValuePair(ACCEPT_HEADER_NAME, ACCEPT_HEADER_VALUE)
        ));
    ConfiguredRequestHeaders configuredRequestHeaders = new ConfiguredRequestHeaders();
    configuredRequestHeaders.setHeaders(headers);
    
    this.getHttpProducer().setRequestHeaderProvider(configuredRequestHeaders);
  }
  
  @Override
  public void prepare() throws CoreException {
    LifecycleHelper.prepare(this.getHttpProducer());
  }

  @Override
  protected void initService() throws CoreException {
    LifecycleHelper.init(this.getHttpProducer());
  }

  @Override
  protected void closeService() {
    LifecycleHelper.close(this.getHttpProducer());
  }

  @Override
  public void stop() {
    LifecycleHelper.stop(this.getHttpProducer());
  }

  @Override
  public void start() throws CoreException {
    LifecycleHelper.start(this.getHttpProducer());
  }

  public String getApiUrl() {
    return apiUrl;
  }

  public void setApiUrl(String apiUrl) {
    this.apiUrl = apiUrl;
  }

  public StandardHttpProducer getHttpProducer() {
    return httpProducer;
  }

  public void setHttpProducer(StandardHttpProducer httpProducer) {
    this.httpProducer = httpProducer;
  }
  
  protected String tokenKey() {
    return this.getTokenKey() != null ? this.getTokenKey() : DEFAULT_TOKEN_KEY;
  }

  public String getTokenKey() {
    return tokenKey;
  }

  public void setTokenKey(String tokenKey) {
    this.tokenKey = tokenKey;
  }

  private boolean sendPayload() {
    return this.getSendPayload() == null ? DEFAULT_SEND_PAYLOAD : this.getSendPayload();
  }
  
  public Boolean getSendPayload() {
    return sendPayload;
  }

  public void setSendPayload(Boolean sendPayload) {
    this.sendPayload = sendPayload;
  }

  private RequestMethod requestMethod() {
    return this.getRequestMethod() == null ? DEFAULT_REQUEST_METHOD : this.getRequestMethod();
  }
  
  public RequestMethod getRequestMethod() {
    return requestMethod;
  }

  public void setRequestMethod(RequestMethod requestMethod) {
    this.requestMethod = requestMethod;
  }

}
