package gov.nasa.jpl.ammos.mpsa.aerie.merlin.framework.models;

import gov.nasa.jpl.ammos.mpsa.aerie.merlinsdk.effects.traits.ConcurrentUpdateTrait;
import gov.nasa.jpl.ammos.mpsa.aerie.merlinsdk.resources.discrete.DiscreteResource;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Optional;
import java.util.Set;

import static gov.nasa.jpl.ammos.mpsa.aerie.merlinsdk.resources.DelimitedDynamics.persistent;

public final class RegisterModel<T> implements Model<Pair<Optional<T>, Set<T>>, RegisterModel<T>> {
  private T _value;
  private boolean _conflicted;

  public RegisterModel(final T initialValue, final boolean conflicted) {
    this._value = initialValue;
    this._conflicted = conflicted;
  }

  public RegisterModel(final T initialValue) {
    this(initialValue, false);
  }

  @Override
  public RegisterModel<T> duplicate() {
    return new RegisterModel<>(this._value, this._conflicted);
  }

  @Override
  public ConcurrentUpdateTrait<T> effectTrait() {
    return new ConcurrentUpdateTrait<>();
  }

  @Override
  public void react(final Pair<Optional<T>, Set<T>> concurrentValues) {
    concurrentValues.getLeft().ifPresent(newValue -> this._value = newValue);
    this._conflicted = (concurrentValues.getRight().size() > 1);
  }


  /// Resources
  public static <T> DiscreteResource<RegisterModel<T>, T> value() {
    return (model) -> persistent(model._value);
  }

  public static DiscreteResource<RegisterModel<?>, Boolean> conflicted =
      (model) -> persistent(model._conflicted);
}
