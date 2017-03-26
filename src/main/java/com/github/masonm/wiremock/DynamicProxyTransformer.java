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

        URI requestUri;
        try {
            requestUri = new URI(request.getAbsoluteUrl());
        } catch (URISyntaxException e) {
            return responseDefinition;
        }

        String proxyUrl = requestUri.getScheme() + "://" + request.getHeader(targetHeader);

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
