package org.toxsoft.core.tslib.av.validators.defav;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.core.tslib.av.validators.defav.ITsResources.*;

import java.util.regex.Pattern;

import org.toxsoft.core.tslib.av.EAtomicType;
import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants;
import org.toxsoft.core.tslib.av.opset.IOptionSet;
import org.toxsoft.core.tslib.bricks.validator.ITsValidator;
import org.toxsoft.core.tslib.bricks.validator.ValidationResult;

/**
 * Валидатор типа {@link EAtomicType#STRING}.
 *
 * @author hazard157
 */
class StringValidator
    implements ITsValidator<IAtomicValue> {

  /**
   * Откомпилированный шаблон маски текстовой строки (если она определена в ограничении
   * {@link IAvMetaConstants#TSID_STRING_MASK}.
   * <p>
   * Меняется при изменении значения органичения в {@link DefaultAvValidator#constraints}.
   * <p>
   * При отсутствии маски строки, данная сыслка равна null.
   */
  private Pattern pattern     = null;
  /**
   * Последняя использованная маска, null означает, что маска не задана
   */
  private String  prevStrMask = null;

  private IOptionSet constraints;

  public StringValidator( IOptionSet aConstraints ) {
    constraints = aConstraints;
  }

  @Override
  public ValidationResult validate( IAtomicValue aValue ) {
    String s = aValue.asString();
    // проверим на соответствие маске
    String strMask = constraints.getStr( TSID_STRING_MASK, null );
    if( strMask != null ) {
      if( prevStrMask == null || !prevStrMask.equals( strMask ) ) {
        pattern = Pattern.compile( strMask );
      }
      if( !pattern.matcher( s ).matches() ) {
        return ValidationResult.error( FMT_ERR_NO_STRING_MASK_MATCH, s );
      }
    }
    else {
      pattern = null;
    }
    prevStrMask = strMask;
    // проверим длину строки
    int maxChars = constraints.getInt( TSID_MAX_LENGTH, 0 );
    if( maxChars > 0 && s.length() > maxChars ) {
      return ValidationResult.error( FMT_ERR_TOO_LONG_STRING, Integer.valueOf( maxChars ) );
    }
    return ValidationResult.SUCCESS;
  }
}