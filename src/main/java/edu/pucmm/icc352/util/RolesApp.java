package edu.pucmm.icc352.util;

import io.javalin.security.RouteRole;

public enum RolesApp implements RouteRole {
                                NO_REGISTRADO,
                                USUARIO,  // Los que inician sesion pero no son admin
    ROLE_ADMIN //El admin
}