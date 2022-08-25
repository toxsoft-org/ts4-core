package org.toxsoft.core.tsgui.ved.std.comps;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.std.IVedStdProperties.*;
import static org.toxsoft.core.tsgui.ved.std.comps.ITsResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.fonts.impl.*;
import org.toxsoft.core.tsgui.graphics.lines.*;
import org.toxsoft.core.tsgui.valed.controls.graphics.*;
import org.toxsoft.core.tsgui.ved.core.*;
import org.toxsoft.core.tsgui.ved.core.impl.*;
import org.toxsoft.core.tsgui.ved.core.view.*;
import org.toxsoft.core.tsgui.ved.std.library.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;

/**
 * Стандартная компонента - текст в рамке.
 * <p>
 *
 * @author vs
 */
public class VedStdCompTextLabel
    extends VedAbstractComponent {

  /**
   * Component kind ID.
   */
  public static final String KIND_ID = "textLabel"; //$NON-NLS-1$

  /**
   * Parameter: label text.
   */
  public static final IDataDef OPDEF_TEXT = DataDef.create( "text", STRING, //$NON-NLS-1$
      TSID_NAME, STR_N_NAME, //
      TSID_DESCRIPTION, STR_D_NAME, //
      TSID_DEFAULT_VALUE, AV_STR_EMPTY //
  );

  /**
   * Parameter: label font.
   */
  public static final IDataDef OPDEF_FONT = DataDef.create( "font", VALOBJ, //$NON-NLS-1$
      TSID_NAME, STR_N_FONT, //
      TSID_DESCRIPTION, STR_D_FONT, //
      TSID_KEEPER_ID, FontInfo.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( new FontInfo( "Arial", 14, 0 ) ) // //$NON-NLS-1$
  );

  /**
   * Parameter: horizontal alignment.
   */
  public static final IDataDef OPDEF_HOR_ALIGN = DataDef.create( "horAlign", VALOBJ, //$NON-NLS-1$
      TSID_NAME, STR_N_HOR_ALIGN, //
      TSID_DESCRIPTION, STR_D_HOR_ALIGN, //
      TSID_KEEPER_ID, EHorAlignment.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( EHorAlignment.CENTER ) //
  );

  /**
   * Parameter: vertical alignment.
   */
  public static final IDataDef OPDEF_VER_ALIGN = DataDef.create( "verAlign", VALOBJ, //$NON-NLS-1$
      TSID_NAME, STR_N_VER_ALIGN, //
      TSID_DESCRIPTION, STR_D_VER_ALIGN, //
      TSID_KEEPER_ID, EVerAlignment.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( EVerAlignment.CENTER ) //
  );

  /**
   * Property: horizontal margin.
   */
  public static final IDataDef OPDEF_HOR_MARGIN = DataDef.create( "horMargin", FLOATING, //$NON-NLS-1$
      TSID_NAME, STR_N_HOR_MARGIN, //
      TSID_DESCRIPTION, STR_D_HOR_MARGIN, //
      TSID_DEFAULT_VALUE, AvUtils.avFloat( 4. ) //
  );

  /**
   * Property: vertical margin.
   */
  public static final IDataDef OPDEF_VER_MARGIN = DataDef.create( "verMargin", FLOATING, //$NON-NLS-1$
      TSID_NAME, STR_N_VER_MARGIN, //
      TSID_DESCRIPTION, STR_D_VER_MARGIN, //
      TSID_DEFAULT_VALUE, AvUtils.avFloat( 4. ) //
  );

  /**
   * Parameter: label border.
   */
  public static final IDataDef OPDEF_BORDER_INFO = DataDef.create( "borderInfo", VALOBJ, // //$NON-NLS-1$
      TSID_NAME, STR_N_BORDER_INFO, //
      TSID_DESCRIPTION, STR_D_BORDER_INFO, //
      TSID_KEEPER_ID, TsBorderInfo.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjTsBorderInfo.FACTORY_NAME, //
      TSID_DEFAULT_VALUE, avValobj( TsBorderInfo.DEFAULT )//
  );

  /**
   * Component provider singleton.
   */
  public static final VedAbstractComponentProvider PROVIDER =
      new VedAbstractComponentProvider( VedStdLibraryShapes.LIBRARY_ID, KIND_ID, OptionSetUtils.createOpSet( //
          TSID_NAME, STR_N_VSC_BORDER, //
          TSID_DESCRIPTION, STR_D_VSC_BORDER, //
          TSID_ICON_ID, ICONID_COMP_RECTANGLE //
      ), //
          PDEF_X, //
          PDEF_Y, //
          PDEF_WIDTH, //
          PDEF_HEIGHT, //
          PDEF_BG_COLOR, //
          PDEF_FG_COLOR, //
          PDEF_ROTATION_ANGLE, //
          OPDEF_TEXT, //
          OPDEF_FONT, //
          OPDEF_HOR_ALIGN, //
          OPDEF_VER_ALIGN, //
          OPDEF_HOR_MARGIN, //
          OPDEF_VER_MARGIN, //
          OPDEF_BORDER_INFO ) {

        @Override
        protected IVedComponent doCreateComponent( String aCompId, IVedEnvironment aEnvironment, IOptionSet aProps,
            IOptionSet aExtdata ) {
          IVedComponent c = new VedStdCompTextLabel( aCompId, aEnvironment );
          c.props().setProps( aProps );
          c.extdata().setAll( aExtdata );
          return c;
        }
      };

  /**
   * Constructor.
   *
   * @param aId String - component ID
   * @param aVedEnv {@link IVedEnvironment} - the VED envoronment
   */
  public VedStdCompTextLabel( String aId, IVedEnvironment aVedEnv ) {
    super( PROVIDER, aVedEnv, aId );
  }

  @Override
  protected VedAbstractComponentView doCreateView( IVedScreen aScreen ) {
    return new VedStdCompTextLabelView( this, aScreen );
  }

  void foo( GC aGc ) {
    Path path = new Path( Display.getCurrent() );
    path.addString( "Hello", 0, 0, aGc.getFont() );
    float[] coords = new float[4];
    path.getBounds( coords );

    // Pattern pattern = new Patt
    aGc.setBackgroundPattern( null );

    aGc.setBackgroundPattern( null );

    path.dispose();
  }

}
