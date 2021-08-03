package studio.dboo.favores.modules.groups;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupsController {

    private final GroupsService groupsService;

    @GetMapping
    public void getGroup(){

    }

    @PostMapping
    public ResponseEntity<Groups> createGroup(Groups groups){
        return ResponseEntity.status(HttpStatus.OK).body(groupsService.createGroup(groups));
    }
}
