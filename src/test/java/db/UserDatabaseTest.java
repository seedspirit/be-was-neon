package db;

import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserDatabaseTest {
    @BeforeEach
    void setupDatabase(){
        User userOne = new User("asdf", "userOne", "userOne", "asdf@asdf.com");
        User userTwo = new User("qwer", "userTwo", "userTwo", "qwer@qwer.com");
        UserDatabase.addUser(userOne);
        UserDatabase.addUser(userTwo);
    }

    @DisplayName("모든 User 객체의 userId, name을 회원가입 순서의 표 형태 나타내는 html을 만들 수 있다")
    @Test
    public void generateUserInfoTableTest() {
        String expected =
                """
                        <tr>
                        <td>asdf</td>
                        <td>userOne</td>
                        </tr>
                        <tr>
                        <td>qwer</td>
                        <td>userTwo</td>
                        </tr>
                        """;

        String actual = UserDatabase.generateUserInfoTable();
        assertThat(actual).isEqualTo(expected);
    }
}
