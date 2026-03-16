package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты для IntOpenAddressingHashListTest")
public class IntOpenAddressingHashListTest {

    private IntOpenAddressingHashList list;

    @BeforeEach
    void listUp() {
        // Инициализируем чистый набор перед каждым тестом
        list = new IntOpenAddressingHashList();
    }

    @Test
    @DisplayName("Добавление дубликатов элементов увеличивает размер")
    void addUniqueElementsIncreasesSize() {
        boolean added1 = list.add(10);
        boolean added2 = list.add(20);
        boolean added3 = list.add(10); // Повтор

        assertThat(added1).isTrue();
        assertThat(added2).isTrue();
        assertThat(added3).isTrue(); // Дубликат добавлен
        assertThat(list.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("Поиск существующего элемента возвращает true")
    void containsExistingElementReturnsTrue() {
        list.add(5);
        list.add(15);

        assertThat(list.contains(5)).isTrue();
        assertThat(list.contains(15)).isTrue();
        assertThat(list.contains(99)).isFalse();
    }

    @Test
    @DisplayName("Удаление существующего элемента уменьшает размер")
    void removeExistingElementDecreasesSize() {
        list.add(100);
        list.add(200);

        boolean removed = list.remove(100);

        assertThat(removed).isTrue();
        assertThat(list.size()).isEqualTo(1);
        assertThat(list.contains(100)).isFalse();
    }

    @Test
    @DisplayName("Удаление несуществующего элемента возвращает false")
    void removeNonExistingElementReturnsFalse() {
        list.add(100);

        boolean removed = list.remove(999);

        assertThat(removed).isFalse();
        assertThat(list.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Очистка набора обнуляет размер")
    void clearEmptiesThelist() {
        list.add(1);
        list.add(2);
        list.add(3);

        list.clear();

        assertThat(list.size()).isZero();
    }

    @Test
    @DisplayName("Работа с отрицательными числами, нулем, MIN_VALUE")
    void handleNegativeNumbersAndZero() {
        list.add(-1);
        list.add(0);
        list.add(-100);
        list.add(Integer.MIN_VALUE);


        assertThat(list.size()).isEqualTo(4);
        assertThat(list.contains(-1)).isTrue();
        assertThat(list.contains(0)).isTrue();
        assertThat(list.contains(Integer.MIN_VALUE)).isTrue();

    }

    @Test
    @DisplayName("Проверка на пустом наборе")
    void isEmptyOnNewlist() {
        assertThat(list.size()).isZero();
    }
}