import config.EndPoint;
import config.TestConfig;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
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

    private String expectedlongString = "Samantha very long long long long long long long long long long long long string";
    private String expectedUnicodeNonUnicodeValues = "Samantha pr�s-*";
    private String expectedLanguageSupport = "Clementine Bauch 放心測試";
    private String expectedJsonFormatDelimiters = "Clementine Bauch <> ! & * $ # @ ~ \\ {} \"";
    private String expectedDefaultEmail = "Nathan@yesenia.net";



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
                .queryParam("username", defaultUserName)

                .get(EndPoint.USERS_PATH)
         .then()
                .assertThat()
                .statusCode(expectedResponseSuccessStatus)
                .body("username", containsString(defaultUserName))
                .log().all();
    }

    @Test
    //TC02: Update (Patch or Put request) username with very long  string
    //Expected Result: Username should be update or limited with number of characters
    public void updateUserNameSamanthaNameValues (){

        String longString = "{\n" +
                "   \"username\": \"Samantha very long long long long long long long long long long long long string \"\n" +
                "}";

        given()
                .spec(baseRequestSpec)
                .pathParam("userid",3)
                .body(longString)
                .log().all()
        .when()
                .put(EndPoint.USERS_ID_PATH)
        .then()
                //.spec(successResponse)
                .assertThat()
                .statusCode(expectedResponseSuccessStatus)
                .body("username", containsString(expectedlongString))
                .log().all();//username should be Samantha1
         //Now we should back the test to default parameters.
        }


    //TC03: Update (Patch or Put request) username for id = 3 with unicode - non unicode characters
    //Expected Result: Username Field should update or failed with non unicode characters
    @Test
    public void userNameSupportNonUnicodeCharters () {

        String unicodeNonUnicodeValues = "{\n" +
                "   \"username\": \"Samantha pr�s-* \"\n" +
                "}";

        given()
                .spec(baseRequestSpec)
                .pathParam("userid",3)
                .body(unicodeNonUnicodeValues)
                .log().all()
                .when()
                    .patch(EndPoint.USERS_ID_PATH)
                .then()
                .assertThat()
                .statusCode(expectedResponseSuccessStatus)
                .log().all()
                .body("username", containsString(expectedUnicodeNonUnicodeValues));
    }
    //TC04: Verify (Patch or Put request) name supported AnyLanguage like Chinese 放心測試
    //Expected Result: name Field should update with supported characters
    @Test
    public void userNameSupportAnyLanguage () {

        String LanguageSupport = "{\n" +
                "   \"username\": \"Clementine Bauch 放心測試 \"\n" +
                "}";

        given()
                .spec(baseRequestSpec)
                .pathParam("userid",3)
                .body(LanguageSupport)
                .log().all()
        .when()
                .patch(EndPoint.USERS_ID_PATH)
        .then()
                .assertThat()
                .statusCode(expectedResponseSuccessStatus)
                .log().all()
                .body("username", containsString(expectedLanguageSupport));
    }

    //TC05: Verify username update (Patch or Put request) with Json format characters and symbol characters
    //Expected Result: Field should support son format characters and symbol characters
    @Test
    public void nameSupportJsonFormatDelimiters () {

        String JsonFormatDelimiters = "{\n" +
              //  "   \"username\": \"Clementine Bauch <> ! & * $ # @ ~ \\ {} \" \"\n" +
                "   \"username\": \"Clementine Bauch \"\n" +
                "}";
//        System.out.println(JsonFormatDelimiters);


      given()
              .spec(baseRequestSpec)
              .pathParam("userid",3)
              .body(JsonFormatDelimiters)
               .when()
                .patch(EndPoint.USERS_ID_PATH)
              .then()
                    .assertThat()
              .statusCode(expectedResponseSuccessStatus)
                    .log().all()
                    .body("name", IsEqual.equalTo(expectedJsonFormatDelimiters)) //name should be update
         ;
    }

    //TC06: We should repeat those test cases for all user id #3 fields such as address.street , address.suit  company.name, company.catchPhrase etc.
    //Expected Result: Fields should support or unsupport such fields according to system defination

    //TC007:Update (Patch or Put request) email for id = 3 with non support format
    //Expected Result: Field should not update with illegal format
    @Test
    public void verifyUpdateFailedForNonSupportEmailFormat() {
        String nonSupportEmailFormat = "{\n" +
                "   \"email\": \"nonsupport.emailformat.com \" \n" +
                "}";
       // System.out.println(nonSupportEmailFormat);
        given()
                .spec(baseRequestSpec)
                .pathParam("userid",3)
                .body(nonSupportEmailFormat)
                .when()
                .patch(EndPoint.USERS_ID_PATH)
                .then()
                .assertThat()
                .statusCode(expectedResponseSuccessStatus)
                .log().all()
                .body("email", containsString(expectedDefaultEmail));//field should not be change
    }



    //TC08: Verify delete action is supported or user can be delete and all data not saved in the DB
    //Expected Result: User should deleted all data should be deleted as well
    @Test
    public void deleteUserId() { //should be done
        String b = "{\n" +
                "   \"username\": \"Clementine Bauch \"\n" +
                "}";
        given()
                .spec(baseRequestSpec)
                .pathParam("userid",3)
              //  .body(deleteUserId)
                .when()
                .delete(EndPoint.USERS_ID_PATH)
                .then()
                .assertThat()
                .statusCode(expectedResponseSuccessStatus)
                .log().all();
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
        System.out.println(XSSLoginValues);
        given()
                .spec(baseRequestSpec)
                .pathParam("userid",3)
                .body(XSSLoginValues)
                .when()
                .patch(EndPoint.USERS_ID_PATH)
              .then()
               //.contentType(ContentType.JSON)
              .assertThat()
                .statusCode(expectedResponseNotFoundStatus)
                .log().all();
    }

    //TC10 Back all values to default
    //Expected Results: All values back to defaults

