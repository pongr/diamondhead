package com.pongr

import org.specs2.mutable._
import com.pongr.diamondhead.facebook._

class FacebookSpec extends Specification {
  val appSecret = "105c404824eaf71cb547eef8bd679c28"

  //un-authed user
  val request1 = "H-g9Nu4Bo9adnQIkYQlVed0C3soAXpWglFoeSnWYBho.eyJhbGdvcml0aG0iOiJITUFDLVNIQTI1NiIsImlzc3VlZF9hdCI6MTM3NDE3NzAxMCwicGFnZSI6eyJpZCI6IjY5NDUyOTUyNzIzMDUwNyIsImxpa2VkIjp0cnVlLCJhZG1pbiI6ZmFsc2V9LCJ1c2VyIjp7ImNvdW50cnkiOiJ1cyIsImxvY2FsZSI6ImVuX1VTIiwiYWdlIjp7Im1pbiI6MjF9fX0"
  val signature1 = "H-g9Nu4Bo9adnQIkYQlVed0C3soAXpWglFoeSnWYBho"
  val payload1 = "eyJhbGdvcml0aG0iOiJITUFDLVNIQTI1NiIsImlzc3VlZF9hdCI6MTM3NDE3NzAxMCwicGFnZSI6eyJpZCI6IjY5NDUyOTUyNzIzMDUwNyIsImxpa2VkIjp0cnVlLCJhZG1pbiI6ZmFsc2V9LCJ1c2VyIjp7ImNvdW50cnkiOiJ1cyIsImxvY2FsZSI6ImVuX1VTIiwiYWdlIjp7Im1pbiI6MjF9fX0"
  val rawJson1 = """{"algorithm":"HMAC-SHA256","issued_at":1374177010,"page":{"id":"694529527230507","liked":true,"admin":false},"user":{"country":"us","locale":"en_US","age":{"min":21}}}"""
  val signedRequest1 = FacebookSignedRequest(
    "HMAC-SHA256", 1374177010,
    User("us", "en_US", Age(21, None)),
    None, None, None,
    Some(Page("694529527230507", true, false))
  )

  //authed user
  val request2 = "GpSTGUbspdpHkPHstKT8qOCxLiVG1ooRQY6yT0b4qDE.eyJhbGdvcml0aG0iOiJITUFDLVNIQTI1NiIsImV4cGlyZXMiOjEzNzQyNTY4MDAsImlzc3VlZF9hdCI6MTM3NDI1MDY5MSwib2F1dGhfdG9rZW4iOiJDQUFUaVpBMmV5QWtBQkFNTDZuU09sVkxXdFdvazJpNTVKYnFPeEVaQVJ4bHoyT3g2YlYwdUpaQlBQcmYzb3pwRk9sMUlaQ3JRUVlBcmNSMk14bzdOQzZqOHUzVFhPOEJkdWhYSFd6TVFiejRHVUlIZmRDWkFMYUQzekhFWkFUeE82OWt1QnVDSlZkNGhNYWpQNXZ1VFBUaDBvd1lrZG9BdGtaRCIsInBhZ2UiOnsiaWQiOiI2OTQ1Mjk1MjcyMzA1MDciLCJsaWtlZCI6dHJ1ZSwiYWRtaW4iOmZhbHNlfSwidXNlciI6eyJjb3VudHJ5IjoidXMiLCJsb2NhbGUiOiJlbl9VUyIsImFnZSI6eyJtaW4iOjIxfX0sInVzZXJfaWQiOiIxODM2NjM4NjMyIn0"
  val signature2 = "GpSTGUbspdpHkPHstKT8qOCxLiVG1ooRQY6yT0b4qDE"
  val payload2 = "eyJhbGdvcml0aG0iOiJITUFDLVNIQTI1NiIsImV4cGlyZXMiOjEzNzQyNTY4MDAsImlzc3VlZF9hdCI6MTM3NDI1MDY5MSwib2F1dGhfdG9rZW4iOiJDQUFUaVpBMmV5QWtBQkFNTDZuU09sVkxXdFdvazJpNTVKYnFPeEVaQVJ4bHoyT3g2YlYwdUpaQlBQcmYzb3pwRk9sMUlaQ3JRUVlBcmNSMk14bzdOQzZqOHUzVFhPOEJkdWhYSFd6TVFiejRHVUlIZmRDWkFMYUQzekhFWkFUeE82OWt1QnVDSlZkNGhNYWpQNXZ1VFBUaDBvd1lrZG9BdGtaRCIsInBhZ2UiOnsiaWQiOiI2OTQ1Mjk1MjcyMzA1MDciLCJsaWtlZCI6dHJ1ZSwiYWRtaW4iOmZhbHNlfSwidXNlciI6eyJjb3VudHJ5IjoidXMiLCJsb2NhbGUiOiJlbl9VUyIsImFnZSI6eyJtaW4iOjIxfX0sInVzZXJfaWQiOiIxODM2NjM4NjMyIn0"
  val rawJson2 = """{"algorithm":"HMAC-SHA256","expires":1374256800,"issued_at":1374250691,"oauth_token":"CAATiZA2eyAkABAML6nSOlVLWtWok2i55JbqOxEZARxlz2Ox6bV0uJZBPPrf3ozpFOl1IZCrQQYArcR2Mxo7NC6j8u3TXO8BduhXHWzMQbz4GUIHfdCZALaD3zHEZATxO69kuBuCJVd4hMajP5vuTPTh0owYkdoAtkZD","page":{"id":"694529527230507","liked":true,"admin":false},"user":{"country":"us","locale":"en_US","age":{"min":21}},"user_id":"1836638632"}"""
  val signedRequest2 = FacebookSignedRequest(
    "HMAC-SHA256", 1374250691,
    User("us", "en_US", Age(21, None)),
    Some("1836638632"),
    Some("CAATiZA2eyAkABAML6nSOlVLWtWok2i55JbqOxEZARxlz2Ox6bV0uJZBPPrf3ozpFOl1IZCrQQYArcR2Mxo7NC6j8u3TXO8BduhXHWzMQbz4GUIHfdCZALaD3zHEZATxO69kuBuCJVd4hMajP5vuTPTh0owYkdoAtkZD"),
    Some(1374256800),
    Some(Page("694529527230507", true, false))
  )

