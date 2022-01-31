package org.toxsoft.core.tslib.coll.notifier.impl;

import org.toxsoft.core.tslib.bricks.validator.ValidationResult;
import org.toxsoft.core.tslib.coll.notifier.INotifierMap;
import org.toxsoft.core.tslib.coll.notifier.basis.ITsMapValidator;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Adater for {@link ITsMapValidator} implementations.
 * <p>
 * All methods simply returns {@link ValidationResult#SUCCESS}.
 *
 * @author hazard157
 * @param <K> - the type of keys maintained by this map
 * @param <E> - the type of mapped values
 */
public class TsMapValidatorAdapter<K, E>
    implements ITsMapValidator<K, E> {

  @Override
  public ValidationResult canPut( INotifierMap<K, E> aSource, K aKey, E aExistingItem, E aNewItem ) {
    if( aKey == null || aNewItem == null ) {
      throw new TsNullArgumentRtException();
    }
    return ValidationResult.SUCCESS;
  }

  @Override
  public ValidationResult canRemove( INotifierMap<K, E> aSource, K aKey ) {
    if( aKey == null ) {
      throw new TsNullArgumentRtException();
    }
    return ValidationResult.SUCCESS;
  }

  @Override
  public ValidationResult canAdd( INotifierMap<K, E> aSource, K aKey, E aExistingItem, E aNewItem ) {
    if( aKey == null || aNewItem == null ) {
      throw new TsNullArgumentRtException();
    }
    return ValidationResult.SUCCESS;
  }

}
