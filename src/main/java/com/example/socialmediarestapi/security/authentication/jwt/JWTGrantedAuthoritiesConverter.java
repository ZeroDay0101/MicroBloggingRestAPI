package com.example.socialmediarestapi.security.authentication.jwt;

import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;

public class JWTGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList();

        for (String authority : this.getAuthorities(jwt).values()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(authority));
        }

        return grantedAuthorities;
    }

    private LinkedTreeMap<String, String> getAuthorities(Jwt jwt) {
        String claimName = "roles";

        ArrayList<LinkedTreeMap<String, String>> authorities = jwt.getClaim(claimName);
        return authorities != null ? authorities.get(0) : new LinkedTreeMap<String, String>();

    }


}
