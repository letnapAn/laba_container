package org.example;
import java.util.Iterator;
/**
     *  О том, что такое Conventional Commits только после 3 коммита узнала...
 */
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        IntContainer container = new IntHashSet();

        // Добавление элементов
        container.add(1);
        container.add(2);
        container.add(1); // Дубль
        container.add(3);
        container.add(Integer.MIN_VALUE); // Пограничное значение для проверки хэш-функции

        System.out.println("Size: " + container.size());

        // Поиск элементов
        System.out.println("Contains 1: " + container.contains(1));
        System.out.println("Contains 7: " + container.contains(7));
        System.out.println("Contains MIN_VALUE: " + container.contains(Integer.MIN_VALUE));

        // Удаление элементов
        container.remove(1);
        container.remove(Integer.MIN_VALUE);
        System.out.println("Contains 1 after remove: " + container.contains(1));
        System.out.println("Contains MIN_VALUE after remove: " + container.contains(Integer.MIN_VALUE));
        System.out.println("Size after remove: " + container.size());

        // Очистка
        container.clear();
        System.out.println("Size after clear: " + container.size());



        IntContainer containerList = new IntOpenAddressingHashList();

        // Добавление элементов
        containerList.add(1);
        containerList.add(2);
        containerList.add(1); // Дубль
        containerList.add(3);
        containerList.add(Integer.MIN_VALUE); // Пограничное значение для проверки хэш-функции

        System.out.println("Size: " + containerList.size());

        // Поиск элементов
        System.out.println("Contains 1: " + containerList.contains(1));
        System.out.println("Contains 7: " + containerList.contains(7));
        System.out.println("Contains MIN_VALUE: " + containerList.contains(Integer.MIN_VALUE));

        // Удаление элементов
        containerList.remove(1);
        containerList.remove(Integer.MIN_VALUE);
        System.out.println("Contains 1 after remove: " + containerList.contains(1));
        System.out.println("Contains MIN_VALUE after remove: " + containerList.contains(Integer.MIN_VALUE));
        System.out.println("Size after remove: " + containerList.size());

        // Очистка
        containerList.clear();
        System.out.println("Size after clear: " + containerList.size());
    }
}