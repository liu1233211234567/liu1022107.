package com.fh.conf;

import org.springframework.context.annotation.Configuration;

import java.io.InputStream;

@Configuration
public class MyConfig extends WXPayConfig {

    private byte[] certData;

    @Override
    public String getAppID() {
        return "wxa1e44e130a9a8eee";
    }

    @Override
    public String getMchID() {
        return "1507758211";
    }

    @Override
    public String getKey() {
        return "feihujiaoyu12345678yuxiaoyang123";
    }

    public int getHttpConnectTimeoutMs(){
        return 8000;
    }
    public int getHttpReadTimeoutMs(){
        return 9000;
    }

    @Override
    InputStream getCertStream() {
        return null;
    }

    @Override
    IWXPayDomain getWXPayDomain() {
        return new IWXPayDomain() {
            @Override
            public void report(String domain, long elapsedTimeMillis, Exception ex) {

            }

            @Override
            public DomainInfo getDomain(WXPayConfig config) {
                return new DomainInfo(WXPayConstants.DOMAIN_API,true);
            }
        };
    }
}
