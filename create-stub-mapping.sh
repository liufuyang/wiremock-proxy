#!/bin/sh

# java -cp ../wiremock/build/libs/wiremock-standalone-2.6.0.jar:build/libs/wiremock-dynamic-proxy-0.1.jar com.github.tomakehurst.wiremock.standalone.WireMockServerRunner --verbose --extensions="com.github.masonm.wiremock.DynamicProxyTransformer"

curl -d '{
    "request": {
        "method": "GET",
        "urlPattern": ".*"
    },
    "response": {
	"transformers": ["dynamic-proxy"],
	"transformerParameters": {
		"headerName": "X-Dest"
	}
    }
}' http://localhost:8080/__admin/mappings
