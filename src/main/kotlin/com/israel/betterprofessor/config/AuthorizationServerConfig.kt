package com.israel.betterprofessor.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.provider.token.TokenStore

@Configuration
@EnableAuthorizationServer
open class AuthorizationServerConfig : AuthorizationServerConfigurerAdapter() {

    @Autowired
    private val tokenStore: TokenStore? = null

    @Autowired
    private val authenticationManager: AuthenticationManager? = null

    @Autowired
    private val encoder: PasswordEncoder? = null

    @Throws(Exception::class)
    override fun configure(configurer: ClientDetailsServiceConfigurer) {

        configurer.inMemory()
                .withClient(CLIENT_ID)
                .secret(encoder!!.encode(CLIENT_SECRET))
                .authorizedGrantTypes(GRANT_TYPE_PASSWORD, AUTHORIZATION_CODE, REFRESH_TOKEN, IMPLICIT)
                .scopes(SCOPE_READ, SCOPE_WRITE, TRUST)
                .accessTokenValiditySeconds(ACCESS_TOKEN_VALIDITY_SECONDS)
                .refreshTokenValiditySeconds(FREFRESH_TOKEN_VALIDITY_SECONDS)
    }

    @Throws(Exception::class)
    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer) {
        endpoints.tokenStore(tokenStore).authenticationManager(authenticationManager)
    }

    companion object {
        internal val CLIENT_ID = "better-professor-client"
        internal val CLIENT_SECRET = "better-professor-secret"
        internal val GRANT_TYPE_PASSWORD = "password"
        internal val AUTHORIZATION_CODE = "authorization_code"
        internal val REFRESH_TOKEN = "refresh_token"
        internal val IMPLICIT = "implicit"
        internal val SCOPE_READ = "read"
        internal val SCOPE_WRITE = "write"
        internal val TRUST = "trust"
        internal val ACCESS_TOKEN_VALIDITY_SECONDS = 1 * 60 * 60
        internal val FREFRESH_TOKEN_VALIDITY_SECONDS = 6 * 60 * 60
    }
}