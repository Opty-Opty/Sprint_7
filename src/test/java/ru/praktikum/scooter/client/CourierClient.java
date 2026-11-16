package ru.praktikum.scooter.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.praktikum.scooter.model.Courier;
import ru.praktikum.scooter.model.CourierCredentials;

import static io.restassured.RestAssured.given;

public class CourierClient extends BaseHttpClient {
    private static final String COURIER_PATH = "api/v1/courier";
    private static final String LOGIN_PATH = "api/v1/courier/login";

    @Step("Создание курьера")
    public ValidatableResponse create(Courier courier) {
        return given()
                .spec(getBaseSpec())
                .body(courier)
                .when()
                .post(COURIER_PATH)
                .then();
    }

    @Step("Логин курьера в системе")
    public ValidatableResponse login(CourierCredentials credentials) {
        return given()
                .spec(getBaseSpec())
                .body(credentials)
                .when()
                .post(LOGIN_PATH)
                .then();
    }

    @Step("Удаление курьера по id: {id}")
    public ValidatableResponse delete(int id) {
        return given()
                .spec(getBaseSpec())
                .when()
                .delete(COURIER_PATH + "/" + id)
                .then();
    }
}
