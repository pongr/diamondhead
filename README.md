Utilities for working with signed requests in Scala. Diamondhead provides low-level generating and parsing functions for signed requests, and makes it easy to handle your own payload JSON format. Diamondhead ships with support for Facebook's signed request format built on these low-level functions.

### Signed Requests

TODO describe what signed requests are and use cases...

### Low-Level Functions

TODO generating & parsing with raw String payloads...

### Custom JSON Protocols

TODO generating & parsing with case class payloads...

### Facebook Signed Requsts

TODO parse Facebook's signed requests into convenient case classes...

### Credits

* [commons-codec][1] for Base64url encoding and decoding
* [spray-json][2] for JSON parsing and generating

### Authors

* Zach Cox

[1]: http://commons.apache.org/proper/commons-codec/
[2]: https://github.com/spray/spray-json
