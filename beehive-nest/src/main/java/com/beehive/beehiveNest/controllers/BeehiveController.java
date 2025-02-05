package com.beehive.beehiveNest.controllers;

import com.beehive.beehiveNest.model.dtos.BeehiveDto;
import com.beehive.beehiveNest.model.forms.BeehiveForm;
import com.beehive.beehiveNest.model.forms.CheckForm;
import com.beehive.beehiveNest.model.forms.RegisterBeehiveForm;
import com.beehive.beehiveNest.model.forms.SelfRegisterBeehiveForm;
import com.beehive.beehiveNest.services.BeehiveService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/beehives")
public class BeehiveController extends BaseController<BeehiveDto, BeehiveForm> {
    private final BeehiveService beehiveService;

    public BeehiveController(BeehiveService service) {
        super(service); // Injecting into the BaseController
        this.beehiveService = service; // Injecting into the controller as well
    }

    @GetMapping("/apiary/{apiaryId}")
    public List<BeehiveDto> getBeehivesByApiaryId(@PathVariable Long apiaryId) {
        return beehiveService.findBeehivesByApiaryId(apiaryId);
    }

    @PostMapping("/register")
    public BeehiveDto registerBeehive(@RequestBody RegisterBeehiveForm form) {
        return beehiveService.register(form);
    }

    @PostMapping("/check")
    public ResponseEntity<Void> checkBeehiveSerial(@RequestBody CheckForm checkForm) {
        if (beehiveService.checkBeehiveSerial(checkForm)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/selfRegister")
    public ResponseEntity<Void> selfRegisterHiveSensor(@RequestBody SelfRegisterBeehiveForm form) {
        beehiveService.selfRegister(form);
        return ResponseEntity.noContent().build();
    }

}
