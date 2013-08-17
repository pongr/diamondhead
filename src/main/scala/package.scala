package com.pongr

import spray.json._
import DefaultJsonProtocol._
import org.apache.commons.codec.binary.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

package object diamondhead {
  /** The character between the signature and payload. */
  val delimiter = '.'

  /** The charset to used when converting strings to and from byte arrays. */
  val charset = "UTF-8"
  /** Base64url decodes the specified string. */
  def decode(s: String): String = new String(new Base64(true).decode(s), charset) //the true param makes it url-safe, as per signed request spec
  /** Base64url encodes the specified bytes. */
  def encode(bs: Array[Byte]): String = new String(Base64.encodeBase64URLSafe(bs), charset)
  /** Base64url encodes the specified string. */
  def encode(s: String): String = encode(s.getBytes(charset))

  /** Signs the specified payload with the specified key and then base64url encodes the result. */
  def signAndEncode(key: String, payload: String): String = {
    //http://stackoverflow.com/a/14322761
    val algorithm = "HmacSHA256"
    val mac = Mac.getInstance(algorithm)
    mac.init(new SecretKeySpec(key.getBytes(charset), algorithm))
    encode(mac.doFinal(payload.getBytes(charset)))
  }

  /** Creates a signature using specified key and payload, then creates a signed request string with the format: signature + delimiter + payload. */
  def generate(key: String, payload: String): String = signAndEncode(key, payload) + delimiter + payload

  /** Generates the signed request using the specified key and encoded compact-printed JsValue. */
  def generate(key: String, jsvalue: JsValue): String = generate(key, encode(jsvalue.compactPrint))

  /** Generates the signed request using the specified key and T converted to JSON. */
  def generate[T : JsonFormat](key: String, t: T): String = generate(key, t.toJson)

  /** Parse the specified query string into a map of keys and values. */
  def parseParams(s: String): Map[String, String] = s.split('&').map(_.split('=')).map(a => (a(0), a(1))).toMap //TODO this is pretty bad... https://twitter.com/zcox/status/337291390704812032
  /** The standard name of the signed request parameter. */
  val signedRequestParam = "signed_request"
  /** Extract the signed request string out of another string, which could potentially be a query string. */
  def extract(s: String): Either[Throwable, String] = try {
    Right(if (s contains "=") parseParams(s).apply(signedRequestParam) else s)
  } catch {
    case t: Throwable => Left(t)
  }

  /** Split the specified string into two parts, separated by the first occurrence of the delimiter character. */
  def split(s: String): Either[Throwable, (String, String)] = try {
    val index = s indexOf delimiter
    if (index < 0) throw new IllegalArgumentException("String '%s' does not contain delimiter '%s'" format (s, delimiter))
    Right((s.substring(0, index), s.substring(index + 1)))
  } catch {
    case t: Throwable => Left(t)
  }

  /** Verifies the signature in the specified signed request using the specified key and returns the payload if it's valid. */
  def parse(key: String, signedRequest: String): Either[Throwable, String] = 
    extract(signedRequest).right.flatMap(split) match {
      case Right((signature, payload)) => 
        if (signature == signAndEncode(key, payload)) Right(payload)
        else Left(new InvalidSignature)
      case Left(t) => Left(t)
    }

  /** Verifies the signature in the specified signed request using the specified key and returns the decoded payload converted to a JsValue if it's valid. */
  def parseAsJsValue(key: String, signedRequest: String): Either[Throwable, JsValue] = parse(key, signedRequest).right.map(decode(_).asJson)

  /** Verifies the signature in the specified signed request using the specified key and returns the decoded payload converted to the specified type if it's valid. */
  def parseAs[T : JsonFormat](key: String, signedRequest: String): Either[Throwable, T] = parseAsJsValue(key, signedRequest).right.map(_.convertTo[T])
}
