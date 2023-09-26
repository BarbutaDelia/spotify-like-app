package pos.PlaylistV2;//package pos.PlaylistV2;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

//@Configuration
//@EnableWebMvc
//public class WebConfig extends WebMvcConfigurerAdapter {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:3000")
//                .allowedMethods("PATCH", "OPTIONS")
//                .allowedHeaders("content-type", "authorization")
//                .exposedHeaders("content-type", "authorization")
//                .allowCredentials(false)
//                .maxAge(3600);
//    }
//}

//@Configuration
//@EnableWebMvc
//public class CorsConfig implements WebMvcConfigurer {
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**");
//        registry.allowedOrigins("https://a.com");
//    }
//}

//@Configuration
//@EnableWebMvc
//public class WebConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**");
//    }
//}
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@EnableWebSecurity
//public class WebSecurityConfig {
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
////        http.authorizeRequests()
////                .anyRequest()
////                .authenticated()
////                .and()
////                .httpBasic();
//        http.cors();
//        return http.build();
//    }
//}
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//@EnableWebMvc
//public class WebConfig implements Filter,WebMvcConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**");
//    }
//
//    @Override
//    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) {
//        HttpServletResponse response = (HttpServletResponse) res;
//        HttpServletRequest request = (HttpServletRequest) req;
//        System.out.println("WebConfig; "+request.getRequestURI());
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE,PATCH,HEAD");
//        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With,observe");
//        response.setHeader("Access-Control-Max-Age", "3600");
//        response.setHeader("Access-Control-Allow-Credentials", "true");
//        response.setHeader("Access-Control-Expose-Headers", "Authorization");
//        response.addHeader("Access-Control-Expose-Headers", "USERID");
//        response.addHeader("Access-Control-Expose-Headers", "ROLE");
//        response.addHeader("Access-Control-Expose-Headers", "responseType");
//        response.addHeader("Access-Control-Expose-Headers", "observe");
//        System.out.println("Request Method: "+request.getMethod());
//        if (!(request.getMethod().equalsIgnoreCase("OPTIONS"))) {
//            try {
//                chain.doFilter(req, res);
//            } catch(Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//            System.out.println("Pre-flight");
//            response.setHeader("Access-Control-Allow-Origin", "*");
//            response.setHeader("Access-Control-Allow-Methods", "POST,GET,DELETE,PUT,PATCH,HEAD");
//            response.setHeader("Access-Control-Max-Age", "3600");
//            response.setHeader("Access-Control-Allow-Headers", "Access-Control-Expose-Headers"+"Authorization, content-type," +
//                    "USERID"+"ROLE"+
//                    "access-control-request-headers,access-control-request-method,accept,origin,authorization,x-requested-with,responseType,observe");
//            response.setStatus(HttpServletResponse.SC_OK);
//        }
//
//    }
//
//}