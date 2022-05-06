package ru.restvoting.web.user;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import ru.restvoting.model.User;
import ru.restvoting.repository.UserRepository;

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

    public void delete(int id) {
        log.info("delete {}", id);
        userRepository.deleteExisted(id);
    }

    User prepareAndSave(User user) {
        return userRepository.save(prepareToSave(user));
    }
}