package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/credentials")
public class CredentialController {
    private final CredentialService credentialService;
    private final UserService userService;

    public CredentialController(CredentialService credentialService, UserService userService) {
        this.credentialService = credentialService;
        this.userService = userService;
    }

    @GetMapping()
    public String credentialView() {
        return "credentials";
    }

    @PostMapping(params="action=save")
    public String addCredentials(@ModelAttribute("credential") Credentials credential, @ModelAttribute("credentials") List<Credentials> credentials, Principal principal) {
        credential.setUserId(userService.getUserByUsername(principal.getName()).getUserId());
        credentialService.createCredentials(credential);
        credentials.add(credential);
        return "credentials";
    }

    @PostMapping(params="action=edit")
    public String editCredentials(@ModelAttribute("credential") Credentials credential, @ModelAttribute("credentials") List<Credentials> credentials) {
        for(Credentials c : credentials) {
            if(c.getCredentialId().equals(credential.getCredentialId())) {
                c.setUrl(credential.getUrl());
                c.setUsername(credential.getUsername());
                c.setPassword(credential.getPassword());
                credentialService.updateCredentials(c);
            }
        }
        return "credentials";
    }

    @PostMapping(params="action=delete")
    public String deleteCredentials(@ModelAttribute("credential") Credentials credential, @ModelAttribute("credentials") List<Credentials> credentials) {
        credentials.removeIf(c -> Objects.equals(c.getCredentialId(), credential.getCredentialId()));
        credentialService.deleteCredentials(credential.getCredentialId());
        return "credentials";
    }

    @ModelAttribute("credentials")
    public List<Credentials> allCredentials(@ModelAttribute("credential") Credentials credential, Principal principal) {
        return credentialService.getCredentials(userService.getUserByUsername(principal.getName()).getUserId());
    }
}
