package com.pongr

import org.specs2.mutable._
import spray.json._
import com.pongr.diamondhead._

object SignedRequestSpecProtocol extends DefaultJsonProtocol {
  case class Thing(a: String, b: Int, c: Boolean)
  implicit val thingFormat = jsonFormat3(Thing)
}

class SignedRequestSpec extends Specification {
  import SignedRequestSpecProtocol._

  val key1 = "key"
  val payload1 = "payload"
  val signature1 = "XZi0XJCiB_qZjOY5_qbwLsyMw_Nv74HWlPuFa00KKMo"
  val signedRequest1 = signature1 + delimiter + payload1

  val key2 = "key2"
  val payload2 = "eyJhIjoiYSIsImIiOjEsImMiOnRydWV9"
  val signature2 = "_4Yi_c6tAiyCCgZEn2fEngQm8MvBWWK4phdTRvAm7jw"
  val signedRequest2 = signature2 + delimiter + payload2
  val thing2 = Thing("a", 1, true)
  val rawJson2 = """{"a":"a","b":1,"c":true}"""

  val key3 = "105c404824eaf71cb547eef8bd679c28"
  val payload3 = "eyJhbGdvcml0aG0iOiJITUFDLVNIQTI1NiIsImlzc3VlZF9hdCI6MTM3NDE3NzAxMCwicGFnZSI6eyJpZCI6IjY5NDUyOTUyNzIzMDUwNyIsImxpa2VkIjp0cnVlLCJhZG1pbiI6ZmFsc2V9LCJ1c2VyIjp7ImNvdW50cnkiOiJ1cyIsImxvY2FsZSI6ImVuX1VTIiwiYWdlIjp7Im1pbiI6MjF9fX0"
  val signature3 = "H-g9Nu4Bo9adnQIkYQlVed0C3soAXpWglFoeSnWYBho"
  val signedRequest3 = signature3 + delimiter + payload3
  val rawJson3 = """{"algorithm":"HMAC-SHA256","issued_at":1374177010,"page":{"id":"694529527230507","liked":true,"admin":false},"user":{"country":"us","locale":"en_US","age":{"min":21}}}"""

  "The signed request functions" should {
    "sign a string with a key using hmac-sha256 and then base64url encode it" in {
      signAndEncode(key1, payload1) must_== signature1
      signAndEncode(key2, payload2) must_== signature2
      signAndEncode(key3, payload3) must_== signature3
    }

    "generate a signed request string from a String payload and key" in {
      generate(key1, payload1) must_== signedRequest1
      generate(key2, payload2) must_== signedRequest2
      generate(key3, payload3) must_== signedRequest3
    }

    "generate a signed request string from a JsValue payload and key" in {
      generate(key2, rawJson2.asJson) must_== signedRequest2
      generate(key3, rawJson3.asJson) must_== signedRequest3
    }

    "generate a signed request string from a case class payload and key" in {
      generate(key2, thing2) must_== signedRequest2
    }

    "split the raw string on delimiter" in {
      split("ab" + delimiter + "cd") must beRight(("ab", "cd"))
      split("abcd") must beLeft.like { case t => t must beAnInstanceOf[IllegalArgumentException] }
    }

    "parse query parameters" in {
      parseParams("a=b&c=d") must_== Map("a" -> "b", "c" -> "d")
    }

    "extract signed request from a plain or query string" in {
      extract("sr") must beRight("sr")
      extract("signed_request=sr") must beRight("sr")
      extract("signed_request=sr&locale=en_US") must beRight("sr")
      extract("foo=bar") must beLeft.like { case t => t must beAnInstanceOf[Throwable] }
    }

    "don't parse malformed signed request" in {
      parse(key1, "foobar") must beLeft.like { case t => t must beAnInstanceOf[Throwable] }
      parse(key1, "foo=bar") must beLeft.like { case t => t must beAnInstanceOf[Throwable] }
    }

    "don't parse signed request if signature is invalid" in {
      parse(key1, "invalid." + payload1) must beLeft.like { case t => t must beAnInstanceOf[InvalidSignature] }
    }

    "parse a valid signed request into just the payload string" in {
      parse(key1, signedRequest1) must beRight(payload1)
      parse(key2, signedRequest2) must beRight(payload2)
      parse(key3, signedRequest3) must beRight(payload3)
    }

    "parse a valid signed request into a JsValue from decoded payload" in {
      parseAsJsValue(key2, signedRequest2) must beRight(rawJson2.asJson)
      parseAsJsValue(key3, signedRequest3) must beRight(rawJson3.asJson)
    }

    "parse a valid signed request into a case class from decoded payload" in {
      parseAs(key2, signedRequest2) must beRight(thing2)
    }
  }
}
