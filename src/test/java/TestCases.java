import config.EndPoint;
import config.TestConfig;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.lang3.ObjectUtils;
import org.hamcrest.core.IsEqual;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.util.HashMap;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import io.restassured.response.Response;

import io.restassured.specification.RequestSpecification;
//import io.restassured.specification.ResponseSpecification;
//import io.restassured.builder.ResponseSpecBuilder;



public class TestCases {
    //This class created in order to preform Rest Assured Test cases
    //In order to test and give different tests scenarios in order to  test
    //the relations between 3 tables.
    //Users table , Posts Table , Comment table
    //Users --> Posts: Primary Key Users.ID --> Forgen Key: posts.UserID
    //Posts --> Commnets : Primary Key Posts.ID --> Forgen Key: Comments .PostID


   // public static RequestSpecification baseRequestSpec;
    public static RequestSpecification baseRequestSpec;

    private int expectedResponseSuccessStatus = 200;
    private int expectedResponseCreatedStatus = 201;
    private int expectedResponseServerErrorStatus = 505;
    private int expectedResponseNotFoundStatus = 404;



    //Expected Values
    private String defaultName = "Clementine Bauch";
    private String defaultUserName = "Samantha";
    private String defaultUserId = "3";
    private String expectedlongString = "Samantha very long long long long long long long long long long long long string";
    private String UnicodeNonUnicodeValues = "Samantha pr�s-*";
    private String expectedLanguageSupport = "Clementine Bauch 放心測試";
    private String JsonFormatDelimiters = "Clementine Bauch <> ! & * $ # @ ~ \\ {} \"";
    private String defaultEmail = "Nathan@yesenia.net";
    private String incorrectEmailFormat = "Nathan@kuku@net";
    //Posts
    private String postsDefaultTitle = "maxime id vitae nihil numquam";
    private String postsDefaultPostId = "23";
    private String postsDefaultBody = "veritatis unde neque eligendi\nquae quod architecto quo neque vitae\nest illo sit tempora doloremque fugit quod\net et vel beatae sequi ullam sed tenetur perspiciatis";
    private String postsDefaultUserId = "3";




    @BeforeTest
    public void setupBeforeTest(){
        baseRequestSpec = new RequestSpecBuilder()
                .setBaseUri("https://jsonplaceholder.typicode.com/")
                .addHeader("Content-Type","application/json")
                .addHeader("Server ", "cloudflare")
                .build();
        RestAssured.requestSpecification = baseRequestSpec;
    }

@Test
    //TC01: Verify username "Samantha" exists under all users (array #2) according to Json
    // Expected Result: Test should success username "Samantha" exists
    public void verifyUserNameSamanthaExists (){
        given()
                .spec(baseRequestSpec)
                //.queryParam("username", defaultUserName)
                .param("[2].username",defaultUserName)
                .get(EndPoint.USERS_PATH)
         .then()
                .assertThat()
                .statusCode(expectedResponseSuccessStatus)
                //.body("username", containsString(defaultUserName))
                .body("[2].username", IsEqual.equalTo("Samantha"));
                //.log().body();
    }
    @Test
    //TC02: Update (Patch or Put request) username with very long  string
    //Expected Result: Username should or shouldn't update or limited with number of characters
    public void updateUserNameSamanthaNameValues (){

        given()
                .spec(baseRequestSpec)
                .pathParam("userid",defaultUserId)
                .param("username",expectedlongString)
                .log().all()
        .when()
                .patch(EndPoint.USERS_ID_PATH)
        .then()
                .assertThat()
                .statusCode(expectedResponseSuccessStatus)
                .body("username", IsEqual.equalTo(defaultUserName))//verify name is not update
                .log().all();
         //If action support we should set back the test to default username.

        }
    //TC03: Update (Patch or Put request) username for id = 3 with unicode - non unicode characters
    //Expected Result: Username Field should update or failed with non unicode characters
    @Test
    public void userNameSupportNonUnicodeCharters () {
        given()
                .spec(baseRequestSpec)
                .pathParam("userid",defaultUserId)
                .param("username",UnicodeNonUnicodeValues)
                //.body(unicodeNonUnicodeValues)
                .log().all()
                .when()
                    .patch(EndPoint.USERS_ID_PATH)
                .then()
                .assertThat()
                .statusCode(expectedResponseSuccessStatus)
                .log().all()
                .body("username", IsEqual.equalTo(UnicodeNonUnicodeValues));
        //If action support we should set back the test to default username.
    }
    //TC04: Verify (Patch or Put request) name supported Languages like Chinese 放心測試
    //Expected Result: name Field should update with supported characters
    @Test
    public void userNameSupportAnyLanguage () {
        given()
                .spec(baseRequestSpec)
                .pathParam("userid",defaultUserId)
               // .body(LanguageSupport)
                .param("username",expectedLanguageSupport)
                .log().all()
        .when()
                .patch(EndPoint.USERS_ID_PATH)
        .then()
                .assertThat()
                .statusCode(expectedResponseSuccessStatus)
                .log().all()
                .body("username", IsEqual.equalTo(expectedLanguageSupport));
        //we sould support different types of Languages and assret that this update is supported and dont cause any crushes
        //If action support we should set back the test to default username.
    }

