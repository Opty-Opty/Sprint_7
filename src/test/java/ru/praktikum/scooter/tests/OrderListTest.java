package ru.praktikum.scooter.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.scooter.client.OrderClient;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;

public class OrderListTest {
    private OrderClient orderClient;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Проверка, что в тело ответа возвращается непустой список заказов")
    public void getOrdersListReturnsNotEmptyListTest() {
        orderClient.getOrders()
                .statusCode(SC_OK)
                .body("orders", not(empty()));
    }
}
