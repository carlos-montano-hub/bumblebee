package com.beehive.beehiveNest.controllers;

import com.beehive.beehiveNest.model.dtos.ApiaryDto;
import com.beehive.beehiveNest.model.dtos.AppUserDto;
import com.beehive.beehiveNest.model.forms.ApiaryBeekeeperForm;
import com.beehive.beehiveNest.model.forms.ApiaryForm;
import com.beehive.beehiveNest.services.ApiaryService;
import com.beehive.beehiveNest.services.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/apiary")
public class ApiaryController extends BaseController<ApiaryDto, ApiaryForm> {
    private final ApiaryService apiaryService;
    private final AppUserService appUserService;

    @Autowired
    public ApiaryController(ApiaryService service, AppUserService appUserService) {
        super(service);  // Injecting into the BaseController
        this.apiaryService = service;  // Injecting into the controller as well
        this.appUserService = appUserService;

    }

    @GetMapping("/user/{userId}")
    public List<ApiaryDto> getApiariesByUserId(@PathVariable Long userId) {
        return apiaryService.findApiariesByUserId(userId);
    }

    @GetMapping("/user")
    public List<ApiaryDto> getApiariesByUser(@AuthenticationPrincipal UserDetails userDetails) {
        AppUserDto profile = appUserService.findByEmail(userDetails.getUsername());

        return apiaryService.findApiariesByUserId(profile.getId());
    }

    @PostMapping("/user")
    public ResponseEntity<ApiaryDto> createApiaryByUser(@RequestBody ApiaryBeekeeperForm form, @AuthenticationPrincipal UserDetails userDetails) {
        AppUserDto profile = appUserService.findByEmail(userDetails.getUsername());

        ApiaryDto createdApiary = apiaryService.create(new ApiaryForm(form.getName(), profile.getId()));
        return ResponseEntity.status(200).body(createdApiary);
    }
}
