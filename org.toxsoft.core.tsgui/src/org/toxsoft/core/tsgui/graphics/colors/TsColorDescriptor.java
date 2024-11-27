package org.toxsoft.core.tsgui.graphics.colors;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import java.io.*;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.coll.synch.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The information how to create the {@link Color} from the different sources.
 * <p>
 * This is an immutable class.
 *
 * @author hazard157
 */
public class TsColorDescriptor
    implements IParameterized, Serializable {

  // TODO какой UID должен быть?
  private static final long serialVersionUID = 8007296849090328037L;

  /**
   * Registered keeper ID.
   */
  public static final String KEEPER_ID = "TsColorDescriptor"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<TsColorDescriptor> KEEPER =
      new AbstractEntityKeeper<>( TsColorDescriptor.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, TsColorDescriptor aEntity ) {
          aSw.writeAsIs( aEntity.kindId() );
          aSw.writeSeparatorChar();
          OptionSetKeeper.KEEPER.write( aSw, aEntity.params() );
        }

        @Override
        protected TsColorDescriptor doRead( IStrioReader aSr ) {
          String kindId = aSr.readIdPath();
          aSr.ensureSeparatorChar();
          IOptionSet params = OptionSetKeeper.KEEPER.read( aSr );
          return new TsColorDescriptor( kindId, params );
        }
      };

  private static final IStringMapEdit<ITsColorSourceKind> kindsById = new SynchronizedStringMap<>( new StringMap<>() );

  {
    kindsById.put( TsColorSourceKindRgba.KIND_ID, TsColorSourceKindRgba.INSTANCE );
    kindsById.put( TsColorSourceKindTsColor.KIND_ID, TsColorSourceKindTsColor.INSTANCE );
    kindsById.put( TsColorSourceKindSystemColor.KIND_ID, TsColorSourceKindSystemColor.INSTANCE );
  }

  /**
   * Constant of the absent color.
   * <p>
   * In fact transparent color will be used.
   */
  public static final TsColorDescriptor NONE = new TsColorDescriptor( TsColorSourceKindRgba.KIND_ID, //
      OptionSetUtils.createOpSet( TsColorSourceKindRgba.OPDEF_RGBA, avValobj( new RGBA( 0, 0, 0, 0 ) ) ) );

  private final String     kindId;
  private final IOptionSet params;
  private final String     uniqueName;

  /**
   * Constructor.
   *
   * @param aKindId String - the color source kind ID (an IDpath)
   * @param aParams {@link IOptionSet} - color creation parameters, specific for the kind of source
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public TsColorDescriptor( String aKindId, IOptionSet aParams ) {
    kindId = aKindId;
    params = aParams;
    AbstractTsColorSourceKind k = (AbstractTsColorSourceKind)kindsById.findByKey( aKindId );
    if( k != null ) {
      uniqueName = k.uniqueColorNameString( aParams );
    }
    else {
      uniqueName = EMPTY_STRING;
    }
  }

  // ------------------------------------------------------------------------------------
  // IParameterized
  //

  /**
   * Parameters contains options specific to each kind.<br>
   * {@inheritDoc}
   */
  @Override
  public IOptionSet params() {
    return params;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the ID of the image source kind.
   *
   * @return String - the kind ID (an IDname)
   */
  public String kindId() {
    return kindId;
  }

  /**
   * Return color.
   *
   * @param aDisplay {@link Display} - the display
   * @return {@link Color} - color
   */
  public Color color( Display aDisplay ) {
    AbstractTsColorSourceKind k = (AbstractTsColorSourceKind)kindsById.findByKey( kindId );
    return k.createColor( this, aDisplay );
  }

  /**
   * Returns color components.
   *
   * @param aDisplay {@link Display} - the display
   * @return {@link RGBA} - color componens
   */
  public RGBA rgba( Display aDisplay ) {
    Color c = color( aDisplay );
    return c.getRGBA();
  }

  // ------------------------------------------------------------------------------------
  // package API
  //

  String uniqueName() {
    return uniqueName;
  }

  // ------------------------------------------------------------------------------------
  // Kinds registry static API
  //

  /**
   * Returns all registered color source kinds.
   * <p>
   * To be usable, the descriptor kind ID must be the ID of the registered kind
   * {@link TsColorDescriptor#getColorSourceKindsMap()}.
   *
   * @return {@link IStringMap}&lt;{@link ITsColorSourceKind}&gt; - the map "kind ID" - "the color source kind"
   */
  public static IStringMap<ITsColorSourceKind> getColorSourceKindsMap() {
    return kindsById;
  }

  /**
   * Finds source kind of the specified descriptor.
   * <p>
   * If <code>aColorDescriptor</code> is <code>null</code> corresponding source kind is not registered method returns
   * the <code>aDefaultKind</code>.
   *
   * @param aColorDescriptor {@link TsColorDescriptor} - the descriptor, may be <code>null</code>
   * @param aDefaultKind {@link ITsColorSourceKind} - the default kind, may be <code>null</code>
   * @return {@link ITsColorSourceKind} - the source kind or <code>null</code>
   */
  public static ITsColorSourceKind findKind( TsColorDescriptor aColorDescriptor, ITsColorSourceKind aDefaultKind ) {
    if( aColorDescriptor != null ) {
      ITsColorSourceKind k = kindsById.findByKey( aColorDescriptor.kindId() );
      if( k != null ) {
        return k;
      }
    }
    return aDefaultKind;
  }

  /**
   * Registers the source kind.
   * <p>
   * Replaces previous registration of the same {@link ITsColorSourceKind#id()}.
   *
   * @param aColorSourceKind {@link ITsColorSourceKind} - the kind to register
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static void registerColorSourceKind( ITsColorSourceKind aColorSourceKind ) {
    TsNullArgumentRtException.checkNull( aColorSourceKind );
    kindsById.put( aColorSourceKind.id(), aColorSourceKind );
  }

}
