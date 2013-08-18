Utilities for working with signed requests in Scala. Diamondhead provides low-level generating and parsing functions for signed requests, and makes it easy to handle your own payload JSON format. Diamondhead ships with support for Facebook's signed request format built on these low-level functions.

### sbt

```
"com.pongr" %% "diamondhead" % "0.9.0"
```

### Signed Requests

Web services need to communicate with one another. When one web service receives an HTTP request from another which includes important data, that web service often needs to verify that the HTTP request did in fact originate from the expected client (and not some bad actor trying to trick the service). If some other web site is asking your service for HTML to display in an iframe, or notifying you that some event occurred, you need to verify that the requestor is who you expect.

Signed requests provide a simple way for a web service to verify that the information it received in an HTTP request actually came from the expected client. The two services share some secret key (that no one else should know). When one service makes a request to the other, it signs the data it's sending with this secret key (using somethign like [HMAC-SHA256][12]), and includes the signature along with this data. When the other service receives the data it verifies the signature. 

Note that signed requests do not encrypt the payload data; the data is sent as-is over whatever communication channel is being used. Signed requests simply provide a way for a web service to verify that the data in the request came from the expected client. You should always send HTTP requests via SSL/HTTPS if you want their contents encrypted.

Signed requests are defined in a (somewhat obscure) [OAuth2-related spec][6] and used increasingly by Facebook, especially when requesting the iframe content for a Facebook app's canvas page or a Facebook page tab. The base64url encoding and decoding, along with HMAC-SHA256 signing to verify signatures, is tedious code to write that can easily be performed by a library. Diamondhead provides all of this for you.

### Facebook Signed Requsts

Diamondhead ships with support for [Facebook signed requests][5], which comes in very handy when your Scala web app needs to verify the signed_request parameter and extract out the Facebook userId and OAuth2 access token. The [SignedRequest][7] case class includes common fields that Facebook uses in their [signed requests][4]. For examples of how Facebook uses signed requests, see the [canvas tutorial][8] and [page tab tutorial][9].

``` scala

import com.pongr.diamondhead.facebook._

val signedRequest: String = ??? //probably extracted from a POST request from Facebook
val appSecret: String = ???     //probably from your app config or database
parse(appSecret, signedRequest) match {
  case Right(SignedRequest(_,_,_, Some(userId), Some(token), _,_)) => //authed user
  case Right(sr) => //un-authed user
  case Left(t) => //unable to parse the signed request
}
```

This SignedRequest case class is built on Diamondhead's generic signed request functions. You can define your own case class to work with your own payload data format.

### Custom JSON Protocols

Signed requests aren't just for Facebook. You can define your own payload JSON formats and use them when providing your own services. Typically you want to use a case class to work with payload data, instead of raw json. Simply define a [spray-json protocol][10] for your case class:

``` scala
import spray.json._

object ThingProtocol extends DefaultJsonProtocol {
  case class Thing(a: String, b: Int, c: Boolean)
  implicit val thingFormat = jsonFormat3(Thing)
}
```

The `generate` function will convert your case class to json, base64url encode it, sign it and produce the final signed request string:

``` scala
import com.pongr.diamondhead._

val signedRequest: String = generate(key, Thing("a", 1, true))
//now send signedRequest to some web service, they'll know it came from you
```

The `parseAs` function will verify the signature in a signed request string, then decode and parse it into an instance of your case class (or return any error that occurred during this process):

``` scala
import com.pongr.diamondhead._
import ThingProtocol._

val e: Either[Throwable, Thing] = parseAs(key, signedRequest)
//handle the parse error or use the thing
```

### Low-Level Functions

The custom JSON protocol functions descried above are built on top of low-level String functions. If by chance you have your own base64url encoded payload String (instead of a case class instance) you can generate the signed request String:

``` scala
import com.pongr.diamondhead._

val payload: String = ??? //already base64url encoded
val signedRequest: String = generate(key, payload)
//now send signedRequest to some web service, they'll know it came from you
```

Similarly, if you just need to verify and extract the encoded payload String from a signed request, you can do that too:

``` scala
import com.pongr.diamondhead._

val e: Either[Throwable, String] = parse(key, signedRequest)
//handle the parse error or use the payload string
```

### License

Diamondhead is released under the [Apache 2 License][11].

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
[7]: https://github.com/pongr/diamondhead/blob/master/src/main/scala/facebook/package.scala
[8]: https://developers.facebook.com/docs/appsonfacebook/tutorial/
[9]: https://developers.facebook.com/docs/appsonfacebook/pagetabs/
[10]: https://github.com/spray/spray-json#providing-jsonformats-for-case-classes
[11]: http://www.apache.org/licenses/LICENSE-2.0.txt
[12]: http://en.wikipedia.org/wiki/Hash-based_message_authentication_code
