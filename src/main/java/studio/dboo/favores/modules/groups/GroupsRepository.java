package studio.dboo.favores.modules.groups;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupsRepository extends JpaRepository<Groups, Long> {
    Groups findGroupsByGroupName(String groupName);
    boolean existsGroupsByGroupName(String groupName);
}
