package ru.restvoting.util;

import org.springframework.core.NestedExceptionUtils;
import org.springframework.lang.NonNull;
import ru.restvoting.HasId;
import ru.restvoting.model.AbstractBaseEntity;
import ru.restvoting.model.Vote;
import ru.restvoting.util.exception.IllegalDateTimeException;
import ru.restvoting.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;

public class ValidationUtil {

    public static final LocalTime VOTING_DEADLINE = LocalTime.of(11, 0);

    private ValidationUtil() {
    }

    public static <T> T checkNotFoundWithId(T object, int id) {
        checkNotFoundWithId(object != null, id);
        return object;
    }

    public static void checkNotFoundWithId(boolean found, int id) {
        checkNotFound(found, "id=" + id);
    }

    public static <T> T checkNotFound(T object, String msg) {
        checkNotFound(object != null, msg);
        return object;
    }

    public static void checkNotFound(boolean found, String msg) {
        if (!found) {
            throw new NotFoundException("Not found entity with " + msg);
        }
    }

    public static void checkNew(AbstractBaseEntity entity) {
        if (!entity.isNew()) {
            throw new IllegalArgumentException(entity + " must be new (id=null)");
        }
    }

    public static void assureIdConsistent(HasId bean, int id) {
//      conservative when you reply, but accept liberally (http://stackoverflow.com/a/32728226/548473)
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.id() != id) {
            throw new IllegalArgumentException(bean + " must be with id=" + id);
        }
    }

    public static void validateVote(Vote vote) {
        if (!vote.getVoteDate().isEqual(LocalDate.now()) || getLocalTime().isAfter(VOTING_DEADLINE)) {
            throw new IllegalDateTimeException("The vote can be accepted only today before 11:00");
        }
    }

    public static LocalTime getLocalTime() {
         return LocalTime.now();
    }

    //  https://stackoverflow.com/a/65442410/548473
    @NonNull
    public static Throwable getRootCause(@NonNull Throwable t) {
        Throwable rootCause = NestedExceptionUtils.getRootCause(t);
        return rootCause != null ? rootCause : t;
    }

    public static String getMessage(Throwable e) {
        return e.getLocalizedMessage() != null ? e.getLocalizedMessage() : e.getClass().getName();
    }
}