package studio.dboo.portfolio.services.account;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @AllArgsConstructor @NoArgsConstructor
public class Account {

    @Id @GeneratedValue
    private Long id;

    /** Login Info **/
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String password;

    /** Email Verification **/
    private boolean emailVerified = false;
    private String emailCheckToken;
    private LocalDateTime emailCheckTokenGeneratedAt;

    public void generateEmailCheckToken() {
        this.emailCheckToken = UUID.randomUUID().toString();
        this.emailCheckTokenGeneratedAt = LocalDateTime.now();
    }

    public void completeSignUp() {
        this.emailVerified = true;
        this.createAt = LocalDateTime.now();
    }

    /** Personal Info **/
    @Column(unique = true)
    private String nickname;
    private String address;
    private String sex;

    /** Tier, Point **/
    private Integer tier; // 1~5 blonze, silver, gold, platinum, diamond
    private Long point;

    /** Logging Data Manipulation **/
    private LocalDateTime createAt;
    private LocalDateTime modifyAt;
    private LocalDateTime droppedAt;

}
