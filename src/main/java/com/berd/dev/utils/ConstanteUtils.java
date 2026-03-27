package com.berd.dev.utils;

import java.util.HashMap;
import java.util.Map;

public class ConstanteUtils {

    public static HashMap<String, String> userRoleMap = new HashMap<>(
            Map.of(
                    "ROLE_USER", "Utilisateur",
                    "ROLE_ADMIN", "Admin"));

}