//POSTS Test Cases

    //TC11 - Create new posts to userid #3 with very long title
    //Expected Result: Title should be limit
@Test
    public void createNewPosts() {
        String createNewPost = "{\n" +
                "   \"userId\": \"3\",\n" +
                "   \"title\": \"Very long long Title Support...\",\n" +
                "   \"body\": \"bar\"\n" +
                "}";

        System.out.println(createNewPost);

        given()
                .spec(baseRequestSpec)
               // .pathParam("userid",3)
                .body(createNewPost)
                .log().all()
                .when()
                    .post(EndPoint.POST_PATH)
                .then()
                .log().all()
                .assertThat()
                .statusCode(expectedResponseNotFoundStatus)
                .log().all()
                .body("title", containsString("Very long long Title Support..."))
        ;//field should not be change
    }


    ///TC12 - create new posts to userid #3 with very unicode / non-unicode title body (should done on two differnt tests
    //expectec result: Action should success or failed according to the spec
    @Test
    public void createNewPostsUnicodeAndBodyUnicodeAndNonUniCode() {
        String createNewPost = "{\n" +
                "   \"userId\": \"3\",\n" +
                "   \"title\": \"UniCode-Non Unicode Char on the title Clementine Bauch 放心測試 Samantha pr�s-*\",\n" +
                "   \"body\": \"UniCode-Non Unicode Char on the body Clementine Bauch 放心測試 Samantha pr�s-*\"\n" +
                "}";

        //System.out.println(createNewPost);

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
                .body("title", containsString("UniCode-Non Unicode Char on the title Clementine Bauch 放心測試 Samantha pr�s-*"))
                .body("body", containsString("UniCode-Non Unicode Char on the title Clementine Bauch 放心測試 Samantha pr�s-*"))
        ;//fields should not be change
    }
    //TC13 try to delete userId 1 and post #5 (userId=1&id=5) from the post messgae
    //Expected result: action should success
    @Test
    public void deleteUserIdFromPosts() {
      //  String deletePost = "{\n" +
     //           "   \"postId\": \"1\",\n" +
       //         "   \"id\": \"5\",\n" +
       //         "}";
        given()
                .spec(baseRequestSpec)
                .pathParam("id",1)
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
                //ID shouldnt exists
                .body("id", containsString("quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto"));

    }

    //TC14: User try to create POST w/o title
    //Expected result: Action should failed.

    @Test
    public void createNewPostsWithoutTitle() {
        String b = "{\n" +
                "   \"userId\": \"3\",\n" +
                "   \"body\": \"bar\"\n" +
                "}";

        System.out.println(b);

       given()
                .spec(baseRequestSpec)
                .body(b)
                .log().all()
                .when()
                .post("/posts")
                .then()
                .assertThat()
                .statusCode(expectedResponseNotFoundStatus);

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
                .body("title", containsString("nesciunt quas odio"))//body dont change
;
    }


//COMMENTS
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
    //take the service of posts down and create new commnets
    //create /delete / edit  100, 1K, 5K , 10K post simusutensly
    //create /delete /edit 00, 1K, 5K , 10K comments simusutensly


}
