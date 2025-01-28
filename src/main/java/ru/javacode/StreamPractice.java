package ru.javacode;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StreamPractice {
    /**
     * Создайте список заказов с разными продуктами и их стоимостями.
     * 1. Группируйте заказы по продуктам.
     * 2. Для каждого продукта найдите общую стоимость всех заказов.
     * 3. Отсортируйте продукты по убыванию общей стоимости.
     * 4. Выберите три самых дорогих продукта.
     * 5. Выведите результат: список трех самых дорогих продуктов и их общая стоимость.
     */
    public static void main(String[] args) {
        List<Order> orders = List.of(
                new Order("Laptop", 1200.0),
                new Order("Smartphone", 800.0),
                new Order("Laptop", 1500.0),
                new Order("Tablet", 500.0),
                new Order("Smartphone", 900.0)
        );

        orders.stream()
                .collect(Collectors.toMap(Order::getProduct, Order::getCost, Double::sum))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(3)
                .forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));
    }

    private static class Order {
        private String product;

        private double cost;

        public Order(String product, double cost) {
            this.product = product;
            this.cost = cost;
        }

        public String getProduct() {
            return product;
        }

        public double getCost() {
            return cost;
        }
    }
}
