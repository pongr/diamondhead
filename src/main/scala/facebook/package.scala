package com.pongr.diamondhead

import spray.json._
import DefaultJsonProtocol._

package object facebook {
  case class Age(
    min: Int,
    max: Option[Int]
  )

  case class User(
    country: String,
    locale: String,
    age: Age
  )

  case class Page(
    id: String,
    liked: Boolean,
    admin: Boolean
  )

  case class SignedRequest(
    algorithm: String,
    issued_at: Long,
    user: User,
    user_id: Option[String],
    oauth_token: Option[String],
    expires: Option[Long],
    page: Option[Page]
  )

  implicit val ageFormat = jsonFormat2(Age)
  implicit val userFormat = jsonFormat3(User)
  implicit val pageFormat = jsonFormat3(Page)
  implicit val signedRequestFormat = jsonFormat7(SignedRequest)

  def parse(appSecret: String, signedRequest: String): Either[Throwable, SignedRequest] = 
    parseAs[SignedRequest](appSecret, signedRequest)
}
