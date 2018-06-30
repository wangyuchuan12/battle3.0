package com.wyc.common.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;
import com.wyc.ApplicationContextProvider;
import com.wyc.common.domain.vo.ApplicationProperties;
import com.wyc.common.util.MyLongDateFormat;
import com.wyc.common.util.MySimpleDateFormat;
import com.wyc.common.wx.domain.WxContext;



@Configuration
/*@ComponentScan(basePackages = "com", excludeFilters = {
        @Filter(type = FilterType.ASSIGNABLE_TYPE, value = WebConfig2.class),
        @Filter(type = FilterType.ASSIGNABLE_TYPE, value = DatabaseConfig.class),
    //    @Filter(type = FilterType.ASSIGNABLE_TYPE, value = SessionConfig.class),
        @Filter(type=FilterType.ASSIGNABLE_TYPE,value=GameWebConfig.class)
})*/
@EnableScheduling
public class AppConfig{
	 
	    final static Logger logger = LoggerFactory.getLogger(AppConfig.class);
	    @Bean
	    public ApplicationContextProvider applicationContextProvider() {
	        return new ApplicationContextProvider();
	    }
	    
	    @Bean
	    public ScheduledExecutorService executorService(){
	    	
	    	ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);
	    	return executorService;
	    }
	    
	    @Bean
	    public SockIOPool sockIoPool(){
	        String[] servers =
	        {
	              "127.0.0.1:8888"
	        };
	        System.out.println(".....5");
	        SockIOPool sockIOPool = SockIOPool.getInstance();
	        sockIOPool.setServers(servers);
	        sockIOPool.setNagle(false);
	        sockIOPool.setSocketTO(3000);
	        sockIOPool.setSocketConnectTO(0);
	        sockIOPool.initialize();
	        return sockIOPool;
	        
	        
	    }
	   
	    @Bean
	    public MemCachedClient memcachedClient(){
	        return new MemCachedClient();
	    }
	    
	   
	    
	    @Bean
	    public HttpClient httpGet(ApplicationProperties applicationProperties,WxContext wxc){
	    	

	        FileInputStream instream = null;
	        try {
	            KeyStore keyStore = KeyStore.getInstance("PKCS12");
	            instream =  new FileInputStream(new File(applicationProperties.getProperty("apiclient_cert_path")));
	            keyStore.load(instream,wxc.getMchId().toCharArray());
	            SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, wxc.getMchId().toCharArray()).build();
	            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(

	                    sslcontext,new String[] { "TLSv1" },null,

	                    SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER); //设置httpclient的SSLSocketFactory

	                    CloseableHttpClient httpclient = HttpClients.custom()

	                    .setSSLSocketFactory(sslsf)

	                    .build();
	                    return httpclient;
	        } catch (Exception e) {
	            logger.error("there is has an error:{}",e);
	            try {
	                instream.close();
	            } catch (IOException e1) {
	                // TODO Auto-generated catch block
	                e1.printStackTrace();
	            }
	        }
	        return null;
	    }
	    
	    @Bean
	    public ApplicationProperties applicationProperties() {
	        
	        ApplicationProperties properties = new ApplicationProperties();
	        File databaseConfigFile = new File(
	                "/etc/battle/application.properties");
	        
	        File redisConfigFile = new File(
	                "/etc/battle/redis.properties");
	        
	        
	        try {
	            if (databaseConfigFile.exists()) {
	                properties.load(new FileInputStream(databaseConfigFile));
	            } else {
	                InputStream defaultConfig = this.getClass()
	                        .getResourceAsStream("/application.properties");
	                properties.load(defaultConfig);
	            }
	        } catch (IOException e) {
	            logger.error("Load application.properties error: {}", e);
	        }
	        
	        try {
	            if (redisConfigFile.exists()) {
	                properties.load(new FileInputStream(redisConfigFile));
	            } else {
	                InputStream defaultConfig = this.getClass()
	                        .getResourceAsStream("/redis.properties");
	                properties.load(defaultConfig);
	            }
	        } catch (IOException e) {
	            logger.error("Load application.properties error: {}", e);
	        }
	        return properties;
	    }
	    
	    @Bean
	    @Autowired
	    public WxContext wxContext(ApplicationProperties myProperties){
	        WxContext wxContext = new WxContext();
	        wxContext.setAppid(myProperties.getProperty("appid"));
	        wxContext.setAppsecret(myProperties.getProperty("appsecret"));
	        wxContext.setFilePath(myProperties.getProperty("file_path"));
	        wxContext.setFlag(myProperties.getProperty("flag"));
	        wxContext.setDomainName(myProperties.getProperty("domain_name"));
	        wxContext.setKey(myProperties.getProperty("key"));
	        wxContext.setMchId(myProperties.getProperty("mch_id"));
	        wxContext.setTransferFee(Integer.parseInt(myProperties.getProperty("transfer_fee")));
	        wxContext.setInstantArrival(Integer.parseInt(myProperties.getProperty("instant_arrival")));
	        wxContext.setArrivalNum(Integer.parseInt(myProperties.getProperty("arrival_num")));
	        wxContext.setShareNumShowAnswer(Integer.parseInt(myProperties.getProperty("share_num_show_answer")));
	        wxContext.setSubmailAppid(myProperties.getProperty("submail_appid"));
	        wxContext.setSubmailSignature(myProperties.getProperty("submail_signature"));
	        wxContext.setApplyExpertProjectCode(myProperties.getProperty("apply_expert_project_code"));
	        
	        wxContext.setQnAccessKey(myProperties.getProperty("qn_accessKey"));
	        wxContext.setQnScretKey(myProperties.getProperty("qn_scretKey"));
	        wxContext.setQnBucketname(myProperties.getProperty("qn_bucketname"));
	        wxContext.setQnDomain(myProperties.getProperty("qn_domain"));
//	        wxContext = wxContextService.getWxContextBean();
	        
	        return wxContext;
	    }
	    
	    @Bean
	    public MyLongDateFormat myLongDateFormat(){
	        MyLongDateFormat sdf = new MyLongDateFormat("yyyy-MM-dd HH:mm:ss");
	        return sdf;
	    }
	    
	    @Bean
	    public MySimpleDateFormat mySimpleDateFormat(){
	    	MySimpleDateFormat sdf = new MySimpleDateFormat("MM-dd HH:mm");
	    	return sdf;
	    }
}
