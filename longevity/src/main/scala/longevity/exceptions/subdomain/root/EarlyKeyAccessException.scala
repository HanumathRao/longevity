package longevity.exceptions.subdomain.root

import longevity.exceptions.subdomain.SubdomainException

/** an exception involving an attempt to access an key before [[Subdomain]] initilization */
class EarlyKeyAccessException extends SubdomainException(
  "cannot access RootEntityType.keys until after the subdomain has been initialized")