    //TC05: Verify username update (Patch or Put request) with Json format characters and symbol characters
    //Expected Result: Field should support son format characters and symbol characters
    @Test
    public void nameSupportJsonFormatDelimiters () {
      given()
              .spec(baseRequestSpec)
              .pathParam("userid",defaultUserId)
              .param("username",JsonFormatDelimiters)
              .log().all()
      .when()
                .patch(EndPoint.USERS_ID_PATH)
      .then()
               .assertThat()
               .statusCode(expectedResponseSuccessStatus)
               .log().all()
               .body("username", IsEqual.equalTo(JsonFormatDelimiters))
              //Name should be update or denied according to Spec but we need to observed it not break the API requests
              //If action support we should set back the test to default username.
         ;
    }

    //TC06: We should repeat those test cases for all user id #3 fields such as:
    // 1. Address
    // 2. Street
    // 3. Address
    // 4. Suit
    // 5. Company.name
    //Expected Result: Fields should support or un-support such fields according to system definition

    //TC007:Update (Patch or Put request) email for id = 3 with non support format
    //Expected Result: Field should not update with illegal format
    @Test
    public void verifyUpdateFailedForNonSupportEmailFormat() {
         given()
                .spec(baseRequestSpec)
                .pathParam("userid",defaultUserId)
                .param("email",incorrectEmailFormat)
                .log().all()
         .when()
                .patch(EndPoint.USERS_ID_PATH)
         .then()
                .assertThat()
                .statusCode(expectedResponseSuccessStatus)
                .log().all()
                .body("email", IsEqual.equalTo(defaultEmail));//field should not be change
    }
    //TC08: Verify delete action is supported or user can be delete and all data not saved in the DB
    //Expected Result: User should deleted all data should be deleted as well
    @Test
    public void deleteUserId() { //should be done
        given()
                .spec(baseRequestSpec)
                .pathParam("userid",defaultUserId)
              //  .body(deleteUserId)
        .when()
                .delete(EndPoint.USERS_ID_PATH)
        .then()
                .assertThat()
                .statusCode(expectedResponseSuccessStatus)
                .body("email", IsEqual.equalTo("")) //assret that values is deleted
                .log().all();
        //We now should inseart back the user name
    }
    @Test
    //TC09: Login: update (Put or Patch) name for users id = 3 with keywords that can hacked the account to DB via the service OWASP/XSS
    //Can be payment page , username page etc.
    //Expected Result: Databse data cannot be access
    public void checkAPISecureCodingViaUserNameField() {
        String XSSLoginValues = "{\n" +
                "   \"username\":\" </form> please login to continue <form action = +" +
                " method= get>"+
                "username ='user'"
                +"<password = 'password'>" +
                "submit value login </form> )"
                +"\n" +
                "}";
       //System.out.println(XSSLoginValues);
        given()
                .spec(baseRequestSpec)
                .pathParam("userid",defaultUserId)
                .param("username",XSSLoginValues)
                //.body(XSSLoginValues)
                .log().all()
        .when()
                .patch(EndPoint.USERS_ID_PATH)
        .then()
               .assertThat()
               .statusCode(expectedResponseNotFoundStatus)
               .log().all();
    }
    //TC10 Set all Values of user name to defaults
    //Expected Results: All values back to defaults

//POSTS Test Cases
    //We can repeat  all use case with relevant parameters as previous test cases
    //TC11 - Create new posts to userid #3 with very long title
    //Expected Result: Title should be limit or valid
@Test
    public void createNewPosts() {
        String createNewPost = "{\n" +
                "   \"userId\": \"3\",\n" +
                "   \"title\": \"Very long long Title Support...\",\n" +
                "   \"body\": \"bar\"\n" +
                "}";

    given()
            .spec(baseRequestSpec)
            .body(createNewPost)
            .log().all()
    .when()
            .post(EndPoint.POST_PATH)
    .then()
            .assertThat()
            .statusCode(expectedResponseCreatedStatus)
            .log().all()
            .body("title", IsEqual.equalTo("Very long long Title Support..."));
    }
    ///TC12 - create new posts to userid #3 with very unicode / non-unicode title body (should done on two differnt tests
    //expectec result: Action should success or failed according to the spec
    @Test
    public void createNewPostsUnicodeAndBodyUnicodeAndNonUniCode() {
        String createNewPost = "{\n" +
                "   \"Id\": \"3\",\n" +
                "   \"title\": \"UniCode Non-Unicode Char on the title Clementine Bauch 放心測試 Samantha pr�s-*\",\n" +
                "   \"body\": \"UniCode Non-Unicode Char on the body Clementine Bauch 放心測試 Samantha pr�s-*\"\n" +
                "}";
        given()
                .spec(baseRequestSpec)
                .body(createNewPost)
                .log().all()
                .when()
                .post(EndPoint.POST_PATH)
                .then()
                .log().all()
                .assertThat()
                .statusCode(expectedResponseCreatedStatus)
                .log().all()
                .body("title", containsString("UniCode Non-Unicode Char on the title Clementine Bauch 放心測試 Samantha pr�s-*"))
                .body("body", containsString("UniCode Non-Unicode Char on the title Clementine Bauch 放心測試 Samantha pr�s-*"))
        ;
    }
    //TC13 try to delete userId 1 and post #5 (userId=1&id=5) from the post messgae
    //Expected result: Action should success
    @Test
    public void deleteUserIdFromPosts() {
        given()
                .spec(baseRequestSpec)
                .pathParam("id",1)
                //.pathParam("id",4)
                //.body(deletePost)
                .log().all()
                .when()
                .delete(EndPoint.DELETE_USER_ID_PATH)
                .then()
                .assertThat()
                .statusCode(expectedResponseSuccessStatus)
                .log().all()
                //ID shouldn't exists
                .body("id", IsEqual.equalTo(null));
        //Assert all values are null or empry
    }
    //TC14: User try to create POST w/o title
    //Expected result: Action should failed.

