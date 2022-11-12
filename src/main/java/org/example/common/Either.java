package org.example.common;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.function.Function;

@Slf4j
public class Either<L, R> {

    private L left;
    private R right;

    private Either(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public static <L, R> Either<L, R> left(L value) {
        return new Either<>(value, null);
    }

    public static <L, R> Either<L, R> right(R value) {
        return new Either<>(null, value);
    }

    public boolean isLeft() {
        return left != null;
    }

    public boolean isRight() {
        return right != null;
    }

    public Optional<L> getLeft() {
        return Optional.ofNullable(left);
    }


    public Optional<R> getRight() {
        return Optional.ofNullable(right);
    }

    public <T> Optional<T> mapLeft(Function<? super L, T> mapper) {
        if (isLeft()) {
            return Optional.of(mapper.apply(left));
        }

        return Optional.empty();
    }

    public <T> Optional<T> mapRight(Function<? super R, T> mapper) {
        if (isRight()) {
            return Optional.of(mapper.apply(right));
        }

        return Optional.empty();
    }

    public Optional<?> get() {
        if (isLeft()) {
            return Optional.ofNullable(left);
        }
        return Optional.ofNullable(right);
    }

    public boolean isNotNull() {
        return !(left == null && right == null);
    }

    public static <T, L> Function<T, Either> lift(Function<T, L> function) {
        return (T t) -> {
            try {
                return Either.left(function.apply(t));
            } catch (Exception ex) {
                log.debug("save object {}, occured exception: {}", t.toString(), ex.getMessage());
//                return Either.right("");
                return Either.right(new ObjectWithException<>(t, ex));
            }
        };
    }
}