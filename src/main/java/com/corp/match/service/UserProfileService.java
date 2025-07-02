package com.corp.match.service;



import com.corp.match.entity.UserProfile;
import com.corp.match.repository.UserProfileRepository;
import com.corp.match.uttility.EnrichmentApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    public UserProfile registerUser(UserProfile userProfile) {
        String domain = userProfile.getWorkEmail().substring(userProfile.getWorkEmail().indexOf('@') + 1);
        String companyName = EnrichmentApi.getCompanyNameFromDomain(domain);

        userProfile.setCompanyName(companyName);
        userProfile.setStatus("Offline");

        return userProfileRepository.save(userProfile);
    }
    public UserProfile saveUser(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }

}
