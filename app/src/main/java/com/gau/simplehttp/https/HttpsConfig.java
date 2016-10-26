package com.gau.simplehttp.https;

import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

public class HttpsConfig {

    public HttpsConfig(String password, String keyStorePath, String trustStorePath, String defaultHostName) {
        initHttpsUrlConnection(password, keyStorePath, trustStorePath, defaultHostName);
    }

    /**
     * @param password        密码
     * @param keyStorePath    密钥库路径
     * @param trustStorePath  信任库地址
     * @param defaultHostName 默认认证主机名
     */
    private void initHttpsUrlConnection(String password, String keyStorePath, String trustStorePath, String defaultHostName) {
        SSLContext sslContext = null;
        try {
            sslContext = getSSLContext(password, keyStorePath, trustStorePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (sslContext != null) {
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        }
        HttpsURLConnection.setDefaultHostnameVerifier(new DefaultHostnameVerifier(defaultHostName));
    }

    /**
     * 获取密钥库
     *
     * @param password     密码
     * @param keyStorePath 密钥库路径
     * @return 密钥库
     * @throws Exception
     */
    private KeyStore getKeyStore(String password, String keyStorePath) throws Exception {
        // 初始化密钥库
        KeyStore keyStore = KeyStore.getInstance("JKS");
        // 获取密钥库文件流
        FileInputStream fis = new FileInputStream(keyStorePath);
        // 加载密钥库
        keyStore.load(fis, password.toCharArray());
        // 关闭文件流
        fis.close();
        return keyStore;
    }

    /**
     * @param password       密码
     * @param keyStorePath   密钥库路径
     * @param trustStorePath 信任库地址
     * @return SSLContext
     * @throws Exception
     */
    private SSLContext getSSLContext(String password, String keyStorePath, String trustStorePath) throws Exception {
        // 实例化密钥库
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        // 获取密钥
        KeyStore keyStore = getKeyStore(password, keyStorePath);
        // 初始化密钥
        keyManagerFactory.init(keyStore, password.toCharArray());
        // 实例化信任库
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        // 获取信任库
        KeyStore trustStore = getKeyStore(password, trustStorePath);
        // 初始化信任库
        trustManagerFactory.init(trustStore);
        // 实例化SSLContext
        SSLContext sslContext = SSLContext.getInstance("TLS");
        // 初始化SSLContext
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
        return sslContext;
    }

    private class DefaultHostnameVerifier implements HostnameVerifier {

        private String defaultHostName;

        DefaultHostnameVerifier(String defaultHostName) {
            this.defaultHostName = defaultHostName;
        }

        @Override
        public boolean verify(String hostname, SSLSession session) {
            return defaultHostName == null || defaultHostName.isEmpty() || hostname.equals(defaultHostName);
        }
    }

}
