package org.toxsoft.core.tsgui.ved.impl;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.swtevents.*;
import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tsgui.ved.api.library.*;
import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Base implementation of {@link IVedEditorTool}.
 *
 * @author hazard157
 */
public abstract class VedAbstractEditorTool
    implements IVedEditorTool, IVedDisposable {

  private final IVedEditorToolProvider toolProvider;
  private final IVedEnvironment        vedEnv;
  private final IVedScreen             vedScreen;

  private boolean disposed = false;

  /**
   * Constructor.
   *
   * @param aProvider {@link IVedEditorToolProvider} - creator provider
   * @param aEnv {@link IVedEnvironment} - the VED framefork environment
   * @param aScreen {@link IVedScreen} - one of the screens in environment
   */
  public VedAbstractEditorTool( IVedEditorToolProvider aProvider, IVedEnvironment aEnv, IVedScreen aScreen ) {
    TsNullArgumentRtException.checkNulls( aProvider, aEnv, aScreen );
    toolProvider = aProvider;
    vedEnv = aEnv;
    vedScreen = aScreen;
  }

  // ------------------------------------------------------------------------------------
  // IStridable
  //

  @Override
  final public String id() {
    return toolProvider.id();
  }

  @Override
  final public String nmName() {
    return toolProvider.nmName();
  }

  @Override
  final public String description() {
    return toolProvider.description();
  }

  // ------------------------------------------------------------------------------------
  // IParameterized
  //

  @Override
  final public IOptionSet params() {
    return toolProvider.params();
  }

  // ------------------------------------------------------------------------------------
  // IIconIdable
  //

  @Override
  final public String iconId() {
    return toolProvider.iconId();
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  final public ITsGuiContext tsContext() {
    return vedEnv.tsContext();
  }

  // ------------------------------------------------------------------------------------
  // IVedDisposable
  //

  @Override
  final public boolean isDisposed() {
    return disposed;
  }

  @Override
  final public void dispose() {
    if( !disposed ) {
      disposed = true;
      doDispose();
    }
  }

  // ------------------------------------------------------------------------------------
  // IVedEditorTool
  //

  @Override
  final public IVedEditorToolProvider provider() {
    return toolProvider;
  }

  // ------------------------------------------------------------------------------------
  // API for subclasses
  //

  /**
   * Returns the owner VED screen.
   *
   * @return {@link IVedScreen} - the owner VED screen
   */
  public IVedScreen vedScreen() {
    return vedScreen;
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  /**
   * Subclass may perform additional resources cleaning when tool is disposed.
   */
  protected void doDispose() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // To implement
  //

  /**
   * Returns (if mouse is needed for tool) mouse events handler.
   *
   * @return {@link IVedViewDecorator} - mouse events handler or <code>null</code>
   */
  public abstract ISwtMouseListener mouseListener();

  /**
   * Returns (if keyboard is needed for tool) keyboard handler.
   *
   * @return {@link IVedViewDecorator} - keyboard events handler or <code>null</code>
   */
  public abstract ISwtKeyListener keyListener();

  /**
   * Returns (if any) screen decorator painter.
   *
   * @return {@link IVedViewDecorator} - screen decorator or <code>null</code>
   */
  public abstract IVedScreenDecorator screenDecorator();

  /**
   * Returns (if any) view decorator painter.
   *
   * @return {@link IVedViewDecorator} - views decorator or <code>null</code>
   */
  public abstract IVedViewDecorator viewDecorator();

}
