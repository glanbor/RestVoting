package ru.restvoting.web.user;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import ru.restvoting.model.User;
import ru.restvoting.repository.UserRepository;
import ru.restvoting.to.UserTo;
import ru.restvoting.util.UserUtil;

import java.util.List;

import static ru.restvoting.util.UserUtil.prepareToSave;
import static ru.restvoting.util.ValidationUtil.*;

@Slf4j
public abstract class AbstractUserController {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    private UniqueMailValidator emailValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(emailValidator);
    }


    public List<User> getAll() {
        log.info("getAll");
        return userRepository.findAll(Sort.by(Sort.Direction.ASC, "name", "email"));
    }

    public User get(int id) {
        log.info("get {}", id);
        return checkNotFoundWithId(userRepository.findById(id).orElse(null), id);
    }

    public User create(User user) {
        log.info("create {}", user);
        checkNew(user);
        Assert.notNull(user, "user must not be null");
        return userRepository.save(user);
    }

    public User create(UserTo userTo) {
        log.info("create {}", userTo);
        checkNew(userTo);
        User newFromTo = UserUtil.createNewFromTo(userTo);
        Assert.notNull(newFromTo, "user must not be null");
        return prepareAndSave(newFromTo);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        userRepository.deleteExisted(id);
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

    private User prepareAndSave(User user) {
        return userRepository.save(prepareToSave(user));
    }
}