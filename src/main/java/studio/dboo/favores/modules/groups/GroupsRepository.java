package studio.dboo.favores.modules.groups;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import studio.dboo.favores.modules.groups.entity.Groups;

@Transactional
public interface GroupsRepository extends JpaRepository<Groups, Long> {


}
