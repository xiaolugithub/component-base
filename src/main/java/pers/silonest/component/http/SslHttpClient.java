package pers.silonest.component.http;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;

import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import pers.silonest.component.base.resource.ClassPathResource;

public class SslHttpClient extends AbstractHttpClient {

  private final String KEY_STORE_TYPE_P12 = "PKCS12";
  private final String KEY_STORE_TYPE_JKS = "JKS";

  public SslHttpClient(String clientPath, String clientPwd, String jksPath, String jksPwd) {
    try {
      // // 1、导入自己证书
      // KeyStore selfCert = KeyStore.getInstance(KEY_STORE_TYPE_P12);
      // selfCert.load(new ClassPathResource(clientPath).getInputStream(), clientPwd.toCharArray());
      // KeyManagerFactory kmf = KeyManagerFactory.getInstance("sunx509");
      // kmf.init(selfCert, clientPwd.toCharArray());
      //
      // // 2、导入服务器CA证书
      // KeyStore caCert = KeyStore.getInstance(KEY_STORE_TYPE_JKS);
      // caCert.load(new ClassPathResource(jksPath).getInputStream(), jksPwd.toCharArray());
      // TrustManagerFactory tmf = TrustManagerFactory.getInstance("sunx509");
      // tmf.init(caCert);
      //
      // SSLContext sc = SSLContext.getInstance("TLS");
      // sc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
      // SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore,
      // KEY_STORE_PASSWORD.toCharArray())
      // .loadTrustMaterial(trustStore, new TrustSelfSignedStrategy()).build();

      // SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sc);
      SSLContextBuilder builder = new SSLContextBuilder();
      // 全部信任 不做身份鉴定
      builder.loadTrustMaterial(null, new TrustStrategy() {
        @Override
        public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
          return true;
        }
      });
      SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build(), new String[] {"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2"},
          null, NoopHostnameVerifier.INSTANCE);
      this.httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
    } catch (Exception ex) {
      throw new IllegalArgumentException(ex);
    }
  }
}
