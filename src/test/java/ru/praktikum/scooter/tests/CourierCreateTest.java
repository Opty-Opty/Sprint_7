package ru.praktikum.scooter.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.scooter.client.CourierClient;
import ru.praktikum.scooter.model.Courier;
import ru.praktikum.scooter.model.CourierCredentials;
import ru.praktikum.scooter.util.TestDataGenerator;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class CourierCreateTest {
    private CourierClient courierClient;
    private Courier courier;
    private Integer courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = TestDataGenerator.getRandomCourier();
    }

    @After
    public void tearDown() {
        if (courierId != null) {
            courierClient.delete(courierId);
        }
    }

    @Test
    @DisplayName("Успешное создание курьера")
    @Description("Проверка, что курьера можно создать со всеми обязательными полями, возвращается 201 и ok: true")
    public void courierCanBeCreatedTest() {
        ValidatableResponse createResponse = courierClient.create(courier);
        createResponse.statusCode(SC_CREATED)
                .body("ok", equalTo(true));

        courierId = courierClient.login(CourierCredentials.from(courier))
                .extract().path("id");
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    @Description("Проверка ошибки 409 при повторном создании курьера с тем же логином")
    public void cannotCreateDuplicateCourierTest() {
        courierClient.create(courier).statusCode(SC_CREATED);
        courierId = courierClient.login(CourierCredentials.from(courier))
                .extract().path("id");

        courierClient.create(courier)
                .statusCode(SC_CONFLICT)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Создание курьера без логина")
    @Description("Проверка ошибки 400, если не передано обязательное поле login")
    public void cannotCreateCourierWithoutLoginTest() {
        courier.setLogin(null);
        courierClient.create(courier)
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    @Description("Проверка ошибки 400, если не передано обязательное поле password")
    public void cannotCreateCourierWithoutPasswordTest() {
        courier.setPassword(null);
        courierClient.create(courier)
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера с пустым логином")
    @Description("Проверка ошибки 400 при передаче пустой строки в поле login")
    public void cannotCreateCourierWithEmptyLoginTest() {
        courier.setLogin("");
        courierClient.create(courier)
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}
