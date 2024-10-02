package org.toxsoft.core.tsgui.m5.std.models.misc;

import static org.toxsoft.core.tsgui.ITsGuiConstants.*;
import static org.toxsoft.core.tsgui.m5.std.models.misc.ITsResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.utils.margins.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;

/**
 * M5-model of a {@link ITsMargins}.
 *
 * @author hazard157
 */
public class TsMarginsM5Model
    extends M5Model<ITsMargins> {

  /**
   * The model ID.
   */
  public static final String MODEL_ID = TSGUI_M5_ID + ".TsMargins"; //$NON-NLS-1$

  /**
   * ID of field {@link #LEFT}.
   */
  public static final String FID_LEFT = "left"; //$NON-NLS-1$

  /**
   * ID of field {@link #RIGHT}.
   */
  public static final String FID_RIGHT = "right"; //$NON-NLS-1$

  /**
   * ID of field {@link #TOP}.
   */
  public static final String FID_TOP = "top"; //$NON-NLS-1$

  /**
   * ID of field {@link #BOTTOM}.
   */
  public static final String FID_BOTTOM = "bottom"; //$NON-NLS-1$

  static final IDataType DT_MARGIN_VALUE = DataType.create( INTEGER, //
      TSID_MIN_INCLUSIVE, avInt( ITsMargins.VALUES_RANGE.minValue() ), //
      TSID_MAX_INCLUSIVE, avInt( ITsMargins.VALUES_RANGE.maxValue() ), //
      TSID_DEFAULT_VALUE, AV_0 //
  );

  /**
   * Attribute {@link ITsMargins#left()}
   */
  public final IM5AttributeFieldDef<ITsMargins> LEFT = new M5AttributeFieldDef<>( FID_LEFT, DT_MARGIN_VALUE, //
      TSID_NAME, STR_MERGIN_LEFT, //
      TSID_DESCRIPTION, STR_MERGIN_LEFT_D //
  ) {

    protected IAtomicValue doGetFieldValue( ITsMargins aEntity ) {
      return avInt( aEntity.left() );
    }
  };

  /**
   * Attribute {@link ITsMargins#right()}
   */
  public final IM5AttributeFieldDef<ITsMargins> RIGHT = new M5AttributeFieldDef<>( FID_RIGHT, DT_MARGIN_VALUE, //
      TSID_NAME, STR_MERGIN_RIGHT, //
      TSID_DESCRIPTION, STR_MERGIN_RIGHT_D //
  ) {

    protected IAtomicValue doGetFieldValue( ITsMargins aEntity ) {
      return avInt( aEntity.right() );
    }
  };

  /**
   * Attribute {@link ITsMargins#top()}
   */
  public final IM5AttributeFieldDef<ITsMargins> TOP = new M5AttributeFieldDef<>( FID_TOP, DT_MARGIN_VALUE, //
      TSID_NAME, STR_MERGIN_TOP, //
      TSID_DESCRIPTION, STR_MERGIN_TOP_D //
  ) {

    protected IAtomicValue doGetFieldValue( ITsMargins aEntity ) {
      return avInt( aEntity.top() );
    }
  };

  /**
   * Attribute {@link ITsMargins#bottom()}
   */
  public final IM5AttributeFieldDef<ITsMargins> BOTTOM = new M5AttributeFieldDef<>( FID_BOTTOM, DT_MARGIN_VALUE, //
      TSID_NAME, STR_MERGIN_BOTTOM, //
      TSID_DESCRIPTION, STR_MERGIN_BOTTOM_D //
  ) {

    protected IAtomicValue doGetFieldValue( ITsMargins aEntity ) {
      return avInt( aEntity.bottom() );
    }
  };

  /**
   * LM for M5-model, allows to create and edit margins but not delete or enumerate.
   *
   * @author hazard157
   */
  class LifecycleManager
      extends M5LifecycleManager<ITsMargins, Object> {

    public LifecycleManager( IM5Model<ITsMargins> aModel ) {
      super( aModel, true, true, false, false, null );
    }

    @Override
    protected ITsMargins doCreate( IM5Bunch<ITsMargins> aValues ) {
      int left = aValues.get( LEFT ).asInt();
      int right = aValues.get( RIGHT ).asInt();
      int top = aValues.get( TOP ).asInt();
      int bottom = aValues.get( BOTTOM ).asInt();
      return new TsMargins( left, right, top, bottom );
    }

    @Override
    protected ITsMargins doEdit( IM5Bunch<ITsMargins> aValues ) {
      return doCreate( aValues );
    }

  }

  /**
   * Constructor.
   */
  public TsMarginsM5Model() {
    super( MODEL_ID, ITsMargins.class );
    addFieldDefs( LEFT, RIGHT, TOP, BOTTOM );
  }

  @Override
  protected IM5LifecycleManager<ITsMargins> doCreateDefaultLifecycleManager() {
    return new LifecycleManager( this );
  }

  @Override
  protected IM5LifecycleManager<ITsMargins> doCreateLifecycleManager( Object aMaster ) {
    return getLifecycleManager( null );
  }

}
