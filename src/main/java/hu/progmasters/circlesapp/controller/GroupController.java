package hu.progmasters.circlesapp.controller;

import hu.progmasters.circlesapp.domain.Group;
import hu.progmasters.circlesapp.dto.incoming.GroupCreationCommand;
import hu.progmasters.circlesapp.service.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final Logger logger = LoggerFactory.getLogger(GroupController.class);
    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping
    public ResponseEntity<Group> createGroup(@RequestBody GroupCreationCommand command){
       String username = getUsernameFromContext();
        Group group = groupService.createGroup(command, username);
       logger.info("New group has been created");
       return new ResponseEntity<Group>(group, HttpStatus.CREATED);
    }

    private String getUsernameFromContext(){
        String username = null;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof UserDetails loggedInUser){
            username = loggedInUser.getUsername();
        }
        return username;
    }
}
