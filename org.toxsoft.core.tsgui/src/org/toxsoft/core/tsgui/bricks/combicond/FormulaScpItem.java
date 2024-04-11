package org.toxsoft.core.tsgui.bricks.combicond;

import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.math.combicond.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The logical formula keyword containing {@link ISingleCondParams}.
 *
 * @author hazard157
 */
final class FormulaScpItem {

  private final String            keyword;
  private final ISingleCondParams scp;

  public FormulaScpItem( String aKeyword, ISingleCondParams aScp ) {
    StridUtils.checkValidIdPath( aKeyword );
    TsNullArgumentRtException.checkNull( aScp );
    keyword = aKeyword;
    scp = aScp;
  }

  public String keyword() {
    return keyword;
  }

  public ISingleCondParams scp() {
    return scp;
  }

  @Override
  public String toString() {
    return String.format( "%s - '%s'", keyword, scp.toString() ); //$NON-NLS-1$
  }

}
