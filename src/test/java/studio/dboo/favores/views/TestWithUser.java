package studio.dboo.favores.views;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Test
@WithMockUser(username = "dboo", roles = "USER")
public @interface TestWithUser {
}
