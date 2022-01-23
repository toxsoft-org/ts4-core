package org.toxsoft.tsgui.m5_3.std.models.enums;

import static org.toxsoft.tsgui.m5_3.IM5Constants.*;
import static org.toxsoft.tsgui.m5_3.std.models.enums.ITsResource.*;
import static org.toxsoft.tslib.av.EAtomicType.*;
import static org.toxsoft.tslib.av.impl.AvUtils.*;

import org.toxsoft.tsgui.m5_3.model.IM5AttributeFieldDef;
import org.toxsoft.tsgui.m5_3.model.IM5LifecycleManager;
import org.toxsoft.tsgui.m5_3.model.impl.M5AttributeFieldDef;
import org.toxsoft.tsgui.m5_3.model.impl.M5Model;
import org.toxsoft.tslib.av.IAtomicValue;
import org.toxsoft.tslib.bricks.strid.IStridable;
import org.toxsoft.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Superclass for all M5-models of the java <code>enum</code>.
 * <p>
 * For enums implementing {@link IStridable} the base class {@link M5StridableEnumModelBase} should be used.
 *
 * @author hazard157
 * @param <T> - modelled <code>enum</code> class
 */
public class M5EnumModelBase<T extends Enum<T>>
    extends M5Model<T> {

  /**
   * ID of attribute {@link #ORDINAL}.
   */
  public static final String FID_ORDINAL = "Ordinal"; //$NON-NLS-1$

  /**
   * ID of attribute {@link #JAVA_NAME}.
   */
  public static final String FID_JAVA_NAME = "JavaName"; //$NON-NLS-1$

  /**
   * Attribute {@link Enum#ordinal()}.
   */
  public final IM5AttributeFieldDef<T> ORDINAL = new M5AttributeFieldDef<>( FID_ORDINAL, INTEGER ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_N_ORDINAL, STR_D_ORDINAL );
      setFlags( M5FF_HIDDEN );
    }

    @Override
    protected IAtomicValue doGetFieldValue( T aEntity ) {
      return avInt( aEntity.ordinal() );
    }

  };

  /**
   * Attribute {@link Enum#name()}.
   */
  public final IM5AttributeFieldDef<T> JAVA_NAME = new M5AttributeFieldDef<>( FID_JAVA_NAME, STRING ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_N_JAVA_NAME, STR_D_JAVA_NAME );
      setFlags( M5FF_COLUMN );
    }

    @Override
    protected IAtomicValue doGetFieldValue( T aEntity ) {
      return avStr( aEntity.name() );
    }

  };

  /**
   * Constructor.
   *
   * @param aId String - model ID
   * @param aEntityClass {@link Class}&lt;T&gt; - modelled enum class
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException identifier is not an IDpath
   * @throws TsIllegalArgumentRtException modelled entity is not Java <code>enum</code>
   */
  public M5EnumModelBase( String aId, Class<T> aEntityClass ) {
    super( aId, aEntityClass );
    TsIllegalArgumentRtException.checkFalse( aEntityClass.isEnum() );
    addFieldDefs( ORDINAL, JAVA_NAME );
    setName( aEntityClass.getSimpleName() );
    setDescription( aEntityClass.getName() );
  }

  @Override
  protected IM5LifecycleManager<T> doCreateDefaultLifecycleManager() {
    return new M5EnumLifecycleManager<>( this );
  }

  @Override
  protected IM5LifecycleManager<T> doCreateLifecycleManager( Object aMaster ) {
    return getLifecycleManager( null );
  }

}
