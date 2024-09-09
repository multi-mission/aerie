package gov.nasa.jpl.aerie.procedural.procedural-constraints

import gov.nasa.jpl.aerie.timeline.Interval
import gov.nasa.jpl.aerie.timeline.payloads.IntervalLike

/** A single violation of a constraint. */
data class Violation(
    /** Interval on which the violation occurs. */
    override val interval: Interval,

    /** List of associated activities (directives or instances) that are related to the violation. */
    val ids: List<ActivityId> = listOf()
) : IntervalLike<Violation> {

  override fun withNewInterval(i: Interval) = Violation(i, ids)

  /** Constructs a violation on the same interval with a different list of ids. */
  fun withNewIds(vararg id: ActivityId) = Violation(interval, id.asList())

  /** Constructs a violation on the same interval with a different list of ids. */
  fun withNewIds(ids: List<ActivityId>) = Violation(interval, ids)
}
