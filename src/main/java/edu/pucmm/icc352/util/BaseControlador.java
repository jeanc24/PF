package edu.pucmm.icc352.util;

import io.javalin.Javalin;

public abstract class BaseControlador {

    protected static Javalin app;

    public BaseControlador(Javalin app){
        this.app = app;
    }

    abstract public void aplicarRutas();
}