    @Test
    public void createNewPostsWithoutTitle() {
        String createNewPostWithoutTitle = "{\n" +
                "   \"userId\": \"3\",\n" +
                "   \"body\": \"bar\"\n" +
                "}";
       given()
                .spec(baseRequestSpec)
                .body(createNewPostWithoutTitle)
                .log().all()
                .when()
                .post("/posts")
                .then()
                .assertThat()
                .statusCode(expectedResponseNotFoundStatus);
//Assert that action should be failed verify post not created
    }

    //TC15: User try to delete the title from the post
    //expected result: Test should failed
    @Test
    public void deleteTitleFromPosts() {
       given()
               .spec(baseRequestSpec)
                .log().all()
                .when()
                .delete("posts/?userId=1&id=5&title=\"nesciunt quas odio\"")
                .then()
                .assertThat()
                .statusCode(expectedResponseNotFoundStatus)
                .body("title", containsString("nesciunt quas odio"))//body should not  change
;
    }

//COMMENTS Test Cases
@Test
    //TC16: User create new comments with long name and support Unicode Texts in different fields related to post ID: 2 mix couple of test cases
    //Expectec result: Action should failed due to invalid mail format
    public void createNewCommentsWithLongText (){
        String createNewComments = "{\n" +
                "   \"id\": \"8\",\n" +
                "   \"name\": \"et omnis dolorem Long Long one......\",\n" +
                "   \"email\": \"Mallory_Kunze@marie!!!!!!org\",\n" +
                "   \"postId\": \"2\",\n" +
                "   \"body\": \"Samantha very long long long सौभाग्य है !@^%#&** : pr�s-*) string ..\",\n" +
              "}";
        given()
                .spec(baseRequestSpec)
                .body(createNewComments)
                .log().all()
                .when()
                    .post(EndPoint.COMMENTS_PATH).
                then()
                .assertThat()//Fields should be update
                .statusCode(expectedResponseNotFoundStatus)
                .body("name", containsString("et omnis dolorem")) //Should be failed beacuse cannot update the field //verify name is not update
                .body("email", containsString("Mallory_Kunze@marie@org")) ;//Should be failed beacuse cannot update the field //verify email is not update
    }


    //TC17: Now that we have relation we can try break the relations between the tables
    //Expected result:change the title of the post an observed comments are still exists
    //TBD

    //TC18:update users id and observed all data saved
    //Expected result: All data should be saved
    //TBD

    //TC18:Delete comments id
    //Expected result: Action should be failed
    //TBD


    //Additional test cases are:
    //Shutdown the service of posts down and create new comment
    //TBD
    //create /delete / edit  100, 1K, 5K , 10K post simultaneously
    //TBD
    //create /delete /edit 00, 1K, 5K , 10K comments simultaneously
    //TBD


}
