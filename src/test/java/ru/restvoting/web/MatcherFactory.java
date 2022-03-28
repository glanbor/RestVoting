package ru.restvoting.web;

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import ru.restvoting.util.JsonUtil;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class MatcherFactory {
    public static <T> Matcher<T> usingIgnoringFieldsComparator(Class<T> clazz, String... fieldsToIgnore) {
        return new Matcher<>(clazz, fieldsToIgnore);
    }

    public static class Matcher<T> {
        private final Class<T> clazz;
        private final String[] fieldsToIgnore;

        private Matcher(Class<T> clazz, String... fieldsToIgnore) {
            this.fieldsToIgnore = fieldsToIgnore;
            this.clazz = clazz;
        }

        public void assertMatch(T actual, T expected) {
            assertThat(actual).usingRecursiveComparison().ignoringFields(fieldsToIgnore).isEqualTo(expected);
        }

        @SafeVarargs
        public final void assertMatch(Iterable<T> actual, T... expected) {
            assertMatch(actual, List.of(expected));
        }

        public void assertMatch(Iterable<T> actual, Iterable<T> expected) {
            assertThat(actual).usingRecursiveFieldByFieldElementComparatorIgnoringFields(fieldsToIgnore).isEqualTo(expected);
        }

        public ResultMatcher contentJson(T... expected) {
            return result -> assertMatch(JsonUtil.readValues(getContent(result), clazz), List.of(expected));
        }

        public ResultMatcher contentJson(T expected) {
            return result -> assertMatch(JsonUtil.readValue(getContent(result), clazz), expected);
        }

        public static String getContent(MvcResult result) throws UnsupportedEncodingException {
            return result.getResponse().getContentAsString();
        }
    }
}
