package team.f5.api.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebMvc
class WebMvcConfig : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods(
                HttpMethod.GET.name,
                HttpMethod.HEAD.name,
                HttpMethod.POST.name,
                HttpMethod.PUT.name,
                HttpMethod.DELETE.name,
                HttpMethod.PATCH.name
            )
    }

    @Bean
    fun corsConfigurationSource(): UrlBasedCorsConfigurationSource {
        val corsConfigSource = UrlBasedCorsConfigurationSource()
        val corsConfig = CorsConfiguration()
        corsConfig.allowedHeaders = listOf("*")
        corsConfig.allowedMethods = HttpMethod.values().map { it.name }
        corsConfig.allowedOrigins = listOf("http://localhost:5173", "http://127.0.0.1:5173")
        corsConfig.allowCredentials = true
        corsConfig.maxAge = 3600
        corsConfigSource.registerCorsConfiguration("/**", corsConfig)
        return corsConfigSource
    }
}
