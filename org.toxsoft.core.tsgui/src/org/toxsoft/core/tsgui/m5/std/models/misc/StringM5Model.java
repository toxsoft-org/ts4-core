package org.toxsoft.core.tsgui.m5.std.models.misc;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.m5.std.models.misc.ITsResources.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.av.*;

/**
 * Модель объектов типа {@link String}.
 *
 * @author goga
 */
public class StringM5Model
    extends M5Model<String> {

  /**
   * Идентификатор модели.
   */
  public static final String MODEL_ID = "ts.String"; //$NON-NLS-1$

  /**
   * Идентификатор поля {@link #STR}.
   */
  public static final String FID_STRING = "String"; //$NON-NLS-1$

  /**
   * Атрибут {@link String#toString()}.
   */
  public final M5AttributeFieldDef<String> STR = new M5AttributeFieldDef<>( FID_STRING, DDEF_STRING ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_N_STRING_STR, STR_D_STRING_STR );
      setDefaultValue( AV_STR_EMPTY );
      setFlags( M5FF_COLUMN );
    }

    @Override
    protected IAtomicValue doGetFieldValue( String aEntity ) {
      return avStr( aEntity );
    }
  };

  /**
   * Внутренный класс управления жизненным циклом.
   *
   * @author goga
   */
  private static class DefaultLifecyleManager
      extends M5LifecycleManager<String, Object> {

    public DefaultLifecyleManager( IM5Model<String> aModel ) {
      super( aModel, true, true, true, false, null );
    }

    @Override
    protected String doCreate( IM5Bunch<String> aValues ) {
      String str = aValues.getAsAv( FID_STRING ).asString();
      return str;
    }

    @Override
    protected String doEdit( IM5Bunch<String> aValues ) {
      return doCreate( aValues );
    }

    @Override
    protected void doRemove( String aEntity ) {
      // ничего не надо делать для удаления
    }

  }

  /**
   * Конструктор встроенной модели.
   */
  public StringM5Model() {
    super( MODEL_ID, String.class );
    setNameAndDescription( STR_N_M5M_STRING, STR_D_M5M_STRING );
    addFieldDefs( STR );
  }

  @Override
  protected IM5LifecycleManager<String> doCreateDefaultLifecycleManager() {
    return new DefaultLifecyleManager( this );
  }

}
