package com.example;

import android.content.Context;

/**
 * Created with IntelliJ IDEA.
 * User: lagvare
 * Date: 24.5.2012
 * Time: 12:29
 * To change this template use File | Settings | File Templates.
 */
public class OAuthHandling {


    public boolean createOAuth2Code(Context context){
        if(FileHandler.loadOAuth2code(context) == ""){
            //tee tunnus
            String code = "";
            FileHandler.saveOAuth2code(code,context);
        }else{
            //jos tunnus on jo olemassa: tehdäänkö kuitenkin uusi? (esim. jos tunnus ei syystä tai toisesta toimi)
        }
        return true;
    }

}
