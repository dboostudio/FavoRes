package studio.dboo.favores.modules.accounts.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @AllArgsConstructor @NoArgsConstructor

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Account{

    final static String ENTER_USERNAME = "아이디를 입력해 주세요.";
    final static String USERNAME_LENGTH = "아이디는 4자 이상, 16자 이하여야 합니다.";
    final static String ENTER_PASSWORD = "비밀번호를 입력해 주세요.";
    final static String CELLPHONE_FORM_NOT_CORRECT = "010-xxxx-xxxx 의 형식에 맞춰서 입력해주세요.";


    @Id @GeneratedValue
    private Long id;

    /** Login Info **/
    @Column(unique = true)
    @Size(min=4, max=16, message = USERNAME_LENGTH) @NotBlank(message = ENTER_USERNAME)
    private String username;                //사용자 아이디

    @NotNull(message = ENTER_PASSWORD) @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @JsonIgnore
    private String role; //권한 (ADMIN, USER)

    /**Groups Mapping*/
    @OneToMany(mappedBy = "groups")
    @Builder.Default //Test시 Warning의 추천에 따라 붙임. Builder패턴에 적용되지 않도록 하는 어노테이션인것으로 추정됨
    private Set<AccountGroups> groups = new HashSet<>();

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
    @Nullable @Pattern(regexp = "^\\\\d{2,3}-\\\\d{3,4}-\\\\d{4}$", message = CELLPHONE_FORM_NOT_CORRECT)
    private String cellPhone;               // 핸드폰번호
    @Email private String email;            // 사용자 이메일
    private String firstname;               // 성
    private String lastname;                // 이름
    private String birth;                   // 생년월일
    private String address;                 // 주소
    private String sex;                     // 성별

    /** Tier, Point **/
    private Integer tier;                   // 등급 : 1~5 blonze, silver, gold, platinum, diamond
    private Long point;                     // 포인트

    /** Logging Data Manipulation **/
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime modifiedAt;
    private LocalDateTime droppedAt;

    /** Encrypt Password */
    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }
}
