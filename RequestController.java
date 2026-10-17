package com.pawhome.controller;

import com.pawhome.controller.Dto.CreateRequest;
import com.pawhome.controller.Dto.RequestResponse;
import com.pawhome.service.RequestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class RequestController {
    private final RequestService service;

    public RequestController(RequestService service) { this.service = service; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequestResponse create(Authentication auth, @Valid @RequestBody CreateRequest dto) {
        return service.create(auth.getName(), dto);
    }

    @GetMapping("/mine")
    public List<RequestResponse> mine(Authentication auth) { return service.mine(auth.getName()); }

    @GetMapping
    public List<RequestResponse> all() { return service.all(); }

    @PutMapping("/{id}/approve")
    public RequestResponse approve(@PathVariable Long id) { return service.approve(id); }

    @PutMapping("/{id}/reject")
    public RequestResponse reject(@PathVariable Long id) { return service.reject(id); }
}
