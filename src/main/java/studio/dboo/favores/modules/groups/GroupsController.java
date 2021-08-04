package studio.dboo.favores.modules.groups;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import studio.dboo.favores.modules.groups.entity.Groups;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
@Api(tags = {"그룹 CRUD"})
public class GroupsController {

    private final GroupsService groupsService;

    @GetMapping
    public void getGroup(){

    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "createGroup", notes = "그룹 생성")
    public ResponseEntity<Groups> createGroup(@Valid @RequestBody Groups groups, HttpServletRequest request) throws URISyntaxException {
        Groups savedGroup = groupsService.createGroups(groups);
        return ResponseEntity.created(new URI(request.getRequestURI() + "/" + savedGroup.getId())).build();
    }
}
