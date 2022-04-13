package ru.restvoting.web.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;
import ru.restvoting.model.User;
import ru.restvoting.repository.UserRepository;
import ru.restvoting.to.UserTo;
import ru.restvoting.util.UserUtil;

import java.util.List;

import static ru.restvoting.util.ValidationUtil.*;

public abstract class AbstractUserController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected UserRepository userRepository;

    public List<User> getAll() {
        log.info("getAll");
        return userRepository.findAll(Sort.by(Sort.Direction.ASC, "name", "email"));
    }

    public User get(int id) {
        log.info("get {}", id);
        return userRepository.findById(id).orElse(null);
    }

    public User create(User user) {
        log.info("create {}", user);
        checkNew(user);
        Assert.notNull(user, "user must not be null");
        return userRepository.save(user);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        checkNotFoundWithId(userRepository.delete(id), id);
    }

    public void update(UserTo userTo, int id) {
        log.info("update {} with id={}", userTo, id);
        assureIdConsistent(userTo, id);
        User user = checkNotFoundWithId(userRepository.findById(id).orElse(null), id);
        UserUtil.updateFromTo(user, userTo);
    }

    public void update(User user, int id) {
        log.info("update {} with id={}", user, id);
        assureIdConsistent(user, id);
        checkNotFoundWithId(userRepository.save(user), user.id());
    }

    public User getByMail(String email) {
        log.info("getByEmail {}", email);
        return userRepository.getByEmail(email);
    }


}