package ru.praktikum.scooter.util;

import ru.praktikum.scooter.model.Courier;
import ru.praktikum.scooter.model.Order;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class TestDataGenerator {
    private static final Random RANDOM = new Random();

    public static String getRandomString(int length) {
        return UUID.randomUUID().toString().replace("-", "").substring(0, length);
    }

    public static Courier getRandomCourier() {
        return new Courier(
                "courier_" + getRandomString(8),
                "pass_" + getRandomString(8),
                "Name_" + getRandomString(6)
        );
    }

    public static Order getRandomOrder(List<String> colors) {
        return new Order(
                "First_" + getRandomString(6),
                "Last_" + getRandomString(6),
                "Street " + (RANDOM.nextInt(100) + 1),
                RANDOM.nextInt(20) + 1,
                "+79" + (100000000 + RANDOM.nextInt(900000000)),
                RANDOM.nextInt(7) + 1,
                LocalDate.now().plusDays(RANDOM.nextInt(10) + 1).toString(),
                "Comment_" + getRandomString(8),
                colors
        );
    }
}
