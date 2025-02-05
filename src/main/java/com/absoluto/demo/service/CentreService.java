package com.absoluto.demo.service;

import com.absoluto.demo.centre.Centre;
import com.absoluto.demo.User;
import com.absoluto.demo.repository.CentreRepository;
import com.absoluto.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CentreService {

    @Autowired
    private CentreRepository centreRepository;

    @Autowired
    private UserRepository userRepository;

    public Centre createCentre(Centre centre) {
        return centreRepository.save(centre);
    }

    public Centre assignUserToCentre(Long centreId, Long userId) {
        Centre centre = centreRepository.findById(centreId)
                .orElseThrow(() -> new RuntimeException("Centre not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        centre.getAssignedUsers().add(user);
        return centreRepository.save(centre);
    }

    public long getCentreCount() {
        return centreRepository.count();
    }
}
