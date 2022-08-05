package org.toxsoft.core.tsgui.ved.glib.comps.tools;

import org.toxsoft.core.tsgui.ved.core.view.*;
import org.toxsoft.core.tsgui.ved.extra.tools.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * Группа инструментов редактора.
 * <p>
 *
 * @author vs
 */
public class VedToolsGroup
    extends Stridable
    implements IVedToolsGroup {

  private IVedEditorTool selectedTool = null;

  private final IStridablesListEdit<IVedEditorTool> toolsList = new StridablesList<>();

  /**
   * Конструктор.<br>
   *
   * @param aId String - ИД группы
   */
  VedToolsGroup( String aId ) {
    super( aId, TsLibUtils.EMPTY_STRING, TsLibUtils.EMPTY_STRING );
  }

  /**
   * Конструктор.<br>
   *
   * @param aId String - ИД группы
   * @param aDescription String - описание группы
   */
  public VedToolsGroup( String aId, String aDescription ) {
    super( aId, TsLibUtils.EMPTY_STRING, aDescription );
  }

  // ------------------------------------------------------------------------------------
  // {@link IVedToolsGroup}
  //

  @Override
  public IStridablesList<IVedEditorTool> listTools() {
    return toolsList;
  }

  @Override
  public IVedEditorTool selectedTool() {
    return selectedTool;
  }

  @Override
  public void selectTool( IVedEditorTool aTool ) {
    selectedTool = aTool;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Добавляет инструмент в группу
   *
   * @param aTool IVedEditorTool - добавляемый инструмент
   */
  public void addTool( IVedEditorTool aTool ) {
    toolsList.add( aTool );
    if( selectedTool == null ) {
      selectedTool = aTool;
    }
  }

}
