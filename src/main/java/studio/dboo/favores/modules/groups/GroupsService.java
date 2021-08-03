package studio.dboo.favores.modules.groups;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupsService {

    private final GroupsRepository groupsRepository;

    public Groups createGroup(Groups groups) {
        if(groupsRepository.existsGroupsByGroupName(groups.getGroupName())){
            throw new RuntimeException("해당하는 그룹명이 이미 존재합니다.");
        }
//        groups.setGroupLeader(
//              TODO- createGroup을 한 유저를 그룹리더로 자동선정 할 것.
//        );
        groupsRepository.save(groups);
        return groups;
    }
}
