package org.toxsoft.tsgui.rcp.valed;

import static org.toxsoft.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.tsgui.rcp.valed.ITsResources.*;
import static org.toxsoft.tsgui.rcp.valed.IValedFileConstants.*;
import static org.toxsoft.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.tslib.av.impl.AvUtils.*;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.*;
import org.toxsoft.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.tsgui.graphics.icons.EIconSize;
import org.toxsoft.tsgui.rcp.utils.TsRcpDialogUtils;
import org.toxsoft.tsgui.utils.layout.BorderLayout;
import org.toxsoft.tsgui.valed.api.IValedControl;
import org.toxsoft.tsgui.valed.api.IValedControlConstants;
import org.toxsoft.tsgui.valed.impl.AbstractValedControl;
import org.toxsoft.tsgui.valed.impl.AbstractValedControlFactory;
import org.toxsoft.tslib.av.IAtomicValue;
import org.toxsoft.tslib.bricks.ctx.ITsContextRo;
import org.toxsoft.tslib.bricks.validator.ValidationResult;
import org.toxsoft.tslib.coll.primtypes.IStringList;
import org.toxsoft.tslib.utils.TsLibUtils;
import org.toxsoft.tslib.utils.errors.TsIllegalStateRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.tslib.utils.files.TsFileUtils;

/**
 * File/directory chooser.
 *
 * @author hazard157
 */
public class ValedFile
    extends AbstractValedControl<File, Composite> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".File"; //$NON-NLS-1$

  /**
   * Factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  @SuppressWarnings( "unchecked" )
  static class Factory
      extends AbstractValedControlFactory {

    protected Factory() {
      super( FACTORY_NAME );
    }

    @Override
    protected IValedControl<File> doCreateEditor( ITsGuiContext aContext ) {
      AbstractValedControl<File, Composite> e = new ValedFile( aContext );
      e.setParamIfNull( IValedControlConstants.OPID_IS_HEIGHT_FIXED, AV_TRUE );
      e.setParamIfNull( IValedControlConstants.OPID_IS_WIDTH_FIXED, AV_FALSE );
      return e;
    }

  }

  // ------------------------------------------------------------------------------------
  //

  private Composite board;
  private Text      text;
  private Button    button;

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ValedFile( ITsGuiContext aContext ) {
    super( aContext );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  void selectFileOrDir() {
    String existingPath = TsFileUtils.determineExistingPartOfPath( text.getText() );
    File f;
    if( params().getBool( OPDEF_IS_DIRECTORY ) ) {
      f = TsRcpDialogUtils.askDirOpen( getShell(), existingPath );
    }
    else {
      if( params().getBool( OPDEF_IS_OPEN_DIALOG ) ) {
        IStringList extensions = OPDEF_FILE_EXTENSIONS.getValue( params() ).asValobj();
        f = TsRcpDialogUtils.askFileOpen( getShell(), existingPath, extensions );
      }
      else {
        f = TsRcpDialogUtils.askFileSave( getShell(), existingPath );
      }
    }
    if( f != null ) {
      String newPath = f.getAbsolutePath();
      if( !newPath.equals( existingPath ) ) {
        text.setText( newPath );
      }
    }
  }

  void setButtonIcon() {
    if( button != null ) {
      String iconName = ICONID_FOLDER;
      if( !params().getBool( OPDEF_IS_DIRECTORY ) ) {
        if( params().getBool( OPDEF_IS_OPEN_DIALOG ) ) {
          iconName = ICONID_DOCUMENT_OPEN;
        }
        else {
          iconName = ICONID_DOCUMENT_SAVE;
        }
      }
      button.setImage( iconManager().loadStdIcon( iconName, EIconSize.IS_16X16 ) );
    }
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов базового класса
  //

  @Override
  protected void doSetEditable( boolean aEditable ) {
    if( isWidget() ) {
      text.setEditable( aEditable );
      button.setEnabled( aEditable );
    }
  }

  @Override
  protected Composite doCreateControl( Composite aParent ) {
    board = new Composite( aParent, SWT.NO_FOCUS );
    board.setLayout( new BorderLayout() );
    // text
    text = new Text( board, SWT.BORDER );
    text.setLayoutData( BorderLayout.CENTER );
    text.addModifyListener( notificationModifyListener );
    // button
    button = new Button( board, SWT.PUSH | SWT.FLAT );
    button.setLayoutData( BorderLayout.EAST );
    setButtonIcon();
    button.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        selectFileOrDir();
      }
    } );
    // setup
    text.setEditable( isEditable() );
    button.setEnabled( isEditable() );
    setButtonIcon();
    return board;
  }

  @Override
  public ValidationResult canGetValue() {
    String s = text.getText();
    boolean needDir = params().getBool( OPDEF_IS_DIRECTORY );
    boolean mustExist = params().getBool( OPDEF_MUST_EXIST );
    File f = new File( s );
    if( f.exists() ) {
      if( needDir ) {
        if( !f.isDirectory() ) {
          return ValidationResult.error( FMT_ERR_NOT_A_DIR, f.getPath() );
        }
      }
      else {
        if( !f.isFile() ) {
          return ValidationResult.error( FMT_ERR_NOT_A_FILE, f.getPath() );
        }
      }
    }
    else {
      if( mustExist ) {
        if( needDir ) {
          return ValidationResult.error( FMT_ERR_DIR_NOT_EXISTS, f.getPath() );
        }
        return ValidationResult.error( FMT_ERR_FILE_NOT_EXISTS, f.getPath() );
      }
    }
    return ValidationResult.SUCCESS;
  }

  @Override
  protected File doGetUnvalidatedValue() {
    String s = text.getText();
    if( s.isBlank() ) {
      return null;
    }
    return new File( s );
  }

  @Override
  protected void doSetUnvalidatedValue( File aValue ) {
    if( aValue == null ) {
      text.setText( TsLibUtils.EMPTY_STRING );
    }
    else {
      text.setText( aValue.getAbsolutePath() );
    }
  }

  @Override
  protected void doClearValue() {
    text.setText( TsLibUtils.EMPTY_STRING );
  }

  @Override
  public void onContextOpChanged( ITsContextRo aSource, String aId, IAtomicValue aValue ) {
    setButtonIcon();
  }

  // ------------------------------------------------------------------------------------
  // API для получения данных при прямом использовании контроля
  //

  /**
   * Возвращает имя файла/каталога, введенный в контроль.
   * <p>
   * Метод просто возвращает текстовую строку из контроля, без проверки валидности введенного значения.
   *
   * @return String - имя файла/каталога
   * @throws TsIllegalStateRtException контроль еще не был создан
   */
  public String getFilename() {
    TsIllegalStateRtException.checkNull( text );
    return text.getText();
  }

  /**
   * Задает имя файла/каталога для отображения.
   * <p>
   * Просто задает текст в контроле, без проверки валидности введенного значения.
   *
   * @param aFilename String - имя файла/каталога
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalStateRtException контроль еще не был создан
   */
  public void setFilename( String aFilename ) {
    TsNullArgumentRtException.checkNull( aFilename );
    TsIllegalStateRtException.checkNull( text );
    text.setText( aFilename );
  }

}
