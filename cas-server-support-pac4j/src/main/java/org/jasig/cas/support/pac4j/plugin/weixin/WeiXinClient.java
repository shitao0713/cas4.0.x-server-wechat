package org.jasig.cas.support.pac4j.plugin.weixin;

import org.pac4j.core.context.WebContext;
import org.pac4j.oauth.client.BaseOAuth20Client;
import org.pac4j.oauth.profile.JsonHelper;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;
import org.scribe.model.Token;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Iterator;

/**
 * Created by luotuo on 17-9-19.
 */
public class WeiXinClient extends BaseOAuth20Client<WeiXinProfile> {

    public WeiXinClient() {
    }

    public WeiXinClient(String key, String secret) {
        this.setKey(key);
        this.setSecret(secret);
    }

    protected WeiXinClient newClient() {
        WeiXinClient client = new WeiXinClient();
        return client;
    }

    protected void internalInit() {
        super.internalInit();
        //String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo#wechat_redirect";
        WeiXinApi20 api = new WeiXinApi20();
        this.service = new WeiXinOAuth20ServiceImpl(api, new OAuthConfig(this.key, this.secret, this.callbackUrl, SignatureType.Header, null, null),
                this.connectTimeout, this.readTimeout, this.proxyHost, this.proxyPort);    }

    protected String getProfileUrl(Token accessToken) {
        return "https://api.weixin.qq.com/sns/userinfo";
    }

    protected WeiXinProfile extractUserProfile(String body) {
        WeiXinProfile profile = new WeiXinProfile();
        JsonNode json = JsonHelper.getFirstNode(body);
        if(json != null) {
            Iterator var4 = WeiXinAttributesDefinition.weixinDefinition.getAllAttributes().iterator();
            while(var4.hasNext()) {
                String attribute = (String)var4.next();
                profile.addAttribute(attribute, JsonHelper.get(json, attribute));
            }
        }
        return profile;
    }

    protected boolean requiresStateParameter() {
        return false;
    }

    protected boolean hasBeenCancelled(WebContext context) {
        return false;
    }
}

