package project.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/**").permitAll()
                    .and()
                .formLogin()
                    .loginPage("/user/login")
                    .loginProcessingUrl("/user/login")
                    .failureUrl("/user/login?error")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .permitAll();
//        .userDetailsService(this.userDetailsService);
    }

}
