package cn.ssm.shiro;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.util.Factory;
import org.apache.shiro.config.IniSecurityManagerFactory;

public class ShiroSecurityManagerFactory {
    private static ShiroSecurityManagerFactory instance = null;
    private static Factory<SecurityManager> factory = null;
    private ShiroSecurityManagerFactory() { }

    public static  ShiroSecurityManagerFactory getInstance() {
        if (instance == null) {
            synchronized (ShiroSecurityManagerFactory.class) {
                if (instance == null) {
                    factory = new IniSecurityManagerFactory("classpath:shiro.ini");
                    instance = new ShiroSecurityManagerFactory();
                }
            }
        }
        return instance;
    }

    public SecurityManager createSecurityManager(){
        return factory.getInstance();
    }
}
