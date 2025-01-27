package com.beehive.beehiveNest.controllers;

import com.beehive.beehiveNest.model.dtos.AppUserDto;
import com.beehive.beehiveNest.model.forms.AppUserForm;
import com.beehive.beehiveNest.services.AppUserService;
import com.beehive.beehiveNest.services.TokenService;
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
