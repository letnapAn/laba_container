package org.example;
import java.util.Iterator;
/**
 * Одна из реализаций контейнера - аналог коллекции Set. Для ситуаций, когда важна скорость поиска.
 * <p>
 * Особенности реализации:
 * <ul>
 *   <li>Хранит только уникальные значения (дубликаты не допускаются)</li>
 *   <li>Использует метод цепочек (chaining) для разрешения коллизий</li>
 *   <li>При заполненности более 80% автоматически увеличивает размер вдвое</li>
 *   <li>Оптимизирован для быстрого поиска элементов</li>
 * </ul>
 *
 * @see IntContainer
 * @version 1.0
 */
public class IntHashSet implements IntContainer {

    /** Начальная емкость контейнера по умолчанию */
    private static final int DEFAULT_CAPACITY = 16;

    /** Массив "корзин" (buckets) для хранения голов связных списков */
    private Node[] buckets;

    /** Текущее количество элементов в контейнере */
    private int size;

    /**
     * Внутренний класс, представляющий узел связного списка.
     * Каждый узел содержит значение и ссылку на следующий элемент в цепочке.
     */
    private static class Node {
        /** Значение элемента */
        int value;
        /** Ссылка на следующий узел в цепочке (null для последнего элемента) */
        Node next;

        /**
         * Узел/клеточка/звено - для нового элемента списка
         *
         * @param value значение элемента
         * @param next следующий узел в цепочке (может быть null)
         */
        Node(int value, Node next) {
            this.value = value;
            this.next = next;
        }
    }

    /**
     * Создает пустой контейнер с начальной емкостью {@value #DEFAULT_CAPACITY}.
     */
    public IntHashSet() {
        this.buckets = new Node[DEFAULT_CAPACITY];
    }

    /**
     * Вычисляет индекс корзины для указанного значения.
     * <p>
     * Алгоритм:
     * <ol>
     *   <li>Для Integer.MIN_VALUE (который равен -2147483648) возвращает 0,
     *       так как Math.abs(Integer.MIN_VALUE) дает отрицательное значение</li>
     *   <li>Для остальных чисел берется абсолютное значение</li>
     *   <li>Вычисляется остаток от деления на длину массива корзин</li>
     * </ol>
     *
     * @param value значение для вычисления хэша
     * @return индекс в массиве buckets (от 0 до buckets.length - 1)
     */
    private int hash(int value) {
        int h = (value == Integer.MIN_VALUE) ? 0 : Math.abs(value);
        return h % buckets.length;
    }

    /**
     * Увеличивает размер контейнера в два раза.
     * <p>
     * Вызывается автоматически, когда коэффициент заполненности превышает 0.8.
     * При увеличении все существующие элементы перераспределяются по новым корзинам
     * с пересчетом их хэшей.
     */
    private void resize() {
        Node[] oldBuckets = buckets;
        buckets = new Node[oldBuckets.length * 2];
        size = 0;

        for (Node node : oldBuckets) {
            while (node != null) {
                Node next = node.next;
                int index = hash(node.value);
                // Вставка в начало списка новой корзины
                buckets[index] = new Node(node.value, buckets[index]);
                size++;
                node = next;
            }
        }
    }

    /**
     * Добавляет указанное значение в контейнер.
     * <p>
     * Если значение уже существует, добавление не производится.
     * После добавления проверяется коэффициент заполненности и при необходимости
     * вызывается {@link #resize()}.
     *
     * @param value целое число для добавления
     * @return true, если значение было добавлено (отсутствовало ранее),
     *         false, если значение уже существует в контейнере
     */
    @Override
    public boolean add(int value) {
        int index = hash(value);
        Node current = buckets[index];

        // Проверяем, нет ли уже такого значения
        while (current != null) {
            if (current.value == value) {
                return false; // Значение уже существует
            }
            current = current.next;
        }

        // Добавляем новый узел в начало списка
        buckets[index] = new Node(value, buckets[index]);
        size++;

        // Проверяем необходимость увеличения размера
        if ((float) size / buckets.length > 0.8) {
            resize();
        }
        return true;
    }

    /**
     * Проверяет, содержится ли указанное значение в контейнере.
     * <p>
     * Время поиска в среднем O(1), в худшем случае (при коллизиях) O(n).
     *
     * @param value значение для поиска
     * @return true, если значение найдено, false в противном случае
     */
    @Override
    public boolean contains(int value) {
        int index = hash(value);
        Node current = buckets[index];

        while (current != null) {
            if (current.value == value) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    /**
     * Удаляет указанное значение из контейнера.
     * <p>
     * Если значение не найдено, контейнер остается без изменений.
     *
     * @param value значение для удаления
     * @return true, если значение было найдено и удалено,
     *         false, если значение не найдено
     */
    @Override
    public boolean remove(int value) {
        int index = hash(value);
        Node current = buckets[index];
        Node prev = null;

        while (current != null) {
            if (current.value == value) {
                // Удаляем текущий узел
                if (prev == null) {
                    // Удаляем первый узел в списке
                    buckets[index] = current.next;
                } else {
                    // Удаляем не первый узел
                    prev.next = current.next;
                }
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    /**
     * Возвращает текущее количество элементов в контейнере.
     *
     * @return количество элементов (от 0 до максимального)
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Удаляет все элементы из контейнера.
     * <p>
     * После выполнения этого метода размер контейнера становится 0,
     * а емкость возвращается к {@value #DEFAULT_CAPACITY}.
     */
    @Override
    public void clear() {
        buckets = new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    /**
     * Возвращает строковое представление контейнера.
     * *
     * @return строковое представление всех элементов
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;

        for (Node node : buckets) {
            Node current = node;
            while (current != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(current.value);
                first = false;
                current = current.next;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Возвращает итератор для обхода элементов контейнера.
     * <p>
     * Порядок обхода соответствует порядку корзин и цепочек:
     * сначала все элементы из первой корзины, затем из второй и т.д.
     *
     * @return итератор для обхода элементов типа {@link Integer}
     */
    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {
            private int bucketIndex = 0;
            private Node currentNode = null;
            private Node nextNode = findNext();

            /**
             * Находит следующий не-null узел для итерации.
             *
             * @return следующий узел или null, если элементы закончились
             */
            private Node findNext() {
                if (currentNode != null && currentNode.next != null) {
                    return currentNode.next;
                }

                // Ищем следующую непустую корзину
                while (bucketIndex < buckets.length) {
                    if (buckets[bucketIndex] != null) {
                        return buckets[bucketIndex];
                    }
                    bucketIndex++;
                }
                return null;
            }

            @Override
            public boolean hasNext() {
                return nextNode != null;
            }

            @Override
            public Integer next() {
                if (!hasNext()) {
                    System.out.println(" symbol: class NoSuchElementException");
                }
                currentNode = nextNode;
                nextNode = findNext();
                return currentNode.value;
            }
        };
    }
}