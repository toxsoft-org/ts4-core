package org.toxsoft.core.tsgui.ved.extra.tools;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.core.*;
import org.toxsoft.core.tsgui.ved.core.library.*;
import org.toxsoft.core.tsgui.ved.core.view.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * Базовый класс для "инструментов" редактора.
 *
 * @author vs
 */
public abstract class VedAbstractEditorTool
    extends StridableParameterized
    implements IVedEditorTool {

  /**
   * ИД опции - "Имя группы"
   */
  public final static String OPID_GROUP = "goupId"; //$NON-NLS-1$

  /**
   * ИД опции - "ИД библиотеки"
   */
  public final static String OPID_LIBRARY = "libraryId"; //$NON-NLS-1$

  /**
   * ИД опции - "ИД компоненты"
   */
  public final static String OPID_COMPONENT = "componentId"; //$NON-NLS-1$

  private boolean disposed = false;

  private IVedScreen vedScreen;

  private final IVedEnvironment vedEnv;

  private final IVedComponentProvider compProvider;

  protected VedAbstractEditorTool( String aId, IOptionSet aParams, IVedScreen aVedScreen, IVedEnvironment aVedEnv,
      IVedComponentProvider aCompProvider ) {
    super( aId, aParams );
    vedScreen = aVedScreen;
    vedEnv = aVedEnv;
    compProvider = aCompProvider;
  }

  // ------------------------------------------------------------------------------------
  // IIconIdable
  //

  @Override
  public String iconId() {
    return params().getStr( TSID_ICON_ID );
  }

  // ------------------------------------------------------------------------------------
  // IVedContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return vedEnv.tsContext();
  }

  // ------------------------------------------------------------------------------------
  // IDisposable
  //

  @Override
  public final boolean isDisposed() {
    return disposed;
  }

  @Override
  public final void dispose() {
    if( !disposed ) {
      disposed = true;
      doDispose();
    }
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * ИД группы или пустая строка если инструмент не принадлежит никакой группе.
   *
   * @return String - ИД группы или пустая строка
   */
  public String groupId() {
    if( params().hasKey( OPID_GROUP ) ) {
      return params().getStr( OPID_GROUP );
    }
    return TsLibUtils.EMPTY_STRING;
  }

  // ------------------------------------------------------------------------------------
  // to use
  //

  @Override
  public final IVedComponentProvider componentProvider() {
    return compProvider;
  }

  @Override
  public IVedScreen vedScreen() {
    return vedScreen;
  }

  // ------------------------------------------------------------------------------------
  // toOverride
  //

  protected void doDispose() {
    // nop
  }
}
