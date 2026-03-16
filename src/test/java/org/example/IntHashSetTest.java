package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты для IntHashSet")
class IntHashSetTest {

    private IntHashSet set;

    @BeforeEach
    void setUp() {
        // Инициализируем чистый набор перед каждым тестом
        set = new IntHashSet();
    }

    @Test
    @DisplayName("Добавление уникальных элементов увеличивает размер")
    void addUniqueElementsIncreasesSize() {
        boolean added1 = set.add(10);
        boolean added2 = set.add(20);
        boolean added3 = set.add(10); // Повтор

        assertThat(added1).isTrue();
        assertThat(added2).isTrue();
        assertThat(added3).isFalse(); // Дубликат не добавлен
        assertThat(set.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Поиск существующего элемента возвращает true")
    void containsExistingElementReturnsTrue() {
        set.add(5);
        set.add(15);

        assertThat(set.contains(5)).isTrue();
        assertThat(set.contains(15)).isTrue();
        assertThat(set.contains(99)).isFalse();
    }

    @Test
    @DisplayName("Удаление существующего элемента уменьшает размер")
    void removeExistingElementDecreasesSize() {
        set.add(100);
        set.add(200);

        boolean removed = set.remove(100);

        assertThat(removed).isTrue();
        assertThat(set.size()).isEqualTo(1);
        assertThat(set.contains(100)).isFalse();
    }

    @Test
    @DisplayName("Удаление несуществующего элемента возвращает false")
    void removeNonExistingElementReturnsFalse() {
        set.add(100);

        boolean removed = set.remove(999);

        assertThat(removed).isFalse();
        assertThat(set.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Очистка набора обнуляет размер")
    void clearEmptiesTheSet() {
        set.add(1);
        set.add(2);
        set.add(3);

        set.clear();

        assertThat(set.size()).isZero();
    }

    @Test
    @DisplayName("Работа с отрицательными числами, нулем, MIN_VALUE")
    void handleNegativeNumbersAndZero() {
        set.add(-1);
        set.add(0);
        set.add(-100);
        set.add(Integer.MIN_VALUE);


        assertThat(set.size()).isEqualTo(4);
        assertThat(set.contains(-1)).isTrue();
        assertThat(set.contains(0)).isTrue();
        assertThat(set.contains(Integer.MIN_VALUE)).isTrue();

    }

    @Test
    @DisplayName("Проверка на пустом наборе")
    void isEmptyOnNewSet() {
        assertThat(set.size()).isZero();
    }
}