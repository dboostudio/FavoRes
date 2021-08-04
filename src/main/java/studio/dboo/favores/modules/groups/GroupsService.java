package studio.dboo.favores.modules.groups;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import studio.dboo.favores.modules.groups.entity.Groups;

@Service
@RequiredArgsConstructor
public class GroupsService {

    /** Bean */
    private final GroupsRepository groupsRepository;


    public Groups createGroups(Groups groups) {
        Groups savedGroup = groupsRepository.save(groups);
        return savedGroup;
    }
}
