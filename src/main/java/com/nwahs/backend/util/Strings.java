package com.nwahs.backend.util;

public interface Strings {

    String apiVersion = "v1"; // API Version
    String apiPath = "/api/" + apiVersion; // API Base URL

    // Address of the backend dashboard
    // in order to allow fetching data
    // from the backend through CORS
    String svelteBackend = "http://localhost:3000";
}
