package org.lbogdanov.poker.core.impl;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.lbogdanov.poker.core.User;
import org.lbogdanov.poker.core.UserService;
import org.scribe.up.profile.google2.Google2Profile;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.annotation.Transactional;


/**
 * Implementation of {@link UserService} interface.
 * 
 * @author Leonid Bogdanov
 */
@Singleton
public class UserServiceImpl implements UserService {

    private static final Object USER_KEY = "USER_KEY";

    @Inject
    private EbeanServer ebean;

    /**
     * {@inheritDoc}
     */
    @Override
    public User getCurrentUser() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.getPrincipal() == null) {
            return null;
        } else {
            Session session = subject.getSession();
            synchronized (session) {
                User user = (User) session.getAttribute(USER_KEY);
                if (user == null) {
                    user = findOrCreateUser(subject.getPrincipals());
                    session.setAttribute(USER_KEY, user);
                }
                return user;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void login(String username, String password, boolean rememberme) {
        SecurityUtils.getSubject().login(new UsernamePasswordToken(username, password, rememberme));
    }

    @Transactional
    User findOrCreateUser(PrincipalCollection principals) {
        User user = ebean.find(User.class)
                         .where().eq("externalId", toExternalId(principals))
                         .findUnique();
        if (user == null) {
            user = initUser(principals);
            ebean.save(user);
        }
        return user;
    }

    private static String toExternalId(PrincipalCollection principals) {
        Google2Profile googleProfile = principals.oneByType(Google2Profile.class);
        if (googleProfile != null) { // Google OAuth realm
            return new Sha256Hash(googleProfile.getId(), "google").toHex();
        }
        String simpleProfile = principals.oneByType(String.class);
        if (simpleProfile != null) { // Ini realm
            return new Sha256Hash(simpleProfile, "ini").toHex();
        }
        throw new UnsupportedOperationException("Unsupported realm");
    }

    private static User initUser(PrincipalCollection principals) {
        User user = new User();
        user.setExternalId(toExternalId(principals));
        Google2Profile googleProfile = principals.oneByType(Google2Profile.class);
        if (googleProfile != null) { // Google OAuth realm
            user.setFirstName(googleProfile.getFirstName());
            user.setLastName(googleProfile.getFamilyName());
            user.setEmail(googleProfile.getEmail());
            return user;
        }
        String simpleProfile = principals.oneByType(String.class);
        if (simpleProfile != null) { // Ini realm
            user.setFirstName(simpleProfile);
            return user;
        }
        throw new UnsupportedOperationException("Unsupported realm");
    }

}
