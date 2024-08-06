package com.inolog.service;

import com.inolog.config.OAuth2Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("[CustomOAuth2UserService] loadUser : {}", userRequest.getClientRegistration().getRegistrationId() );
        OAuth2User oAuth2User = super.loadUser(userRequest);

        OAuth2Principal principal = new OAuth2Principal(oAuth2User.getAttributes());
        return principal;

    }
}
