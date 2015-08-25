package com.zjnan.app.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.AccessDeniedException;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContext;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.context.SecurityContextImpl;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import com.zjnan.app.Constants;
import com.zjnan.app.dao.UserDao;
import com.zjnan.app.model.Authority;
import com.zjnan.app.model.Role;
import com.zjnan.app.model.User;
import com.zjnan.app.service.UserManager;
import com.zjnan.app.service.UserSecurityAdvice;

import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.Mockery;
import org.jmock.Expectations;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

@RunWith(JMock.class)
public class UserSecurityAdviceTest {
    Mockery context = new JUnit4Mockery();
    UserDao userDao = null;
    ApplicationContext ctx = null;
    SecurityContext initialSecurityContext = null;

    @Before
    public void setUp() throws Exception {
        // store initial security context for later restoration
        initialSecurityContext = SecurityContextHolder.getContext();

        SecurityContext context = new SecurityContextImpl();
        
        Role role = new Role();
             role.addAuthority(new Authority(Constants.USER_ROLE));
        
        User user = new User("user");
        user.setId(1L);
        user.setPassword("password");
        user.addRole(role);

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
        token.setDetails(user);
        context.setAuthentication(token);
        SecurityContextHolder.setContext(context);
        System.out.println();;
    }

    @After
    public void tearDown() {
        SecurityContextHolder.setContext(initialSecurityContext);
    }

    @Test
    public void testAddUserWithoutAdminRole() throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertTrue(auth.isAuthenticated());
        UserManager userManager = makeInterceptedTarget();
        User user = new User("admin");
        user.setId(2L);

        try {
            userManager.saveUser(user);
            fail("AccessDeniedException not thrown");
        } catch (AccessDeniedException expected) {
            assertNotNull(expected);
            assertEquals(expected.getMessage(), UserSecurityAdvice.ACCESS_DENIED);
        }
    }

    @Test
    public void testAddUserAsAdmin() throws Exception {
        SecurityContext securityContext = new SecurityContextImpl();
        User user = new User("admin");
        user.setId(2L);
        user.setPassword("password");
        user.addRole(new Role(Constants.ADMIN_ROLE));
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
        token.setDetails(user);
        securityContext.setAuthentication(token);
        SecurityContextHolder.setContext(securityContext);

        UserManager userManager = makeInterceptedTarget();
        final User adminUser = new User("admin");
        adminUser.setId(2L);

        context.checking(new Expectations() {{
            one(userDao).saveUser(with(same(adminUser)));
        }});

        userManager.saveUser(adminUser);
    }

    @Test
    public void testUpdateUserProfile() throws Exception {
        UserManager userManager = makeInterceptedTarget();
        
        Role role = new Role(Constants.USER_ROLE);
        role.addAuthority(new Authority(Constants.USER_ROLE));
        
        final User user = new User("user");
        user.setId(1L);
        user.getRoles().add(role);
        
        Object o = user.getAuthorities();
        context.checking(new Expectations() {{
            one(userDao).saveUser(with(same(user)));
        }});

        userManager.saveUser(user);
    }
    
    // Test fix to http://issues.appfuse.org/browse/APF-96
    @Test
    public void testChangeToAdminRoleFromUserRole() throws Exception {
        UserManager userManager = makeInterceptedTarget();
        User user = new User("user");
        user.setId(1L);
        user.getRoles().add(new Role(Constants.ADMIN_ROLE));

        try {
            userManager.saveUser(user);
            fail("AccessDeniedException not thrown");
        } catch (AccessDeniedException expected) {
            assertNotNull(expected);
            assertEquals(expected.getMessage(), UserSecurityAdvice.ACCESS_DENIED);
        }
    }

    // Test fix to http://issues.appfuse.org/browse/APF-96
    @Test
    public void testAddAdminRoleWhenAlreadyHasUserRole() throws Exception {
        UserManager userManager = makeInterceptedTarget();
        User user = new User("user");
        user.setId(1L);
        user.getRoles().add(new Role(Constants.ADMIN_ROLE));
        user.getRoles().add(new Role(Constants.USER_ROLE));

        try {
            userManager.saveUser(user);
            fail("AccessDeniedException not thrown");
        } catch (AccessDeniedException expected) {
            assertNotNull(expected);
            assertEquals(expected.getMessage(), UserSecurityAdvice.ACCESS_DENIED);
        }
    }

    // Test fix to http://issues.appfuse.org/browse/APF-96
    @Test
    public void testAddUserRoleWhenHasAdminRole() throws Exception {
        SecurityContext securityContext = new SecurityContextImpl();
        
        Role role = new Role();
             role.addAuthority(new Authority(Constants.ADMIN_ROLE));
        User user1 = new User("user");
        user1.setId(1L);
        user1.setPassword("password");
        user1.addRole(role);
        
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(user1.getUsername(), user1.getPassword(), user1.getAuthorities());
        token.setDetails(user1);
        securityContext.setAuthentication(token);
        SecurityContextHolder.setContext(securityContext);

        UserManager userManager = makeInterceptedTarget();
        
        
        Role role1 = new Role();
             role1.addAuthority(new Authority(Constants.ADMIN_ROLE));
        Role role2 = new Role();
             role2.addAuthority(new Authority(Constants.USER_ROLE));
        
        final User user = new User("user");
        user.setId(1L);
        user.getRoles().add(role1);
        user.getRoles().add(role2);

        context.checking(new Expectations() {{
            one(userDao).saveUser(with(same(user)));
        }});

        userManager.saveUser(user);
    }

    // Test fix to http://issues.appfuse.org/browse/APF-96
    @Test
    public void testUpdateUserWithUserRole() throws Exception {
        UserManager userManager = makeInterceptedTarget();
        final User user = new User("user");
        user.setId(1L);
        user.getRoles().add(new Role(Constants.USER_ROLE));

        context.checking(new Expectations() {{
            one(userDao).saveUser(with(same(user)));
        }});

        userManager.saveUser(user);
    }

    private UserManager makeInterceptedTarget() {
        ctx = new ClassPathXmlApplicationContext("/applicationContext-test.xml");

        UserManager userManager = (UserManager) ctx.getBean("target");

        // Mock the userDao
        userDao = context.mock(UserDao.class);
        userManager.setUserDao(userDao);
        return userManager;
    }
}
