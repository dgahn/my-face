package com.pay_here.my_face.security.jwt

import com.pay_here.my_face.domain.RefreshToken
import com.pay_here.my_face.domain.RefreshTokenJpaRepository
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SecurityException
import mu.KotlinLogging
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.security.Key
import java.util.Date

@Component
class TokenProvider(
    @Value("\${jwt.secret}") private val secret: String,
    @Value("\${jwt.token-validity-in-seconds}") tokenValidityInSeconds: Long,
    @Value("\${jwt.refresh-token-validity-in-seconds}") refreshTokenValidityInSeconds: Long,
    private val refreshTokenJpaRepository: RefreshTokenJpaRepository
) : InitializingBean {
    private val tokenValidityInMilliseconds: Long
    private val refreshTokenValidityInMilliseconds: Long
    private lateinit var key: Key

    init {
        tokenValidityInMilliseconds = tokenValidityInSeconds * SEC_TO_MILLI_SEC
        refreshTokenValidityInMilliseconds = refreshTokenValidityInSeconds * SEC_TO_MILLI_SEC
    }

    override fun afterPropertiesSet() {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret))
    }

    fun createToken(authentication: Authentication): String {
        val authorities = authentication.authorities.joinToString { obj: GrantedAuthority -> obj.authority }
        val validity = Date(Date().time + tokenValidityInMilliseconds)

        return Jwts.builder()
            .setSubject(authentication.name)
            .claim(AUTHORITIES_KEY, authorities)
            .signWith(key, SignatureAlgorithm.HS512)
            .setExpiration(validity)
            .compact()
    }

    fun getAuthentication(token: String): Authentication {
        val claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body

        val authorities: Collection<GrantedAuthority> =
            claims[AUTHORITIES_KEY].toString().split(",".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
                .map { role: String? -> SimpleGrantedAuthority(role) }

        val principal = User(claims.subject, "", authorities)

        return UsernamePasswordAuthenticationToken(principal, token, authorities)
    }

    @Suppress("SwallowedException")
    fun validateToken(token: String?): Boolean {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
            return true
        } catch (e: SecurityException) {
            logger.info { "잘못된 JWT 서명입니다." }
        } catch (e: MalformedJwtException) {
            logger.info { "잘못된 JWT 서명입니다." }
        } catch (e: ExpiredJwtException) {
            logger.info { "만료된 JWT 토큰입니다." }
        } catch (e: UnsupportedJwtException) {
            logger.info { "지원되지 않는 JWT 토큰입니다." }
        } catch (e: IllegalArgumentException) {
            logger.info { "JWT 토큰이 잘못되었습니다." }
        }
        return false
    }

    @Transactional
    fun reissueRefreshToken(refreshToken: String): String {
        val authentication = getAuthentication(refreshToken!!)
        val findRefreshToken = refreshTokenJpaRepository.findByIdOrNull(authentication.name)
            ?: throw UsernameNotFoundException("존재하지 않는 이메일입니다. ${authentication.name}")
        return if (findRefreshToken.token == refreshToken) {
            val newRefreshToken = createRefreshToken(authentication)
            findRefreshToken.changeToken(newRefreshToken)
            newRefreshToken
        } else {
            logger.info { "refresh 토큰이 일치하지 않습니다. " }
            findRefreshToken.token
        }
    }

    @Transactional
    fun issueRefreshToken(authentication: Authentication): String {
        val newRefreshToken = createRefreshToken(authentication)

        val refreshToken = refreshTokenJpaRepository.findByIdOrNull(authentication.name)
        if (refreshToken != null) {
            refreshToken.changeToken(newRefreshToken)
        } else {
            refreshTokenJpaRepository.save(RefreshToken(authentication.name, newRefreshToken))
        }

        return newRefreshToken
    }

    private fun createRefreshToken(authentication: Authentication): String {
        val now = Date()
        val validity = Date(now.time + refreshTokenValidityInMilliseconds)
        return Jwts.builder()
            .setSubject(authentication.name)
            .setIssuedAt(now)
            .signWith(key, SignatureAlgorithm.HS512)
            .setExpiration(validity)
            .compact()
    }

    companion object {
        private const val AUTHORITIES_KEY = "auth"
        private const val SEC_TO_MILLI_SEC = 1000
        private val logger = KotlinLogging.logger { }
    }
}
