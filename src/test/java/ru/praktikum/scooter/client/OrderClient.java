package ru.praktikum.scooter.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.praktikum.scooter.model.Order;

import static io.restassured.RestAssured.given;

public class OrderClient extends BaseHttpClient {
    private static final String ORDERS_PATH = "api/v1/orders";
    private static final String CANCEL_ORDER_PATH = "api/v1/orders/cancel";

    @Step("Создание заказа")
    public ValidatableResponse create(Order order) {
        return given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post(ORDERS_PATH)
                .then();
    }

    @Step("Получение списка заказов")
    public ValidatableResponse getOrders() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(ORDERS_PATH)
                .then();
    }

    @Step("Отмена заказа по треку: {track}")
    public ValidatableResponse cancel(int track) {
        return given()
                .spec(getBaseSpec())
                .queryParam("track", track)
                .when()
                .put(CANCEL_ORDER_PATH)
                .then();
    }
}
