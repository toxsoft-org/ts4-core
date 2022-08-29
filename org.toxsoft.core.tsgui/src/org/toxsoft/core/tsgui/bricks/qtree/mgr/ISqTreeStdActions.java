package org.toxsoft.core.tsgui.bricks.qtree.mgr;

import static org.toxsoft.core.tsgui.bricks.qtree.mgr.ITsResources.*;
import static org.toxsoft.core.tslib.ITsHardConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tslib.av.impl.*;

/**
 * QMode based visual tree standart actions, categories and other constants.
 *
 * @author hazard157
 */
public interface ISqTreeStdActions {

  /**
   * Perfix of SkIDE visual tree actions.
   */
  String ACTID_PREFIX = TS_ID + ".qtree.act"; //$NON-NLS-1$

  /**
   * Perfix of SkIDE visual tree action meta-info options.
   */
  String OPID_PREFIX = TS_ID + ".qtree.op"; //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Actions

  /**
   * ID of action {@link #ACDEF_OPEN_DEFAULT}.
   */
  String ACTID_OPEN_DEFAULT = ACTID_PREFIX + ".open.default"; //$NON-NLS-1$

  /**
   * Action: open the node in the default editor.
   */
  ITsActionDef ACDEF_OPEN_DEFAULT = TsActionDef.ofPush1( ACTID_OPEN_DEFAULT, //
      TSID_NAME, STR_N_OPEN_DEFAULT, //
      TSID_DESCRIPTION, STR_D_OPEN_DEFAULT //
  );

  /**
   * стандартные команды (open, refresh, CRUD,...) и узлы его повторяют<br>
   * Params for ActionDef.params(): SKIDE_CATEG (CRUD, OPEN_WITH etc)<br>
   */

  // ------------------------------------------------------------------------------------
  // Action meta-info

  /**
   * ID of option {@link #OPDEF_IS_OPEN_WITH_ACTION}.
   */
  String OPID_IS_OPEN_WITH_ACTION = OPID_PREFIX + ".IsOpenWithAction"; //$NON-NLS-1$

  /**
   * Option for {@link ITsActionDef#params()}: this action opens node in some editor.
   */
  DataDef OPDEF_IS_OPEN_WITH_ACTION = DataDef.create( OPID_IS_OPEN_WITH_ACTION, BOOLEAN, //
      TSID_DEFAULT_VALUE, AV_FALSE //
  );

  /**
   * ID of option {@link #OPDEF_IS_SPREAD_DOWN}.
   */
  String OPID_IS_SPREAD_DOWN = OPID_PREFIX + ".IsSpreadDown"; //$NON-NLS-1$

  /**
   * Option for {@link ITsActionDef#params()}: this action will also appear on subtree nodes.
   */
  DataDef OPDEF_IS_SPREAD_DOWN = DataDef.create( OPID_IS_SPREAD_DOWN, BOOLEAN, //
      TSID_DEFAULT_VALUE, AV_FALSE //
  );

}
