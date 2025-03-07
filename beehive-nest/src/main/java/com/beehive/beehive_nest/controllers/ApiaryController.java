package com.beehive.beehive_nest.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.beehive.beehive_nest.model.dtos.ApiaryDto;
import com.beehive.beehive_nest.model.dtos.AppUserDto;
import com.beehive.beehive_nest.model.forms.ApiaryBeekeeperForm;
import com.beehive.beehive_nest.model.forms.ApiaryForm;
import com.beehive.beehive_nest.services.ApiaryService;
import com.beehive.beehive_nest.services.AppUserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/apiary")
public class ApiaryController extends BaseController<ApiaryDto, ApiaryForm> {
    private final ApiaryService apiaryService;
    private final AppUserService appUserService;

    public ApiaryController(ApiaryService service, AppUserService appUserService) {
        super(service); // Injecting into the BaseController
        this.apiaryService = service; // Injecting into the controller as well
        this.appUserService = appUserService;

    }

    @GetMapping("/user/{userId}")
    public List<ApiaryDto> getApiariesByUserId(@PathVariable UUID userId) {
        return apiaryService.findApiariesByUserId(userId);
    }

    @GetMapping("/user")
    public List<ApiaryDto> getApiariesByUser(@AuthenticationPrincipal UserDetails userDetails) {
        AppUserDto profile = appUserService.findByEmail(userDetails.getUsername());

        return apiaryService.findApiariesByUserId(profile.getId());
    }

    @PostMapping("/user")
    public ResponseEntity<ApiaryDto> createApiaryByUser(@RequestBody ApiaryBeekeeperForm form,
            @AuthenticationPrincipal UserDetails userDetails) {
        AppUserDto profile = appUserService.findByEmail(userDetails.getUsername());

        ApiaryDto createdApiary = apiaryService.create(new ApiaryForm(form.getName(), profile.getId()));
        return ResponseEntity.status(200).body(createdApiary);
    }
}
