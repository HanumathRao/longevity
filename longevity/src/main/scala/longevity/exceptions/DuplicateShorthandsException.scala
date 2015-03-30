package longevity.exceptions

/** An exception that is thrown on attempt to construct a [[longevity.context.ShorthandPool ShorthandPool]]
 * with more than one [[longevity.context.Shorthand Shorthand]] for the same `Actual` type.
 */
class DuplicateShorthandsException
extends ShorthandException("a ShorthandPool cannot contain multiple Shorthands with the same Long type")