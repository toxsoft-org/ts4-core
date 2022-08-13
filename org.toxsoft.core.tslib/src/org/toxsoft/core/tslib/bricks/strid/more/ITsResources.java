package org.toxsoft.core.tslib.bricks.strid.more;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface ITsResources {

  /**
   * {@link IdChainUtils}
   */
  String FMT_ERR_EMPTY_CANOSTR             = "Canonical string '%s' must have at least on IDpath in chain";
  String FMT_ERR_INV_NTH_IDPATH_IN_CANOSTR = "Branch No %d of canonical string '%s' is not an IDpath";

  /**
   * {@link StridablesRegisrty}
   */
  String FMT_ERR_ITEM_ID_ALREAY_REGISTERED  = "Item with identifier '%s' is already regitered";
  String FMT_ERR_CANT_UNREG_BUILTIN_ITEM    = "Builtin item '%s' can not be unregistered";
  String FMT_ERR_CANT_UNREG_UNEXISTING_ITEM = "Can not unregister item '%s' - it was not registered";

}
