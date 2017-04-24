# Overview

This is a deprecated Wiremock extension that allows dynamically determining the `proxyBaseURL` from a request header.
It's no longer necessary, since this functionality was added in Wiremock 2.6.0 with https://github.com/tomakehurst/wiremock/pull/641

I'm keeping this around as an example of a simple Wiremock extension.

# Building

Run `gradle jar` to build the JAR, which will be placed in `build/libs/`.

# Example usage

Running standalone server:

```
java \
        -cp wiremock-standalone.jar:wiremock-dynamic-proxy-0.1.jar \
        com.github.tomakehurst.wiremock.standalone.WireMockServerRunner \
        --extensions="com.github.masonm.wiremock.DynamicProxyTransformer"
```

Creating a stub mapping where the "X-Dest" header from the request is used as the `proxyBaseUrl`:
```
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
```
