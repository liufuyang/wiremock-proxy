# Overview

This is a deprecated Wiremock extension that allows using `urlPrefixToRemove`
as a parameter to make the proxy more powerful. (To deal with the situation mentioned [here](https://github.com/tomakehurst/wiremock/issues/745))

For example, to

# Building

Run `gradle jar` to build the JAR, which will be placed in `build/libs/`.

# Example usage

Running standalone server:

```
java \
        -cp wiremock-standalone.jar:wiremock-proxy-0.1.jar \
        com.github.tomakehurst.wiremock.standalone.WireMockServerRunner \
        --extensions="com.tradeshift.linelinker.integrationtests.WiremockProxyTransformer"
```

Creating a stub could look like:
```
curl -d '
{
  "request": {
    "method": "POST",
    "urlPath": "/prefix/to/remove/endpoint"
  },
  "response": {
    "proxyBaseUrl": "http://localhost:8080/proxy",
    "transformers": ["my-proxy"],
    "transformerParameters": {
      "urlPrefixToRemove": "/prefix/to/remove"
    }
  }
}' http://localhost:8080/__admin/mappings
```

or in code:
```
stubFor(post(urlMatching("/tradeshift/rest/external/linelinker/.*"))
                .willReturn(aResponse()
                        .proxiedFrom("http://docker.for.mac.localhost:8220/external")
                        .withTransformer("my-proxy", "urlPrefixToRemove", "/tradeshift/rest/external/linelinker"))
                        );
```

The example above will proxy any Post request on

`http://localhost:8080/tradeshift/rest/external/linelinker/abc`

to

`http://docker.for.mac.localhost:8220/external/abc`
