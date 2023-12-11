package org.toxsoft.core.tsgui.bricks.tin.tti;

import static org.toxsoft.core.tsgui.graphics.ITsGraphicsConstants.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.graphics.*;

// TODO дописать TtiTsFulcrum
public class TtiTsFulcrum
    extends AbstractTinTypeInfo<TsFulcrum> {

  /**
   * The type information singleton.
   */
  public static final TtiTsFulcrum INSTANCE = new TtiTsFulcrum();

  private TtiTsFulcrum() {
    super( ETinTypeKind.FULL, DT_TS_FULCRUM, TsFulcrum.class );

  }

  // ------------------------------------------------------------------------------------
  // AbstractTinTypeInfo
  //

  @Override
  protected ITinValue doGetNullTinValue() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected ITinValue doGetTinValue( TsFulcrum aEntity ) {
    // TODO Auto-generated method stub
    return null;
  }

}
