package webserver;

import db.Database;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.handler.UserCreateHandler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserCreateHandlerTest {

    private UserCreateHandler handler;
    @BeforeEach
    void setUp(){
        this.handler = new UserCreateHandler();
    }

    @DisplayName("요청에 맞는 정보를 지닌 User 객체가 DB에 저장되는 것을 확인")
    @Test
    void addUserInDatabaseTest() {
        String body = "username=javajigi&nickname=%EB%B0%95%EC%9E%AC%EC%84%B1&password=password&email=javajigi%40slipp.net";
        handler.addUserInDatabase(body);
        User actualUser = Database.findUserById("javajigi");
        User expectedUser = new User("javajigi", "password", "박재성", "javajigi@slipp.net");
        assertThat(actualUser.toString()).isEqualTo(expectedUser.toString());
    }

    @DisplayName("잘못된 이메일 형식을 입력하였을 때 IllegalArgument 예외를 발생시킨다")
    @Test
    void invalidEmailFormatTest() {
        String body = "username=javajigi&nickname=%EB%B0%95%EC%9E%AC%EC%84%B1&password=password&email=javajigislipp.net";
        assertThatThrownBy(() -> handler.addUserInDatabase(body))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(": 잘못된 이메일 형식입니다");
    }

    @DisplayName("잘못된 이름 형식을 입력하였을 때 IllegalArgument 예외를 발생시킨다")
    @Test
    void invalidNameFormatTest() {
        String body = "username=javajigi&nickname=jae..sung&password=password&email=javajigi%40slipp.net";
        assertThatThrownBy(() -> handler.addUserInDatabase(body))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(": 잘못된 이름 형식입니다");
    }
}