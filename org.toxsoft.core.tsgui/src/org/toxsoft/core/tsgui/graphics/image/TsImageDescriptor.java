package org.toxsoft.core.tsgui.graphics.image;

import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import java.io.*;

import org.toxsoft.core.tsgui.graphics.image.impl.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.coll.synch.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The information how to create the {@link TsImage} from the different sources.
 * <p>
 * This is an immutable class.
 *
 * @author hazard157
 */
public final class TsImageDescriptor
    implements IParameterized, Serializable {

  private static final long serialVersionUID = 8007296849090328037L;

  /**
   * Registered keeper ID.
   */
  public static final String KEEPER_ID = "TsImageDescriptor"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<TsImageDescriptor> KEEPER =
      new AbstractEntityKeeper<>( TsImageDescriptor.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, TsImageDescriptor aEntity ) {
          aSw.writeAsIs( aEntity.kindId() );
          aSw.writeSeparatorChar();
          OptionSetKeeper.KEEPER.write( aSw, aEntity.params() );
        }

        @Override
        protected TsImageDescriptor doRead( IStrioReader aSr ) {
          String kindId = aSr.readIdPath();
          aSr.ensureSeparatorChar();
          IOptionSet params = OptionSetKeeper.KEEPER.read( aSr );
          return new TsImageDescriptor( kindId, params, 0 );
        }
      };

  private static final IStringMapEdit<ITsImageSourceKind> kindsById = new SynchronizedStringMap<>( new StringMap<>() );

  {
    kindsById.put( TsImageSourceKindNone.KIND_ID, TsImageSourceKindNone.INSTANCE );
    kindsById.put( TsImageSourceKindPlugin.KIND_ID, TsImageSourceKindPlugin.INSTANCE );
    kindsById.put( TsImageSourceKindTsIcon.KIND_ID, TsImageSourceKindTsIcon.INSTANCE );
    kindsById.put( TsImageSourceKindUrl.KIND_ID, TsImageSourceKindUrl.INSTANCE );
  }

  /**
   * Constant of the absent image.
   * <p>
   * In fact the unknown image {@link ITsImageManager#createUnknownImage(int)} will be displayed.
   */
  public static final TsImageDescriptor NONE = new TsImageDescriptor( TsImageSourceKindNone.KIND_ID, IOptionSet.NULL );

  private final String     kindId;
  private final IOptionSet params;
  private final String     uniqueName;

  /**
   * Constructor.
   *
   * @param aKindId String - the image source kind ID (an IDpath)
   * @param aParams {@link IOptionSet} - image creation parameters, specific for the kind of source
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public TsImageDescriptor( String aKindId, IOptionSet aParams ) {
    this( StridUtils.checkValidIdPath( aKindId ), new OptionSet( aParams ), 0 );
  }

  private TsImageDescriptor( String aKindId, IOptionSet aParams, @SuppressWarnings( "unused" ) int aFoo ) {
    kindId = aKindId;
    params = aParams;
    AbstractTsImageSourceKind k = (AbstractTsImageSourceKind)kindsById.findByKey( aKindId );
    if( k != null ) {
      uniqueName = k.uniqueImageNameString( aParams );
    }
    else {
      uniqueName = EMPTY_STRING;
    }
  }

  /**
   * Static constructor.
   *
   * @param aKindId String - the image source kind ID (an IDpath)
   * @param aIdsAndValues Object[] - parameters as the identifier / value pairs
   * @return {@link TsImageDescriptor} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public static TsImageDescriptor create( String aKindId, Object... aIdsAndValues ) {
    return new TsImageDescriptor( StridUtils.checkValidIdPath( aKindId ), OptionSetUtils.createOpSet( aIdsAndValues ) );
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
   * Parameters contains options specific to each kind.<br>
   * {@inheritDoc}
   */
  @Override
  public IOptionSet params() {
    return params;
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
   * Returns all registered image source kinds.
   * <p>
   * To be usable, the descriptor kind ID must be the ID of the registered kind
   * {@link TsImageDescriptor#getImageSourceKindsMap()}.
   *
   * @return {@link IStringMap}&lt;{@link ITsImageSourceKind}&gt; - the map "kind ID" - "the image source kind"
   */
  public static IStringMap<ITsImageSourceKind> getImageSourceKindsMap() {
    return kindsById;
  }

  /**
   * Finds source kind of the specified descriptor.
   * <p>
   * If <code>aImageDescriptor</code> is <code>null</code> corresponding source kind is not registered method returns
   * the <code>aDefaultKind</code>.
   *
   * @param aImageDescriptor {@link TsImageDescriptor} - the descriptor, may be <code>null</code>
   * @param aDefaultKind {@link ITsImageSourceKind} - the default kind, may be <code>null</code>
   * @return {@link ITsImageSourceKind} - the source kind or <code>null</code>
   */
  public static ITsImageSourceKind findKind( TsImageDescriptor aImageDescriptor, ITsImageSourceKind aDefaultKind ) {
    if( aImageDescriptor != null ) {
      ITsImageSourceKind k = kindsById.findByKey( aImageDescriptor.kindId() );
      if( k != null ) {
        return k;
      }
    }
    return aDefaultKind;
  }

  /**
   * Registers the source kind.
   * <p>
   * Replaces previous registration of the same {@link ITsImageSourceKind#id()}.
   *
   * @param aImageSourceKind {@link ITsImageSourceKind} - the kind to register
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static void registerImageSourceKind( ITsImageSourceKind aImageSourceKind ) {
    TsNullArgumentRtException.checkNull( aImageSourceKind );
    kindsById.put( aImageSourceKind.id(), aImageSourceKind );
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    if( kindId.equals( TsImageSourceKindNone.INSTANCE.id() ) ) {
      return kindId;
    }
    ITsImageSourceKind kind = getImageSourceKindsMap().findByKey( kindId );
    if( kind != null ) {
      return kind.humanReadableString( params );
    }
    return kindId + ": " + params.toString(); //$NON-NLS-1$
  }

  @Override
  public boolean equals( Object aThat ) {
    if( aThat == this ) {
      return true;
    }
    if( aThat instanceof TsImageDescriptor that ) {
      if( this.kindId.equals( that.kindId ) ) {
        if( !this.uniqueName.isEmpty() && !that.uniqueName.isEmpty() ) {
          return this.uniqueName.equals( that.uniqueName );
        }
        return this.params.equals( that.params );
      }
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + kindId.hashCode();
    if( !this.uniqueName.isEmpty() ) {
      result = TsLibUtils.PRIME * result + uniqueName.hashCode();
    }
    else {
      result = TsLibUtils.PRIME * result + params.hashCode();
    }
    return result;
  }

}
