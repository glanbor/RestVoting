package ru.restvoting.util;


import org.springframework.core.NestedExceptionUtils;
import org.springframework.lang.NonNull;
import ru.restvoting.HasId;
import ru.restvoting.model.Vote;
import ru.restvoting.error.IllegalDateTimeException;
import ru.restvoting.error.NotFoundException;

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

    public static void checkNew(HasId bean) {
        if (!bean.isNew()) {
            throw new IllegalArgumentException(bean + " must be new (id=null)");
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
        if (!vote.getVoteDate().isEqual(LocalDate.now()) || DateTimeUtil.getLocalTime().isAfter(VOTING_DEADLINE)) {
            throw new IllegalDateTimeException("The vote can be accepted only today before 11:00");
        }
    }

//    public static LocalTime getLocalTime() {
//         return LocalTime.now();
//    }

    //  https://stackoverflow.com/a/65442410/548473
    @NonNull
    public static Throwable getRootCause(@NonNull Throwable t) {
        Throwable rootCause = NestedExceptionUtils.getRootCause(t);
        return rootCause != null ? rootCause : t;
    }

    public static void checkModification(int count, int id) {
        if (count == 0) {
            throw new NotFoundException("Entity with id=" + id + " not found");
        }
    }

    public static void checkMenuDate(LocalDate menuDate) {
        if (menuDate.isBefore(LocalDate.now())
                || (menuDate.isEqual(LocalDate.now())&& LocalTime.now().isAfter(VOTING_DEADLINE))) {
            throw new IllegalDateTimeException("Illegal DateTime for creating or updating the menu");
        }
    }
}