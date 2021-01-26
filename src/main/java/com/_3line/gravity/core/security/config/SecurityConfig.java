package com._3line.gravity.core.security.config;

import com._3line.gravity.core.security.jwt.JwtAuthenticationEntryPoint;
import com._3line.gravity.core.security.jwt.JwtAuthenticationTokenFilter;
import com._3line.gravity.core.security.service.AuthenticationFailureHandler;
import com._3line.gravity.core.security.service.AuthenticationSuccessHandler;
import com._3line.gravity.core.security.service.implementation.CustomAuthenticationProvider;
import com.querydsl.core.annotations.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

/**
 * @author FortunatusE
 * @date 9/22/2018
 */


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Configuration
    @Order(1)
    public static class ApplicationUserConfigurationAdapter extends WebSecurityConfigurerAdapter {

        @Autowired
        private AuthenticationSuccessHandler authenticationSuccessHandler;

        @Autowired
        private AuthenticationFailureHandler authenticationFailureHandler;

        @Autowired
        private CustomAuthenticationProvider customAuthenticationProvider;


        public ApplicationUserConfigurationAdapter() {
            super();
        }

        @Bean
        public SessionRegistry sessionRegistry() {
            return new SessionRegistryImpl();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder authenticationBuilder) throws Exception {
            authenticationBuilder.authenticationProvider(customAuthenticationProvider);
        }

        @Override
        protected void configure(HttpSecurity httpSecurity) throws Exception {

            httpSecurity.antMatcher("/core/**")
                    .authorizeRequests()
                    .anyRequest()
                    .fullyAuthenticated()
                    .and()
                    .formLogin().loginPage("/core/login").permitAll()
                    .loginProcessingUrl("/core/login/process")
//                    .defaultSuccessUrl("/core/dashboard", true).failureUrl("/core/login?failed=true")
                    .defaultSuccessUrl("/core/bankdetails/", true).failureUrl("/core/login?failed=true")
                    .successHandler(authenticationSuccessHandler)
                    .failureHandler(authenticationFailureHandler)
                    .and().
                    logout().logoutUrl("/core/logout")
                    .logoutSuccessUrl("/").deleteCookies("JSESSIONID").invalidateHttpSession(true)
                    .and().csrf().disable()
                    .sessionManagement()
                    .invalidSessionUrl("/core/login")
                    .maximumSessions(1)
                    .expiredUrl("/core/login?expired=true")
                    .sessionRegistry(sessionRegistry()).and()
                    .sessionFixation()
                    .migrateSession().
                    sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
        }

        @Override
        public void configure(WebSecurity webSecurity) throws Exception {

            webSecurity.ignoring().antMatchers("/", "/core/utility/**","/home/**", "/password/**", "/username/retrieve", "/resources/**", "/static/**", "/assets/**", "/fonts/**", "/css/**", "/js/**", "/img/**", "/images/**","/actuator/**", "/integration/**");

        }
    }


    @Configuration
    @Order(2)
    public static class ApplicationHttpUserConfigurationAdapter extends WebSecurityConfigurerAdapter{

        @Autowired
        private JwtAuthenticationEntryPoint unauthorizedHandler;

        @Autowired
        @Qualifier("jwtservice")
        private UserDetailsService userDetailsService;

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception{
            return super.authenticationManagerBean();
        }


        @Autowired
        public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
            authenticationManagerBuilder
                    .userDetailsService(this.userDetailsService)
                    .passwordEncoder(passwordEncoder());
        }

        @Bean
        public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
            return new JwtAuthenticationTokenFilter();
        }


        @Override
        protected void configure(HttpSecurity httpSecurity) throws Exception {
            //httpSecurity.addFilterBefore(new CorsFilter(), BasicAuthenticationFilter.class);

            // Custom JWT based security filter
            httpSecurity
                    .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

            httpSecurity
                    .csrf().disable()
                    .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()

                    // don't create session
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                    .authorizeRequests()
                    .antMatchers("/auth","/gravity/api/oauth/**","gravity/api/utility/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/v2/*","/swagger-ui.html","/swagger-resources/**","/configuration/ui","/webjars/**").permitAll()
                    .antMatchers(HttpMethod.OPTIONS, "/gravity/api/**").permitAll()
                    .antMatchers(HttpMethod.POST, "/gravity/api/wallet/enquiry/**","/gravity/api/wallet/fund/direct","/gravity/api/wallet/transaction/**",
                            "/gravity/api/itex/**","/gravity/api/transactions/reverse","/integration/**","/gravity/api/aggregator/validate_agent", "/gravity/api/bills/updateBillService").permitAll()
                    .anyRequest().authenticated();

        }


    }
}
