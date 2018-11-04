package cn.ssm.shiro;

import org.apache.shiro.mgt.SecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShiroConfig {

    @Bean
    public ShiroSecurityManagerFactory securityManagerFactory(){
        return ShiroSecurityManagerFactory.getInstance();
    }

    @Bean
    public SecurityManager createUser(ShiroSecurityManagerFactory securityManagerFactory){
        return  securityManagerFactory.createSecurityManager();
    }
}
