package ru.restvoting.web;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class MatcherFactory {
    public static <T> Matcher<T> usingIgnoringFieldsComparator(String... fieldsToIgnore) {
        return new Matcher<>(fieldsToIgnore);
    }

    public static class Matcher<T> {
        private final String[] fieldsToIgnore;

        private Matcher(String... fieldsToIgnore) {
            this.fieldsToIgnore = fieldsToIgnore;
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
    }
}
