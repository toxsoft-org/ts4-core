package org.toxsoft.core.tslib.av.avtree;

/**
 * Локализуемые ресурсы деревьев значений.
 *
 * @author goga
 */
interface ITsResources {

  /**
   * {@link AvTreeArray}
   */
  String STR_ERR_NOT_A_SINGLE_TREE            = Messages.getString( "STR_ERR_NOT_A_SINGLE_TREE" );            //$NON-NLS-1$
  String STR_ERR_NOT_A_ARRAY_TREE             = Messages.getString( "STR_ERR_NOT_A_ARRAY_TREE" );             //$NON-NLS-1$
  String STR_ERR_CANT_ADD_ARRAY_TREE_TO_ARRAY = Messages.getString( "STR_ERR_CANT_ADD_ARRAY_TREE_TO_ARRAY" ); //$NON-NLS-1$

  /**
   * {@link AvTreeUtils}
   */
  String STR_ERR_TREE_ARRAY_EXPECTED_IN_PATH    = Messages.getString( "STR_ERR_TREE_ARRAY_EXPECTED_IN_PATH" );    //$NON-NLS-1$
  String STR_ERR_SINGLE_TREE_EXPECTED_IN_PATH   = Messages.getString( "STR_ERR_SINGLE_TREE_EXPECTED_IN_PATH" );   //$NON-NLS-1$
  String STR_ERR_ARRAY_REF_CANT_BE_IN_INFO_PATH = Messages.getString( "STR_ERR_ARRAY_REF_CANT_BE_IN_INFO_PATH" ); //$NON-NLS-1$

}
