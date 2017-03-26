package com.github.masonm.wiremock;

import java.net.URI;
import java.net.URISyntaxException;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformer;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;

public class DynamicProxyTransformer extends ResponseDefinitionTransformer {
    @Override
    public ResponseDefinition transform(Request request, ResponseDefinition responseDefinition, FileSource files, Parameters parameters) {
        String targetHeader = parameters.getString("headerName");

        URI requestUri, targetUri;
        try {
            requestUri = new URI(request.getAbsoluteUrl());
            targetUri = new URI(request.getHeader(targetHeader));
        } catch (URISyntaxException e) {
            return responseDefinition;
        }

        String proxyUrl = "";
        if (targetUri.getScheme() == null) {
            proxyUrl = requestUri.getScheme() + "://";
        }
        proxyUrl += targetUri;

        return ResponseDefinitionBuilder
                .like(responseDefinition)
                .proxiedFrom(proxyUrl)
                .build();
    }

    @Override
    public boolean applyGlobally() {
        return false;
    }

    @Override
    public String getName() {
        return "dynamic-proxy";
    }
}
