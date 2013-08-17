Utilities for working with signed requests in Scala. Diamondhead provides low-level generating and parsing functions for signed requests, and makes it easy to handle your own payload JSON format. Diamondhead ships with support for Facebook's signed request format built on these low-level functions.

### sbt

```
"com.pongr" %% "diamondhead" % "0.9.0-SNAPSHOT"
```

### Signed Requests

TODO describe what signed requests are and use cases...

[OAuth 2.0 signed request spec][6]

### Low-Level Functions

TODO generating & parsing with raw String payloads...

### Custom JSON Protocols

TODO generating & parsing with case class payloads...

### Facebook Signed Requsts

[Facebook signed requests][5]

[Facebook signed request fields][4]


``` scala

import com.pongr.diamondhead.facebook._

val signedRequest: String = ??? //probably extracted from a POST request from Facebook
val appSecret: String = ???     //probably from your app config
parse(appSecret, signedRequest) match {
  case Right(sr) => //use the SignedRequest to do something cool
  case Left(t) =>   //unable to parse the signed request
}
```

### Credits

* [commons-codec][1] for Base64url encoding and decoding
* [spray-json][2] for JSON parsing and generating

### Authors

* [Zach Cox][3]

[1]: http://commons.apache.org/proper/commons-codec/
[2]: https://github.com/spray/spray-json
[3]: http://theza.ch
[4]: https://developers.facebook.com/docs/reference/login/signed-request/
[5]: https://developers.facebook.com/docs/facebook-login/using-login-with-games/
[6]: https://docs.google.com/document/d/1kv6Oz_HRnWa0DaJx_SQ5Qlk_yqs_7zNAm75-FmKwNo4/pub
