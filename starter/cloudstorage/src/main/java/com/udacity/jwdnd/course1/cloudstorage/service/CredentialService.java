package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.repository.CredentialMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialService {
    private final CredentialMapper credentialMapper;

    public CredentialService(CredentialMapper credentialMapper) {
        this.credentialMapper = credentialMapper;
    }
    public int createCredentials(Credentials credentials) {
        return credentialMapper.insertCredentials(credentials);
    }

    public boolean updateCredentials(Credentials credentials) {
        return credentialMapper.updateCredentials(credentials);
    }

    public List<Credentials> getCredentials(Integer userId) {
        return credentialMapper.findCredentialsByUserId(userId);
    }

    public void deleteCredentials(Integer credentialId) {
        credentialMapper.deleteCredentials(credentialId);
    }
}
