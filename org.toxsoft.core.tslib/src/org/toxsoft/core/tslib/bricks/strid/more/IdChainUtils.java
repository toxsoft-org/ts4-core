package org.toxsoft.core.tslib.bricks.strid.more;

import static org.toxsoft.core.tslib.bricks.strid.more.ITsResources.*;

import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Methods for {@link IdChain} manipulation.
 *
 * @author hazard157
 */
public class IdChainUtils {

  /**
   * Validates {@link String} value to be valid {@link IdChain} canonical representation.
   * <p>
   * Does not allows <code>null</code> value.
   */
  public static final ITsValidator<String> CANONICAL_STRING_VALIDATOR = aValue -> {
    TsNullArgumentRtException.checkNull( aValue );
    String[] ss = aValue.split( IdChain.STR_BRANCH_SEPARATOR, -1 );
    if( ss.length == 0 ) {
      return ValidationResult.error( FMT_ERR_EMPTY_CANOSTR, aValue );
    }
    for( int i = 0; i < ss.length; i++ ) {
      String s = ss[i];
      if( !StridUtils.isValidIdPath( s ) ) {
        return ValidationResult.error( FMT_ERR_INV_NTH_IDPATH_IN_CANOSTR, Integer.valueOf( i + 1 ), aValue );
      }
    }
    return ValidationResult.SUCCESS;
  };

  /**
   * TODO IdChain static API:<br>
   * IdChain add(IdChain1,IdChain2), IdChain getParent(IdChain), boolean isChild() <br>
   * ... etc
   */

  /**
   * No subclassing.
   */
  private IdChainUtils() {
    // nop
  }

}
