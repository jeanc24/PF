package edu.pucmm.icc352.util;

import io.javalin.http.Context;

public interface CrudOpenApi <T>{
    void getAll(Context ctx) throws Exception;
    void getOne(Context ctx) throws Exception;
    void create(Context ctx) throws Exception;
    void update(Context ctx) throws Exception;
    void delete(Context ctx) throws Exception;
}
