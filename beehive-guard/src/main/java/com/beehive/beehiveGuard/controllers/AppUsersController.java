package com.beehive.beehiveGuard.controllers;

import com.beehive.beehiveGuard.model.dtos.AppUserDto;
import com.beehive.beehiveGuard.model.forms.AppUserForm;
import com.beehive.beehiveGuard.services.AppUserService;
import com.beehive.beehiveGuard.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/appUsers")
public class AppUsersController extends BaseController<AppUserDto, AppUserForm> {

    @Autowired
    public AppUsersController(AppUserService service, TokenService tokenService) {
        super(service);
    }


}
