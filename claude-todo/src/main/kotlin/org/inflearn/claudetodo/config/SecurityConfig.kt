package org.inflearn.claudetodo.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { auth ->
                auth.anyRequest().permitAll() // 모든 요청 허용 (개발용)
            }
            .formLogin { form ->
                form.disable()
            }
            .csrf { csrf ->
                csrf.disable()
            }

        return http.build()
    }
}
