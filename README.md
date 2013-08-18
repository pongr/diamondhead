Utilities for working with signed requests in Scala. Diamondhead provides low-level generating and parsing functions for signed requests, and makes it easy to handle your own payload JSON format. Diamondhead ships with support for Facebook's signed request format built on these low-level functions.

### sbt

```
"com.pongr" %% "diamondhead" % "0.9.0-SNAPSHOT"
```

### Signed Requests

Web services need to communicate with one another. When one web service receives an HTTP request from another which includes important data, that web service often needs to verify that the HTTP request did in fact originate from the expected client (and not some bad actor trying to trick the service).

Signed requests provide a simple way for a web service to verify that the information it received in an HTTP request actually came from the expected client. The two services share some secret key (that no one else should know). When one service makes a request to the other, it signs the data it's sending with this secret key, and includes the signature along with this data. When the other service receives the data it verifies the signature. 

Note that signed requests do not encrypt the payload data; the data is sent as-is over whatever communication channel is being used. Signed requests simply provide a way for a web service to verify that the data in the request came from the expected client. You should always send HTTP requests via SSL/HTTPS if you want their contents encrypted.

Signed requests are defined in a (somewhat obscure) [OAuth2-related spec][6] and used increasingly by Facebook. 

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
  case Right(SignedRequest(_,_,_, Some(userId), Some(token), _,_)) => //authed user
  case Right(sr) => //un-authed user
  case Left(t) => //unable to parse the signed request
}
```

### Credits

* [commons-codec][1] for Base64url encoding and decoding
* [spray-json][2] for JSON parsing and generating

### Authors

* [Zach Cox][3]

[1]: http://commons.apache.org/proper/commons-codec/
[2]: https://github.com/spray/spray-json
[3]: https://github.com/zcox
[4]: https://developers.facebook.com/docs/reference/login/signed-request/
[5]: https://developers.facebook.com/docs/facebook-login/using-login-with-games/
[6]: https://docs.google.com/document/d/1kv6Oz_HRnWa0DaJx_SQ5Qlk_yqs_7zNAm75-FmKwNo4/pub
