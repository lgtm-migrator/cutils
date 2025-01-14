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

package collection;

import static art.cutils.collection.ListPartition.of;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import art.cutils.collection.ListPartition;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Test List Partitioning")
final class ListPartitionTest {

  private static @NotNull Stream<Arguments> partitionListInSubListIResource() {
    return Stream.of(
        Arguments.of(3, asList(1, 2, 3, 4, 5, 6, 7, 8, 9)),
        Arguments.of(2, asList("A", "B", "C", "D", "E", "F")),
        Arguments.of(4, asList('a', 'b', 'c', '4', '5', '6', '7', '8', '9', 'd', 'e', 'f')),
        Arguments.of(
            2,
            new ArrayList<List<Integer>>() {
              {
                add(asList(1, 2));
                add(asList(3, 4));
                add(asList(5, 6));
                add(asList(7, 8));
                add(asList(9, 10));
                add(asList(11, 12));
              }
            }));
  }

  private static @NotNull Stream<Arguments> partitionListInSubListIIResource() {
    return Stream.of(
        Arguments.of(6, asList(1, 2, 3, 4, 5)),
        Arguments.of(7, asList("A", "B", "C", "D", "E", "F")),
        Arguments.of(20, asList('a', 'b', 'c', '4', '5', '6', '9', 'd', 'e')));
  }

  private static @NotNull Stream<Arguments> partitionSizeLessThanOneResource() {
    return Stream.of(
        Arguments.of(0, asList(1, 2, 3, 4, 5)),
        Arguments.of(-7, asList("A", "B", "C", "D", "E", "F")),
        Arguments.of(-2, asList('a', 'b', 'c', 'd')));
  }

  private static @NotNull Stream<Arguments> partitionListNullResource() {
    return Stream.of(Arguments.of(1, null), Arguments.of(2, null));
  }

  @DisplayName("Should successfully ListPartition List by the provided sub-list Size.")
  @ParameterizedTest(name = "{index} => partitionSize={0}, list={1}")
  @MethodSource("partitionListInSubListIResource")
  void partitionListInSubListI(int partitionSize, List<?> list) {
    final ListPartition<?> partitionedList = of(list).into(partitionSize);
    assertEquals(partitionedList.size(), 3);
    partitionedList.parallelStream().forEach(item -> assertEquals(item.size(), partitionSize));
  }

  @DisplayName(
      "Should successfully ListPartition List when ListPartition size is greater than Item in List.")
  @ParameterizedTest(name = "{index} => partitionSize={0}, partitionedList={1}")
  @MethodSource("partitionListInSubListIIResource")
  void partitionListInSubListII(int partitionSize, List<?> list) {
    final ListPartition<?> partitionedList = of(list).into(partitionSize);
    assertEquals(partitionedList.size(), 1);
    partitionedList.forEach(item -> assertNotEquals(item.size(), partitionSize));
    partitionedList.forEach(item -> assertEquals(item.size(), list.size()));
  }

  @DisplayName("Should throw NullPointerException  when trying to ListPartition a null List.")
  @ParameterizedTest(name = "{index} => partitionSize={0}, partitionedList={1}")
  @MethodSource("partitionListNullResource")
  void nullPointerExceptionForNullListPartition(int partitionSize, List<?> list) {
    assertThrows(NullPointerException.class, () -> of(list).into(partitionSize));
  }

  @DisplayName("Should throw IllegalArgumentException when partitionSize is Less that 1.")
  @ParameterizedTest(name = "{index} => partitionSize={0}, partitionedList={1}")
  @MethodSource("partitionSizeLessThanOneResource")
  void illegalArgumentExceptionForPartition(int partitionSize, List<?> list) {
    assertThrows(IllegalArgumentException.class, () -> of(list).into(partitionSize));
  }

  @DisplayName("Should throw IndexOutOfBoundsException when index is less than List Size")
  @ParameterizedTest(name = "{index} => partitionSize={0}, partitionedList={1}")
  @MethodSource("partitionSizeLessThanOneResource")
  void testPartitionGetIndexOutOfBoundsException(final int index, final List<?> list) {
    final ListPartition<?> ListPartition = of(list).into(2);
    assertThrows(IndexOutOfBoundsException.class, () -> ListPartition.get(7));
  }

  @Test
  void equalsAndHashCodeContractToBeValid() {
    final ListPartition<?> p1 = of(asList("A", "B", "C", "D", "E", "F")).into(2);
    final ListPartition<?> p2 = of(asList("A", "B", "C", "D", "E", "F")).into(2);

    assertEquals(p1, p2);
    assertEquals(p1, p1);
    assertEquals(p2, p2);

    // Hashcode
    assertEquals(p1.hashCode(), p2.hashCode());
  }

  @Test
  void equalsAndHashCodeContractToBeInvalid() {
    final ListPartition<?> p1 = of(asList("A", "B", "C", "D", "E")).into(3);
    final ListPartition<?> p2 = of(asList("a", "b", "c", "d", "e")).into(3);

    assertNotEquals(p1, p2);
    assertNotEquals(p1, p2.get(0));
    assertNotEquals(p1.hashCode(), p2.hashCode());
  }
}