package org.toxsoft.core.tsgui.panels.opdefs;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

// TODO TRANSLATE

/**
 * Dialog to edit known options in {@link IOptionSet}.
 *
 * @author hazard157
 */
public class DialogOptionSetEdit
    extends AbstractTsDialogPanel<IOptionSet, ITsGuiContext> {

  PanelOptionSetEdit panel;

  /**
   * Конструктор для встраиваяния в панели.
   * <p>
   *
   * @param aParent {@link Composite} - родительская компонента
   * @param aData {@link IOptionSet} - начальные данные для отображения, может быть null
   * @param aContext {@link ITsGuiContext} - контекст радктирования
   * @param aFlags int - флаги настройки панели, собранные по ИЛИ биты {@link ITsDialogConstants}.DF_XXX
   * @throws TsNullArgumentRtException aParent или aContext = null
   */
  public DialogOptionSetEdit( Composite aParent, IOptionSet aData, ITsGuiContext aContext, int aFlags ) {
    super( aParent, aContext, aData, aContext, aFlags );
    init( aParent );
  }

  protected DialogOptionSetEdit( Composite aParent, TsDialog<IOptionSet, ITsGuiContext> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    init( aParent );
  }

  private void init( Composite aParent ) {
    this.setLayout( new BorderLayout() );
    panel = new PanelOptionSetEdit( tsContext() );
    panel.createControl( aParent );
    panel.getControl().setLayoutData( BorderLayout.CENTER );
    panel.genericChangeEventer().addListener( notificationGenericChangeListener );
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsDialogPanel
  //

  @Override
  protected ValidationResult validateData() {
    return panel.validateValues();
  }

  @Override
  protected void doSetDataRecord( IOptionSet aData ) {
    IOptionSet ops = aData;
    if( aData == null ) {
      ops = IOptionSet.NULL;
    }
    panel.setValues( ops );
  }

  @Override
  protected IOptionSet doGetDataRecord() {
    return panel.getValues();
  }

  // ------------------------------------------------------------------------------------
  // Статическое API
  //

  /**
   * Show modal dialog to edit options.
   *
   * @param aDialogDef {@link ITsDialogInfo} - dialog window properties
   * @param aContext {@link ITsGuiContext} - the context
   * @param aValues {@link IOptionSet} - initial values may be <code>null</code>
   * @param aDefs {@link IList}&lt;{@link IDataDef}&gt; - known (visble) options definitions
   * @return {@link IOptionSet} - edited values or <code>null</code> if user cancelled editing
   */
  public static final IOptionSet edit( ITsDialogInfo aDialogDef, ITsGuiContext aContext, IOptionSet aValues,
      IList<IDataDef> aDefs ) {
    TsNullArgumentRtException.checkNulls( aDialogDef, aContext, aDefs );
    IDialogPanelCreator<IOptionSet, ITsGuiContext> creator = ( aParent, aOwnerDialog ) -> {
      DialogOptionSetEdit d = new DialogOptionSetEdit( aParent, aOwnerDialog );
      d.panel.setOptionDefs( aDefs );
      return d;
    };
    TsDialog<IOptionSet, ITsGuiContext> d = new TsDialog<>( aDialogDef, aValues, aContext, creator );
    return d.execData();
  }

}
