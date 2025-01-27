package com.beehive.beehiveGuard.controllers;

import com.beehive.beehiveGuard.model.dtos.UserRoleDto;
import com.beehive.beehiveGuard.model.forms.UserRoleForm;
import com.beehive.beehiveGuard.services.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/userRole")
public class UserRoleController extends BaseController<UserRoleDto, UserRoleForm> {
    private final UserRoleService userRoleService;

    @Autowired
    public UserRoleController(UserRoleService userRoleService) {
        super(userRoleService);  // Injecting into the BaseController
        this.userRoleService = userRoleService;  // Injecting into the controller as well
    }
}
