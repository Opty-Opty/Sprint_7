package ru.praktikum.scooter.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.praktikum.scooter.client.OrderClient;
import ru.praktikum.scooter.model.Order;
import ru.praktikum.scooter.util.TestDataGenerator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreateTest {
    private final List<String> color;
    private OrderClient orderClient;
    private Integer track;

    public OrderCreateTest(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters(name = "Цвета самоката: {0}")
    public static Object[][] getColorData() {
        return new Object[][]{
                {Collections.singletonList("BLACK")},
                {Collections.singletonList("GREY")},
                {Arrays.asList("BLACK", "GREY")},
                {Collections.emptyList()},
                {null}
        };
    }

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @After
    public void tearDown() {
        if (track != null) {
            orderClient.cancel(track);
        }
    }

    @Test
    @DisplayName("Создание заказа с различными вариантами цвета")
    @Description("Проверка создания заказа с цветами BLACK, GREY, обоими цветами и без цвета")
    public void createOrderWithDifferentColorsTest() {
        Order order = TestDataGenerator.getRandomOrder(color);

        track = orderClient.create(order)
                .statusCode(SC_CREATED)
                .body("track", notNullValue())
                .extract().path("track");
    }
}
