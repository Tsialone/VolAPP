package com.berd.dev.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.berd.dev.models.User;
import com.berd.dev.repositories.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final EmailService emailService;

    private final PasswordEncoder passwordEncoder;
    public User save(User user) {
        if (user == null || user.getUsername() == null || user.getPassword() == null || user.getEmail() == null) {
            throw new IllegalArgumentException(
                    "L'utilisateur, le nom d'utilisateur, le mot de passe et l'email ne peuvent pas être nuls");
        }
        if (user.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Le nom d'utilisateur ne peut pas être vide");
        }
        if (user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Le mot de passe ne peut pas être vide");
        }
        if (user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("L'email ne peut pas être vide");
        }

        User existingUser = userRepository.findByUsername(user.getUsername()).orElse(null);
        if (existingUser != null && existingUser.isActive()) {
            throw new IllegalArgumentException("Le nom d'utilisateur est déjà pris");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        String token = UUID.randomUUID().toString();
        user.setActive(false);
        user.setValidationToken(token);

        emailService.envoyerEmail(user.getEmail(), "Bienvenue sur notre application", "Veuillez cliquer sur le lien suivant pour valider votre compte : " + token);







        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + username));
    }
}
