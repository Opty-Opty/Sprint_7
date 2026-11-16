package ru.praktikum.scooter.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.scooter.client.CourierClient;
import ru.praktikum.scooter.model.Courier;
import ru.praktikum.scooter.model.CourierCredentials;
import ru.praktikum.scooter.util.TestDataGenerator;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierLoginTest {
    private CourierClient courierClient;
    private Courier courier;
    private Integer courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = TestDataGenerator.getRandomCourier();
        courierClient.create(courier);
    }

    @After
    public void tearDown() {
        if (courierId == null) {
            courierId = courierClient.login(CourierCredentials.from(courier))
                    .extract().path("id");
        }
        if (courierId != null) {
            courierClient.delete(courierId);
        }
    }

    @Test
    @DisplayName("Успешный логин курьера")
    @Description("Проверка авторизации с валидными данными и получения id в ответе")
    public void courierCanLoginTest() {
        courierId = courierClient.login(CourierCredentials.from(courier))
                .statusCode(SC_OK)
                .body("id", notNullValue())
                .extract().path("id");
    }

    @Test
    @DisplayName("Логин без указания логина")
    @Description("Проверка ошибки 400, если не передано поле login")
    public void loginWithoutLoginFieldFailsTest() {
        CourierCredentials credentials = new CourierCredentials(null, courier.getPassword());
        courierClient.login(credentials)
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Логин без указания пароля")
    @Description("Проверка ошибки 400, если не передано поле password")
    public void loginWithoutPasswordFieldFailsTest() {
        CourierCredentials credentials = new CourierCredentials(courier.getLogin(), "");
        courierClient.login(credentials)
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Логин с неверным паролем")
    @Description("Проверка ошибки 404 при вводе неправильного пароля")
    public void loginWithWrongPasswordFailsTest() {
        CourierCredentials credentials = new CourierCredentials(
                courier.getLogin(),
                TestDataGenerator.getRandomString(10)
        );
        courierClient.login(credentials)
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Логин под несуществующим пользователем")
    @Description("Проверка ошибки 404 при попытке входа с несуществующим логином")
    public void loginWithNonExistentUserFailsTest() {
        CourierCredentials credentials = new CourierCredentials(
                TestDataGenerator.getRandomString(12),
                TestDataGenerator.getRandomString(10)
        );
        courierClient.login(credentials)
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }
}
