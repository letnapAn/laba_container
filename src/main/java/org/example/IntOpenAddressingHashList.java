package org.example;
import java.util.Iterator;

/**
 * Одна из реализаций контейнера - хэш-таблица с открытой адресацией.
 * Для ситуаций, когда важна скорость поиска.
 * <p>
 * Особенности реализации:
 * <ul>
 *   <li>Хранит дубликаты</li>
 *   <li>Использует открытую адресацию для разрешения коллизий</li>
 *   <li>Линейное пробирование</li>
 *   <li>При заполненности более 80% автоматически увеличивает размер вдвое</li>
 * </ul>
 *
 * @see IntContainer
 * @version 1.0
 */
public class IntOpenAddressingHashList implements IntContainer {

    /** Начальная емкость контейнера по умолчанию */
    private static final int DEFAULT_CAPACITY = 16;
    private double LOAD_FACTOR = 0.8;
    /** Массив "корзин" (buckets) для хранения голов связных списков */
    private Node[] list;

    /** Текущее количество элементов в контейнере */
    private int size;

    /**
     * Внутренний класс, представляющий узел связного списка.
     * Каждый узел содержит значение и ссылку на следующий элемент в цепочке.
     */
    private enum NodeStatus {FREE, DELETED, OCCUPIED};
    private static class Node {
        /** Значение элемента */
        int value;
        /** Ссылка на следующий узел в цепочке (null для последнего элемента) */
        NodeStatus status;


        Node() {
            this.status = NodeStatus.FREE;
        }
        /**
         * Узел/клеточка/звено - для нового элемента списка
         *
         * @param value значение элемента
         */
        Node(int value) {
            this.value = value;
            this.status = NodeStatus.OCCUPIED;
        }
    }

    /**
     * Создает пустой контейнер с начальной емкостью {@value #DEFAULT_CAPACITY}.
     */
    public IntOpenAddressingHashList() {

        this.list = new Node[DEFAULT_CAPACITY];
        for (int i = 0; i < list.length; i++) {
            list[i] = new Node(); // Инициализируем каждую ячейку как FREE
        }
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
        return h % list.length;
    }

    /**
     * Увеличивает размер контейнера в два раза.
     * <p>
     * Вызывается автоматически, когда коэффициент заполненности превышает 0.8.
     * При увеличении все существующие элементы добавляются в новый массив с пересчетом их хэшей.
     */
    private void resize() {
        Node[] oldList = list;
        list = new Node[oldList.length * 2];
        for (int i = 0; i < list.length; i++) {
            list[i] = new Node(); // Инициализация
        }
        size = 0; // Сбрасываем, add() сам увеличит
        for (Node node : oldList) {
            if (node.status == NodeStatus.OCCUPIED) {
                add(node.value);
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
        // Проверяем необходимость увеличения размера
        if ((float) size / list.length > LOAD_FACTOR) {
            resize();
        }

        int index = hash(value);

        while ( this.list[index].status == NodeStatus.OCCUPIED) {
            index = (++index) % this.list.length;
        }

        // Добавляем новый узел в начало списка
        list[index].status = NodeStatus.OCCUPIED;
        list[index].value = value;
        ++size;


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
        boolean occupied = this.list[index].status == NodeStatus.OCCUPIED;
        boolean deleted = this.list[index].status == NodeStatus.DELETED;

        while (occupied || deleted) {
            if (occupied && this.list[index].value == value) {
                return true;
            }
            index = (++index) % this.list.length;

            occupied = this.list[index].status == NodeStatus.OCCUPIED;
            deleted = this.list[index].status == NodeStatus.DELETED;
        }
        return false;
    }

    /**
     * Удаляет первое вхождение значения.
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
        boolean occupied = this.list[index].status == NodeStatus.OCCUPIED;
        boolean deleted = this.list[index].status == NodeStatus.DELETED;

        while (occupied || deleted) {
            if (occupied && this.list[index].value == value) {
                this.list[index].status = NodeStatus.DELETED;
                --size;
                return true;
            }
            index = (++index) % this.list.length;

            occupied = this.list[index].status == NodeStatus.OCCUPIED;
            deleted = this.list[index].status == NodeStatus.DELETED;
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
        list = new Node[DEFAULT_CAPACITY];
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

        for (Node node : list) {
            if (node.status == NodeStatus.OCCUPIED) {
                sb.append(node.value);
                sb.append(", ");
            }

        }

        sb.append("]");
        return sb.toString();
    }

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {
            private int current = 0;

            @Override
            public boolean hasNext() {
                while (current < list.length &&
                        list[current].status != NodeStatus.OCCUPIED) {
                    current++;
                }
                return current < list.length;
            }

            @Override
            public Integer next() {
                if (!hasNext()) {
                    System.out.println("no elements");
                    return null;
                }
                return list[current++].value;
            }
        };
    }
}