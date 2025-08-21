package com.slapp.domain.enumeration;

import java.util.Arrays;
import java.util.List;

/**
 * The RoomType enumeration.
 */
public enum RoomType {
    RECORDING("Gravação"),
    REHEARSAL("Ensaio"),
    LIVE("Live/Show"),
    MIXING("Mixagem"),
    MASTERING("Masterização");

    private final String displayName;

    RoomType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    // Método utilitário para validação
    public static boolean isValid(String value) {
        if (value == null || value.trim().isEmpty()) {
            return true; // null/empty é válido para filtros opcionais
        }

        if ("BOTH".equals(value)) {
            return true; // BOTH é um valor especial válido
        }

        try {
            valueOf(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    // Método para obter todos os valores como lista
    public static List<RoomType> getAllTypes() {
        return Arrays.asList(values());
    }
}
