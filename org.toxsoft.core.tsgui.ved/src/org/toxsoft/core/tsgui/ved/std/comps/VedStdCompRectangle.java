package org.toxsoft.core.tsgui.ved.std.comps;

import static org.toxsoft.core.tsgui.ved.std.comps.ITsResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tsgui.ved.impl.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;

/**
 * VED staandard component: filled rectangle.
 *
 * @author hazard157, vs
 */
public class VedStdCompRectangle
    extends VedAbstractComponent {

  /**
   * ID of property {@link #PDEF_FG_COLOR}.
   */
  String PID_FG_COLOR = "fgColor"; //$NON-NLS-1$

  /**
   * Property: components X coordinate.
   */
  IDataDef PDEF_FG_COLOR = DataDef.create( PID_FG_COLOR, VALOBJ, //
      TSID_NAME, STR_N_FG_COLOR, //
      TSID_DESCRIPTION, STR_D_FG_COLOR, //
      TSID_DEFAULT_VALUE, AV_F_0 //
  );

  /**
   * ID of property {@link #PDEF_BG_COLOR}.
   */
  String PID_BG_COLOR = "bgColor"; //$NON-NLS-1$

  /**
   * Property: components X coordinate.
   */
  IDataDef PDEF_BG_COLOR = DataDef.create( PID_BG_COLOR, VALOBJ, //
      TSID_NAME, STR_N_BG_COLOR, //
      TSID_DESCRIPTION, STR_D_BG_COLOR, //
      TSID_DEFAULT_VALUE, AV_F_0 //
  );

  static class StdRectView
      extends VedAbstractComponentView {

    Color bkColor;
    Color fgColor;

    StdRectView( VedStdCompRectangle aOwner ) {
      super( aOwner );
    }

    @Override
    public IVedPainter painter() {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public IVedPorter porter() {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public IVedOutline outline() {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    protected void doDispose() {
      // TODO Auto-generated method stub
    }

    private void update() {
      fgColor =  owner().props()
    }

  }

  /**
   * Конструктор.<br>
   *
   * @param aId String - идентификатор
   * @param aProvider VedAbstractComponentProvider - поставщик компонент
   */
  public VedStdCompRectangle( String aId, VedAbstractComponentProvider aProvider ) {
    super( aProvider, aId );
    // TODO Auto-generated constructor stub
  }

  @Override
  public IVedComponentView createView( IVedScreen aScreen ) {
    // TODO Auto-generated method stub
    return super.createView( aScreen );
  }

}
