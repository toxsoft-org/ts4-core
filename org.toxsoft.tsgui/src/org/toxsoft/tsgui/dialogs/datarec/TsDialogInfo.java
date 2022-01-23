package org.toxsoft.tsgui.dialogs.datarec;

import static org.toxsoft.tsgui.dialogs.ITsGuiDialogResources.*;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.toxsoft.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.tsgui.bricks.ctx.ITsGuiContextable;
import org.toxsoft.tslib.bricks.geometry.ITsPoint;
import org.toxsoft.tslib.bricks.geometry.impl.TsPoint;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * {@link ITsDialogInfo} implementation.
 *
 * @author goga
 */
public class TsDialogInfo
    implements ITsDialogInfo, ITsGuiContextable {

  /**
   * Min values of {@link #setMinSizeShellRelative(int, int)} and {@link #setMaxSizeShellRelative(int, int)} arguments.
   */
  public static final int MIN_SHELL_RELATIVE_PERCENT = 20;

  /**
   * Max values of {@link #setMinSizeShellRelative(int, int)} and {@link #setMaxSizeShellRelative(int, int)} arguments.
   */
  public static final int MAX_SHELL_RELATIVE_PERCENT = 500;

  private final ITsGuiContext tsContext;
  private final Shell         shell;
  private final String        caption;
  private final String        title;
  private final int           flags;

  private ITsPoint minSize = ITsPoint.ZERO;
  private ITsPoint maxSize = ITsPoint.ZERO;

  /**
   * Constructor
   *
   * @param aContext {@link ITsGuiContext} - GUI context
   * @param aShell {@link Shell} - parent window or <code>null</code> for default shell
   * @param aCaption String - window caption
   * @param aTitle String - dialog title area text
   * @param aFlags int - флаги диалога {@link ITsDialogInfo}<b>.DF_XXX</b>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TsDialogInfo( ITsGuiContext aContext, Shell aShell, String aCaption, String aTitle, int aFlags ) {
    TsNullArgumentRtException.checkNulls( aContext, aCaption, aTitle );
    tsContext = aContext;
    if( aShell != null ) {
      shell = aShell;
    }
    else {
      shell = getShell();
    }
    caption = aCaption;
    title = aTitle;
    flags = aFlags;
    setMinSizeShellRelative( 20, 20 );
    setMaxSizeShellRelative( 80, 90 );
  }

  /**
   * Constructor with default shell and 0 flags.
   *
   * @param aContext {@link ITsGuiContext} - GUI context
   * @param aCaption String - window caption
   * @param aTitle String - dialog title area text
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TsDialogInfo( ITsGuiContext aContext, String aCaption, String aTitle ) {
    this( aContext, null, aCaption, aTitle, 0 );
  }

  /**
   * Creates default dialog info for new entity creation.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @return {@link TsDialogInfo} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static TsDialogInfo forCreateEntity( ITsGuiContext aContext ) {
    return new TsDialogInfo( aContext, null, DLG_C_NEW_OBJ, DLG_T_NEW_OBJ, 0 );
  }

  /**
   * Creates default dialog info for existing entity editing.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @return {@link TsDialogInfo} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static TsDialogInfo forEditEntity( ITsGuiContext aContext ) {
    return new TsDialogInfo( aContext, null, DLG_C_EDIT_OBJ, DLG_T_EDIT_OBJ, 0 );
  }

  /**
   * Creates default dialog info to select entity from list.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @return {@link TsDialogInfo} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static TsDialogInfo forSelectEntity( ITsGuiContext aContext ) {
    return new TsDialogInfo( aContext, null, DLG_C_SELECT_OBJ, DLG_T_SELECT_OBJ, 0 );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private static int percentsInRange( int aPercents ) {
    if( aPercents < MIN_SHELL_RELATIVE_PERCENT ) {
      return MIN_SHELL_RELATIVE_PERCENT;
    }
    if( aPercents > MAX_SHELL_RELATIVE_PERCENT ) {
      return MAX_SHELL_RELATIVE_PERCENT;
    }
    return aPercents;
  }

  // ------------------------------------------------------------------------------------
  // Class API
  //

  // TODO TRANSLATE

  /**
   * Задает минимальный размер панели содержимого диалога.
   *
   * @param aSize {@link ITsPoint} - минимальный размер панели содержимого диалога
   * @throws TsNullArgumentRtException аргумент = null
   */
  public void setMinSize( ITsPoint aSize ) {
    TsNullArgumentRtException.checkNull( aSize );
    minSize = aSize;
  }

  /**
   * Задает максимальный размер панели содержимого диалога.
   *
   * @param aSize {@link ITsPoint} - максимальный размер панели содержимого диалога
   * @throws TsNullArgumentRtException аргумент = null
   */
  public void setMaxSize( ITsPoint aSize ) {
    TsNullArgumentRtException.checkNull( aSize );
    maxSize = aSize;
  }

  /**
   * Задает минимальный размер панели диалога относительно родительского окна.
   * <p>
   * В момент вызова метода должно быть задано родительское окно {@link #shell()}, иначе метод преполагает, что размер
   * родительского окна равен 2/3 от текущего дисплея {@link Display#getCurrent()}. Если текущий дисплей null, то метод
   * ничего не делает.
   * <p>
   * Если аргмуент выходят за пределы {@link #MIN_SHELL_RELATIVE_PERCENT} - {@link #MAX_SHELL_RELATIVE_PERCENT}, то они
   * "загоняются" в указанные пределы.
   *
   * @param aWidthPercent int - ширина панели диалога в процентах от ширины родительского окна
   * @param aHeightPercent - высота панели диалога в процентах от высоты родительского окна
   */
  public void setMinSizeShellRelative( int aWidthPercent, int aHeightPercent ) {
    int shellW, shellH;
    shellW = shell.getBounds().width;
    shellH = shell.getBounds().height;
    int width = shellW * percentsInRange( aWidthPercent ) / 100;
    int height = shellH * percentsInRange( aHeightPercent ) / 100;
    minSize = new TsPoint( width, height );
  }

  /**
   * Задает максимальный размер панели диалога относительно родительского окна.
   * <p>
   * В момент вызова метода должно быть задано родительское окно {@link #shell()}, иначе метод преполагает, что размер
   * родительского окна равен 2/3 от текущего дисплея {@link Display#getCurrent()}. Если текущий дисплей null, то метод
   * ничего не делает.
   * <p>
   * Если аргмуент выходят за пределы {@link #MIN_SHELL_RELATIVE_PERCENT} - {@link #MAX_SHELL_RELATIVE_PERCENT}, то они
   * "загоняются" в указанные пределы.
   *
   * @param aWidthPercent int - ширина панели диалога в процентах от ширины родительского окна
   * @param aHeightPercent - высота панели диалога в процентах от высоты родительского окна
   */
  public void setMaxSizeShellRelative( int aWidthPercent, int aHeightPercent ) {
    int shellW, shellH;
    shellW = shell.getBounds().width;
    shellH = shell.getBounds().height;
    int width = shellW * percentsInRange( aWidthPercent ) / 100;
    int height = shellH * percentsInRange( aHeightPercent ) / 100;
    maxSize = new TsPoint( width, height );
  }

  // ------------------------------------------------------------------------------------
  // IAvObjEditDialogInfo
  //

  @Override
  public String caption() {
    return caption;
  }

  @Override
  public String title() {
    return title;
  }

  @Override
  public int flags() {
    return flags;
  }

  @Override
  public Shell shell() {
    return shell;
  }

  @Override
  public ITsPoint minSize() {
    return minSize;
  }

  @Override
  public ITsPoint maxSize() {
    return maxSize;
  }

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

}
