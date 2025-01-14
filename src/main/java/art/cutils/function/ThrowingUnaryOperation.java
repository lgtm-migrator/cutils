/*
 * _________  ____ ______________.___.____       _________
 * \_   ___ \|    |   \__    ___/|   |    |     /   _____/
 * /    \  \/|    |   / |    |   |   |    |     \_____  \
 * \     \___|    |  /  |    |   |   |    |___  /        \
 *  \______  /______/   |____|   |___|_______ \/_______  /
 *         \/                                \/        \/
 *
 * Copyright (C) 2018 — 2022 Bobai Kato. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package art.cutils.function;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an operation on a single operand that produces a result of the same type as its
 * operand. This is a specialization of {@code Function} for the case where the operand and result
 * are of the same type.
 *
 * <p>When you find yourself in a situation where you need to use a function ( that throws an
 * exception), this class is what you need to achieve your goal without Java complaining.
 *
 * <p>The class Represents a function that accepts one argument and produces a result. whose
 * functional method is {@link #apply(Object)}.
 *
 * @param <T> the type of the input to the function
 * @see ThrowingFunction
 * @see UnaryOperator
 * @author @author <a href="https://github.com/B0BAI">Bobai Kato</a>
 * @since 1.0
 */
@FunctionalInterface
public interface ThrowingUnaryOperation<T> extends ThrowingFunction<T, T> {

  /**
   * Returns a unary operator that always returns its input argument.
   *
   * @param <T> the type of the input and output of the operator
   * @return a unary operator that always returns its input argument
   */
  @Contract(pure = true)
  static <T> @NotNull UnaryOperator<T> identity() {
    return t -> t;
  }

  /**
   * Uncheck method which will take operation that will throw Exception.
   *
   * @param operator Variable of {@link ThrowingFunction}
   * @param <T>      the type of the input to the function
   * @return A {@link Function}
   */
  @Contract(pure = true)
  static <T> @NotNull UnaryOperator<T> unchecked(final ThrowingUnaryOperation<T> operator) {
    Objects.requireNonNull(operator, "operator cannot be null");
    return argument -> {
      try {
        return operator.apply(argument);
      } catch (Exception ex) {
        return sneakyThrow(ex);
      }
    };
  }

  /**
   * Sneak exception on function execution.
   *
   * @param ex exception throw on operation
   * @param <T> arg type
   * @param <R> return type
   * @return an exception
   * @throws T arg type exception
   */
  @SuppressWarnings("unchecked")
  @Contract(value = "_ -> fail", pure = true)
  static <T extends Exception, R> R sneakyThrow(final Exception ex) throws T {
    throw (T) ex;
  }
}