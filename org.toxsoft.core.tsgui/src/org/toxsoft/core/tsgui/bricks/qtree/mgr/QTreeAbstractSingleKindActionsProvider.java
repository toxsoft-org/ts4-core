package org.toxsoft.core.tsgui.bricks.qtree.mgr;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.txtmatch.*;

/**
 * {@link IQTreeNodeActionsProvider} for single kind of node.
 *
 * @author hazard157
 */
public abstract class QTreeAbstractSingleKindActionsProvider
    extends QTreeAbstractTreeNodeActionsProvider {

  private final TextMatcher textMatcher;

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aNodeKindId String kind ID of served nodes
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException kind ID is not an IDpath
   */
  public QTreeAbstractSingleKindActionsProvider( ITsGuiContext aContext, String aNodeKindId ) {
    super( aContext );
    StridUtils.checkValidIdPath( aNodeKindId );
    textMatcher = new TextMatcher( ETextMatchMode.EXACT, aNodeKindId );
  }

  @Override
  public ITsFilter<String> nodeKindIdFilter() {
    return textMatcher;
  }

}
