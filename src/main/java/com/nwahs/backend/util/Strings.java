package com.nwahs.backend.util;

public interface Strings {

    short apiVersion = 1; // API Version
    String apiPath = "/api/v" + apiVersion; // API Base URL

    /**
     * Address of the backend dashboard,
     * to allow fetching data to this
     * server
     */
    String svelteBackend = "http://localhost:3000";
}
