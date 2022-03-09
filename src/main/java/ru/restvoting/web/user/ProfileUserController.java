package ru.restvoting.web.user;


import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.restvoting.model.User;

import static ru.restvoting.web.SecurityUtil.authUserId;

@RestController
@RequestMapping("/restvot/profile")
public class ProfileUserController extends AbstractUserController {

    @GetMapping
    public User get() {
        return super.get(authUserId());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete() {
        super.delete(authUserId());
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void update(User user) {
        super.update(user, authUserId());
    }
}