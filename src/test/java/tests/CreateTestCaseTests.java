package tests;

import com.codeborne.selenide.Condition;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import models.CreateTestCaseBody;
import models.CreateTestCaseResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import com.codeborne.selenide.Configuration;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

public class CreateTestCaseTests {
    static String login = "allure8",
                  password = "allure8",
                  projectId= "3506";

    @BeforeAll
    static void setUp() {
        Configuration.baseUrl = "https://allure.autotests.cloud";
        Configuration.holdBrowserOpen = true;
        Configuration.pageLoadStrategy = "eager";

        RestAssured.baseURI = "https://allure.autotests.cloud";

    }

    @Test
    void createWithApiOnlyTest() {
        Faker faker = new Faker();
        String testCaseName = faker.name().fullName();

//        step("Authorize" , () ->{
//            open("/login");
//            $(byName("username")).setValue(login);
//            $(byName("password")).setValue(password);
//            $("button[type='submit']").shouldBe(Condition.visible).click();
//            sleep(6000);
//
//
//
//        });
//        step("Go To Project" , () ->{
//            open("/project/3506/test-cases");
//
//
//        });
        step("Create Testcase" , () ->{

            CreateTestCaseBody testCaseBody = new CreateTestCaseBody();
            testCaseBody.setName(testCaseName);

            given()
                    .log().all()
                    .header("X-XSRF-TOKEN", "900dc31e-06bb-4ed5-88b9-8580b82992ba")
                    .cookies("XSRF-TOKEN","900dc31e-06bb-4ed5-88b9-8580b82992ba",
                            "ALLURE_TESTOPS_SESSION","d3214ba6-ca29-48e5-9485-e9d7c95ec3dd")
                    .body(testCaseBody)
                    .contentType(JSON)
                    .queryParam("projectId",projectId)
                    .when()
                    .post("/api/rs/testcasetree/leaf")
                    .then()
                    .log().status()
                    .log().body()
                    .statusCode(200)
                    .body("statusName", is("Draft"))
                    .body("name", is(testCaseName));



//            $("[data-testid=input__create_test_case]").setValue(testCaseName)
//                    .pressEnter();


        });
//        step("Verify testcase name" , () ->{
//            $(".LoadableTree__view").shouldHave(Condition.text(testCaseName));
//
//        });

    }

    @Test
    void createWithApiAndUiTest() {
        Faker faker = new Faker();
        String testCaseName = faker.name().fullName();

        step("Authorize");


            CreateTestCaseBody testCaseBody = new CreateTestCaseBody();
            testCaseBody.setName(testCaseName);

        CreateTestCaseResponse createTestCaseResponse = step("Create Testcase", () ->

                given()
                        .log().all()
                        .header("X-XSRF-TOKEN", "900dc31e-06bb-4ed5-88b9-8580b82992ba")
                        .cookies("XSRF-TOKEN", "900dc31e-06bb-4ed5-88b9-8580b82992ba",
                                "ALLURE_TESTOPS_SESSION", "d3214ba6-ca29-48e5-9485-e9d7c95ec3dd")
                        .body(testCaseBody)
                        .contentType(JSON)
                        .queryParam("projectId", projectId)
                        .when()
                        .post("/api/rs/testcasetree/leaf")
                        .then()
                        .log().status()
                        .log().body()
                        .statusCode(200)
                        .extract().as(CreateTestCaseResponse.class) );

        step("Verify testcase name" , () ->
            assertThat(createTestCaseResponse.getName()).isEqualTo(testCaseName));
    }

    @Test
    void createWithApiAndUiTestTwo() {
        Faker faker = new Faker();
        String testCaseName = faker.name().fullName();

        step("Authorize");


        CreateTestCaseBody testCaseBody = new CreateTestCaseBody();
        testCaseBody.setName(testCaseName);

        CreateTestCaseResponse createTestCaseResponse = step("Create Testcase", () ->

                given()
                        .log().all()
                        .header("X-XSRF-TOKEN", "900dc31e-06bb-4ed5-88b9-8580b82992ba")
                        .cookies("XSRF-TOKEN", "900dc31e-06bb-4ed5-88b9-8580b82992ba",
                                "ALLURE_TESTOPS_SESSION", "9bee7757-99ee-4250-813f-8e6004bd9c3d")
                        .body(testCaseBody)
                        .contentType(JSON)
                        .queryParam("projectId", projectId)
                        .when()
                        .post("/api/rs/testcasetree/leaf")
                        .then()
                        .log().status()
                        .log().body()
                        .statusCode(200)
                        .extract().as(CreateTestCaseResponse.class) );

        step("Verify testcase name" , () -> {
            open("/favicon.ico");

                    Cookie authorizationCookie = new Cookie("ALLURE_TESTOPS_SESSION","9bee7757-99ee-4250-813f-8e6004bd9c3d");
                    getWebDriver().manage().addCookie(authorizationCookie);
                    Integer testCaseId = createTestCaseResponse.getId();

                    String testCaseUrl = format("/project/%s/test-cases/%s",projectId, testCaseId);
                    open(testCaseUrl);

                    $(".TestCaseLayout__name").shouldHave(Condition.text(testCaseName));
                });
    }
}


