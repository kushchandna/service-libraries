package com.kush.lib.expressions.types;

public interface ComparableObject extends Comparable<ComparableObject> {

    Object get();

    static <T extends Comparable<T>> ComparableObject on(T object) {
        if (object instanceof ComparableObject) {
            return (ComparableObject) object;
        }
        return new DefaultComparableObject<>(object);
    }

    static final class DefaultComparableObject<T extends Comparable<T>> implements ComparableObject {

        private final T object;

        private DefaultComparableObject(T object) {
            this.object = object;
        }

        @Override
        @SuppressWarnings("unchecked")
        public int compareTo(ComparableObject o) {
            return object.compareTo((T) o.get());
        }

        @Override
        public Object get() {
            return object;
        }

        @Override
        public String toString() {
            return String.valueOf(object);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + (object == null ? 0 : object.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            @SuppressWarnings("unchecked")
            DefaultComparableObject<T> other = (DefaultComparableObject<T>) obj;
            if (object == null) {
                if (other.object != null) {
                    return false;
                }
            } else if (!object.equals(other.object)) {
                return false;
            }
            return true;
        }
    }
}
