package studio.dboo.favores.modules.accounts;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import studio.dboo.favores.modules.groups.Group;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @AllArgsConstructor @NoArgsConstructor

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Account{

    @Id @GeneratedValue
    private Long id;

    /** Login Info **/
    @Column(unique = true) @NotNull
    private String username;
    @Column(unique = true) @NotNull @Email
    private String email;
    @NotNull
    private String password;
    private String role; //권한 (ADMIN, USER)

    /**Group Info*/
    // TODO - Many To Many 연관관계 매핑 -> Group

    /** Email Verification **/
    // TODO - Email Verification 구현하기
//    private boolean emailVerified = false;
//    private String emailCheckToken;
//    private LocalDateTime emailCheckTokenGeneratedAt;
//
//    public void generateEmailCheckToken() {
//        this.emailCheckToken = UUID.randomUUID().toString();
//        this.emailCheckTokenGeneratedAt = LocalDateTime.now();
//    }
//
//    public void completeSignUp() {
//        this.emailVerified = true;
//        this.createAt = LocalDateTime.now();
//    }

    /** Private Info **/
    @Pattern(regexp = "^\\\\d{2,3}-\\\\d{3,4}-\\\\d{4}$", message = "핸드폰 번호의 양식이 아닙니다.")
    private String cellPhone; // 핸드폰번호
    private String address; // 주소
    private String sex; // 성별
    private Integer age; // 나이

    /** Tier, Point **/
    private Integer tier; // 등급 : 1~5 blonze, silver, gold, platinum, diamond
    private Long point; // 포인트

    /** Logging Data Manipulation **/
    @CreationTimestamp
    private LocalDateTime createAt;
    @UpdateTimestamp
    private LocalDateTime modifyAt;
    private LocalDateTime droppedAt;

    /** Encrypt Password */
    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }
}
