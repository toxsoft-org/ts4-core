package org.toxsoft.core.tsgui.bricks.actions;

import static org.toxsoft.core.tslib.ITsHardConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.jface.action.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;

/**
 * Constants used by {@link TsAction} / {@link ITsActionDef} package.
 *
 * @author hazard157
 */
public interface ITsActionConstants {

  /**
   * ID of option {@link #OPDEF_CHECKED_ICON_ID}.
   */
  String OPID_CHECKED_ICON_ID = TS_ID + ".tsgui.actop.CheckedIconId"; //$NON-NLS-1$

  /**
   * ID of option {@link #OPDEF_CHECKED_TEXT}.
   */
  String OPID_CHECKED_TEXT = TS_ID + ".tsgui.actop.CheckedText"; //$NON-NLS-1$

  /**
   * ID of option {@link #OPDEF_CHECKED_TOOLTIP}.
   */
  String OPID_CHECKED_TOOLTIP = TS_ID + ".tsgui.actop.CheckedTooltip"; //$NON-NLS-1$

  /**
   * {@link ITsActionDef#params()} option: ID of the icon in checked state for {@link IAction#AS_CHECK_BOX}.
   */
  IDataDef OPDEF_CHECKED_ICON_ID = DataDef.create( OPID_CHECKED_ICON_ID, STRING, //
      TSID_DEFAULT_VALUE, IAtomicValue.NULL );

  /**
   * {@link ITsActionDef#params()} option: the text in checked state for {@link IAction#AS_CHECK_BOX}.
   */
  IDataDef OPDEF_CHECKED_TEXT = DataDef.create( OPID_CHECKED_TEXT, STRING, //
      TSID_DEFAULT_VALUE, IAtomicValue.NULL );

  /**
   * {@link ITsActionDef#params()} option: the tooltip text in checked state for {@link IAction#AS_CHECK_BOX}.
   */
  IDataDef OPDEF_CHECKED_TOOLTIP = DataDef.create( OPID_CHECKED_TOOLTIP, STRING, //
      TSID_DEFAULT_VALUE, IAtomicValue.NULL );

}
