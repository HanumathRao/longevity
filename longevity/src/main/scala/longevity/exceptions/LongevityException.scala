package longevity.exceptions

/** an exception thrown by longevity */
class LongevityException(message: String, cause: Exception) extends Exception(message, cause) {

  def this(message: String) { this(message, null) }

}
