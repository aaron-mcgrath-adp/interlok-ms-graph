package com.adaptris.msgraph;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.ConfiguredProduceDestination;
import com.adaptris.core.CoreException;
import com.adaptris.core.DefaultMessageFactory;
import com.adaptris.core.http.ConfiguredContentTypeProvider;
import com.adaptris.core.http.client.ConfiguredRequestMethodProvider;
import com.adaptris.core.http.client.RequestMethodProvider.RequestMethod;
import com.adaptris.core.http.client.net.StandardHttpProducer;
import com.adaptris.core.util.LifecycleHelper;

public class Test {

  public static void main(String[] args) throws UnsupportedEncodingException {
//    IGraphServiceClient graphClient = 
//        GraphServiceClient
//          .builder()
//          .authenticationProvider(new HttpAuthenticationProvider())
//          .buildClient();
    
    StandardHttpProducer httpProducer = new StandardHttpProducer();
    httpProducer.setDestination(new ConfiguredProduceDestination("https://login.microsoftonline.com/common/oauth2/v2.0/token"));
    
    StringBuilder builder = new StringBuilder();
    builder.append("client_id=");
    builder.append("b9499a9f-3383-46f2-a891-db66a40b870a");
    builder.append("&scope=");
    builder.append(URLEncoder.encode("https://graph.microsoft.com/.default", "UTF-8"));
    builder.append("&client_secret=");
    builder.append("dkaeSHY163^:kloCAVR54@%");
    builder.append("&grant_type=");
    builder.append("client_credentials");
    
    AdaptrisMessage newMessage = DefaultMessageFactory.getDefaultInstance().newMessage(builder.toString());
    
    httpProducer.setContentTypeProvider(new ConfiguredContentTypeProvider("application/x-www-form-urlencoded"));
    
    httpProducer.setAlwaysSendPayload(true);
    httpProducer.setMethodProvider(new ConfiguredRequestMethodProvider(RequestMethod.POST));
    
    try {
      LifecycleHelper.initAndStart(httpProducer);
      
      httpProducer.request(newMessage, 5000l);
      
      System.out.println(newMessage.getContent());
      
      LifecycleHelper.stopAndClose(httpProducer);
      
    } catch (CoreException e) {
      e.printStackTrace();
    }
  }
  
}
