package webserver.httpMessage.httpRequest;

import db.SessionDatabase;
import db.UserDatabase;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import webserver.httpMessage.httpRequest.body.FormBody;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FormBodyTest {

    @BeforeEach
    void clearDatabases(){
        SessionDatabase.clearDatabase();
        UserDatabase.clearDatabase();
    }

    @DisplayName("Body에 byte 형태로 들어온 form 입력을 userId, Password, UserName, UserEmail 형태로 파싱하여 반환할 수 있다")
    @Test
    void parsingFormBodyTest(){
        String bodyStringExample =
                "username=javajigi&nickname=%EB%B0%95%EC%9E%AC%EC%84%B1&password=password&email=javajigi%40slipp.net";
        FormBody formBody = new FormBody(bodyStringExample.getBytes());

        assertThat(formBody.getUserId()).isEqualTo("javajigi");
        assertThat(formBody.getPassword()).isEqualTo("password");
        assertThat(formBody.getUserEmail()).isEqualTo("javajigi@slipp.net");
        assertThat(formBody.getUserName()).isEqualTo("박재성");
    }

    @DisplayName("이미 존재하는 userId를 입력했을 경우 IllegalArgumentException을 반환한다")
    @Test
    void checkUserIdDuplicatedTest(){
        User user = new User("javajigi", "password", "박재성", "javajigi@slipp.net");
        UserDatabase.addUser(user);
        String bodyStringExample =
                "username=javajigi&nickname=%EB%B0%95%EC%9E%AC%EC%84%B1&password=password&email=javajigi%40slipp.net";
        FormBody formBody = new FormBody(bodyStringExample.getBytes());
        assertThatThrownBy(formBody::checkUserIdDuplicated).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Body에 잘못된 유저이름 형식이 들어왔을 때 IllegalArgumentException을 반환한다")
    @Test
    void checkUserNameFormatValidTest(){
        String bodyStringExample =
                "username=javajigi&nickname=%EB%B0%95%EC%9E%AC%EC%84%B1/////&password=password&email=javajigi%40slipp.net";
        FormBody formBody = new FormBody(bodyStringExample.getBytes());
        assertThatThrownBy(formBody::checkUserNameFormatValid)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(": 잘못된 이름 형식입니다");
    }

    @DisplayName("Body에 잘못된 이메일 형식이 들어왔을 때 IllegalArgumentException을 반환한다")
    @Test
    void checkUserEmailFormatValidTest(){
        String bodyStringExample =
                "username=javajigi&nickname=%EB%B0%95%EC%9E%AC%EC%84%B1&password=password&email=javajigislipp.net";
        FormBody formBody = new FormBody(bodyStringExample.getBytes());
        assertThatThrownBy(formBody::checkUserEmailFormatValid)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(": 잘못된 이메일 형식입니다");
    }

    @DisplayName("어떤 User 객체의 비밀번호와 Body에 입력된 비밀번호가 다를 경우 false를 반환한다")
    @Test
    void passwordInputCorrespondPasswordOfTest(){
        String bodyStringExample = "username=javajigi&password=pass";
        FormBody formBody = new FormBody(bodyStringExample.getBytes());
        User user = new User("javajigi", "password", "박재성", "e@e.com");
        assertThat(formBody.passwordInputCorrespondPasswordOf(user)).isFalse();
    }

    @DisplayName("UserDatabase에 없는 User의 userId를 가지고 있을 경우 false를 반환한다")
    @Test
    void userIdExistsInDBTest(){
        String bodyStringExample = "username=java&password=password";
        FormBody formBody = new FormBody(bodyStringExample.getBytes());
        User user = new User("javajigi", "password", "박재성", "e@e.com");
        UserDatabase.addUser(user);
        assertThat(formBody.userIdExistsInDB()).isFalse();
    }
}
