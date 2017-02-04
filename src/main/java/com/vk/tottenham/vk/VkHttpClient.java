package com.vk.tottenham.vk;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecFactory;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.impl.cookie.BasicExpiresHandler;
import org.apache.http.util.TextUtils;
import org.springframework.stereotype.Component;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.conn.PoolingClientConnectionManager;

@Component("vkHttpClient")
public class VkHttpClient extends DefaultHttpClient {
    private static final String[] DATE_PATTERNS = new String[]{"EEE, dd MMM yyyy HH:mm:ss z"};

    public VkHttpClient() {
        super(new PoolingClientConnectionManager());
        getCookieSpecs().register("lenient", new CookieSpecFactory() {
            public CookieSpec newInstance(HttpParams params) {
                return new LenientCookieSpec() {
                };
            }
        });
        HttpClientParams.setCookiePolicy(getParams(), "lenient");
    }
private static class LenientCookieSpec extends BrowserCompatSpec {
    public LenientCookieSpec() {
        super();
        registerAttribHandler(ClientCookie.EXPIRES_ATTR, new BasicExpiresHandler(DATE_PATTERNS) {
            @Override public void parse(SetCookie cookie, String value) throws MalformedCookieException {
                if (TextUtils.isEmpty(value)) {
                    // You should set whatever you want in cookie
                    cookie.setExpiryDate(null);
                } else {
                    super.parse(cookie, value);
                }
            }
        });
    }
}
}