  val appSecret2 = "b3680eea55349eb0d9a3a7ced67bacda"
  val request4 = "signed_request=zcUSiusjiklK9_aEVV1YhUzsb1HKe73IlPZswgHoPJI.eyJhbGdvcml0aG0iOiJITUFDLVNIQTI1NiIsImV4cGlyZXMiOjEzNzU0ODA4MDAsImlzc3VlZF9hdCI6MTM3NTQ3NTM2NSwib2F1dGhfdG9rZW4iOiJDQUFIWU1vckVnVUVCQUE3cmZTMWtYR2paQ2lveW94NXVGVFpCbU8xZnU4WkFwSUphUzg2aUFPZVRZeEJxVHFaQ0hYT2hFWHpqZnBqeFpBNXJ5ZVhHMDFZa2c4Z1VoNVFsQ1BKME1ZaW9yd0VQdWJPRkJ3T2hXWHJXa2hKRExtT2NWUTczcGlLdDZMYkt4V05sRGltM0l6UFlNekloN0tNdENVR0tnY3BJRG1nWkRaRCIsInVzZXIiOnsiY291bnRyeSI6InVzIiwibG9jYWxlIjoiZW5fVVMiLCJhZ2UiOnsibWluIjoyMX19LCJ1c2VyX2lkIjoiNTk5MzE2MDEwIn0&locale=en_US"
  val signature4 = "zcUSiusjiklK9_aEVV1YhUzsb1HKe73IlPZswgHoPJI"
  val payload4 = "eyJhbGdvcml0aG0iOiJITUFDLVNIQTI1NiIsImV4cGlyZXMiOjEzNzU0ODA4MDAsImlzc3VlZF9hdCI6MTM3NTQ3NTM2NSwib2F1dGhfdG9rZW4iOiJDQUFIWU1vckVnVUVCQUE3cmZTMWtYR2paQ2lveW94NXVGVFpCbU8xZnU4WkFwSUphUzg2aUFPZVRZeEJxVHFaQ0hYT2hFWHpqZnBqeFpBNXJ5ZVhHMDFZa2c4Z1VoNVFsQ1BKME1ZaW9yd0VQdWJPRkJ3T2hXWHJXa2hKRExtT2NWUTczcGlLdDZMYkt4V05sRGltM0l6UFlNekloN0tNdENVR0tnY3BJRG1nWkRaRCIsInVzZXIiOnsiY291bnRyeSI6InVzIiwibG9jYWxlIjoiZW5fVVMiLCJhZ2UiOnsibWluIjoyMX19LCJ1c2VyX2lkIjoiNTk5MzE2MDEwIn0"
  val rawJson4 = """{"algorithm":"HMAC-SHA256","expires":1375480800,"issued_at":1375475365,"oauth_token":"CAAHYMorEgUEBAA7rfS1kXGjZCioyox5uFTZBmO1fu8ZApIJaS86iAOeTYxBqTqZCHXOhEXzjfpjxZA5ryeXG01Ykg8gUh5QlCPJ0MYiorwEPubOFBwOhWXrWkhJDLmOcVQ73piKt6LbKxWNlDim3IzPYMzIh7KMtCUGKgcpIDmgZDZD","user":{"country":"us","locale":"en_US","age":{"min":21}},"user_id":"599316010"}"""
  val signedReqeust4 = FacebookSignedRequest(
    "HMAC-SHA256", 1375475365,
    User("us", "en_US", Age(21, None)),
    Some("599316010"),
    Some("CAAHYMorEgUEBAA7rfS1kXGjZCioyox5uFTZBmO1fu8ZApIJaS86iAOeTYxBqTqZCHXOhEXzjfpjxZA5ryeXG01Ykg8gUh5QlCPJ0MYiorwEPubOFBwOhWXrWkhJDLmOcVQ73piKt6LbKxWNlDim3IzPYMzIh7KMtCUGKgcpIDmgZDZD"),
    Some(1375480800),
    None
  )

  "The Facebook signed request protocol" should {
    "parse a signed request for un-authed user" in {
      parse(appSecret, request1) must beRight(signedRequest1)
      parse(appSecret, "signed_request=" + request1) must beRight(signedRequest1)
    }

    "parse a signed request for authed user" in {
      parse(appSecret, request2) must beRight(signedRequest2)
    }

    "parse an app canvas page request which contains other params" in {
      parse(appSecret2, request4) must beRight(signedReqeust4)
    }
  }
}
