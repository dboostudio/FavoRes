package studio.dboo.favores.modules.groups;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity @Getter @Setter
@EqualsAndHashCode(of = "id")
@Builder @AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Group {

    @Id @GeneratedValue
    private Long id;

    //TODO - Group 정보 생성
}
