package org.toxsoft.core.tsgui.dialogs.misc;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITsDialogInfo} extension to be used with {@link DialogAskValue}.
 *
 * @author hazard157
 */
public class AskValueDialogInfo
    extends TsDialogInfo
    implements IParameterizedEdit {

  private final IOptionSetEdit       params    = new OptionSet();
  private final IDataType            dataType;
  private ITsValidator<IAtomicValue> validator = ITsValidator.PASS;
  private String                     label     = EMPTY_STRING;

  /**
   * Constructor.
   * <p>
   * {@link #params()} are initialized by {@link IDataType#params()}.
   *
   * @param aContext {@link ITsGuiContext} - GUI context
   * @param aCaption String - window caption
   * @param aTitle String - dialog title area text
   * @param aDataType {@link IDataType} - the type of the asked value
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public AskValueDialogInfo( ITsGuiContext aContext, String aCaption, String aTitle, IDataType aDataType ) {
    super( aContext, aCaption, aTitle );
    TsNullArgumentRtException.checkNull( aDataType );
    dataType = aDataType;
    params.addAll( aDataType.params() );
    label = aDataType.params().getStr( TSID_NAME, EMPTY_STRING );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the type of the asked value.
   *
   * @return {@link IDataType} - the type of the asked value
   */
  public IDataType dataType() {
    return dataType;
  }

  /**
   * Returns text of label from the left side of the value editor.
   * <p>
   * Empty string causes the absence of the label.
   *
   * @return String - label text or an empty string
   */
  public String label() {
    return label;
  }

  /**
   * Sets the label text.
   *
   * @param aLabel String - label text or an empty string
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void setLabel( String aLabel ) {
    label = TsNullArgumentRtException.checkNull( aLabel );
  }

  /**
   * Returns the value validator used to check entered value and enable OK button.
   * <p>
   * If validator is set to <code>null</code>, default atomic validator for the specifief {@link #dataType()} will be
   * instantiated and used.
   * <p>
   * Initial value is <code>null</code>.
   *
   * @return {@link ITsValidator}&lt;{@link IAtomicValue}&gt; - value validator or <code>null</code>
   */
  public ITsValidator<IAtomicValue> validator() {
    return validator;
  }

  /**
   * Sets the {@link #validator()}.
   *
   * @param aValidator {@link ITsValidator}&lt;{@link IAtomicValue}&gt; - value validator or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void setValidator( ITsValidator<IAtomicValue> aValidator ) {
    validator = aValidator;
  }

  // ------------------------------------------------------------------------------------
  // IParameterizedEdit
  //

  /**
   * Parameters used as a VALED creation context options, are initialized by {@link IDataType#params()}.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public IOptionSetEdit params() {
    return params;
  }

}
