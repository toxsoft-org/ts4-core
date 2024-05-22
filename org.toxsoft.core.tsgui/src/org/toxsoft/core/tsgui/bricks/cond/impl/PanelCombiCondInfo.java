package org.toxsoft.core.tsgui.bricks.cond.impl;

import static org.toxsoft.core.tsgui.bricks.cond.impl.ITsResources.*;
import static org.toxsoft.core.tslib.ITsHardConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.core.tslib.math.lexan.ILexanConstants.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.cond.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.panels.generic.*;
import org.toxsoft.core.tsgui.panels.misc.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.utils.swt.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.math.cond.*;
import org.toxsoft.core.tslib.math.cond.impl.*;
import org.toxsoft.core.tslib.math.lexan.*;
import org.toxsoft.core.tslib.math.lexan.impl.*;
import org.toxsoft.core.tslib.math.logican.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IPanelCombiCondInfo} implementation.
 * <p>
 * Note: it is strongly recommended to set the topic manager with the method
 * {@link #setTopicManager(ITsConditionsTopicManager)} immediately after instance creation.
 *
 * @author hazard157
 */
public class PanelCombiCondInfo
    extends AbstractGenericEntityEditPanel<ITsCombiCondInfo>
    implements IPanelCombiCondInfo {

  /**
   * Singleton of the panel creator {@link IGenericEntityEditPanelCreator} implementation.
   */
  public static final IGenericEntityEditPanelCreator<ITsCombiCondInfo> PANEL_CREATOR = PanelCombiCondInfo::new;

  /**
   * Determines if validation message pane is present in the panel.
   * <p>
   * <i>Type:</i> {@link EAtomicType#BOOLEAN BOOLEAN}
   * <p>
   * <i>Usage:</i> Validation message pane is {@link ValidationResultPanel} displaying the value, returned by
   * {@link IPanelCombiCondInfo#canGetEntity()} on each editing. When option is <code>true</code> - validation message
   * pane is present, <code>false</code> - no pane.
   * <p>
   * <i>Default value:</i> <code>false</code> (no validation message panel)
   */
  public static final IDataDef OPDEF_IS_VALIDATION_PANE =
      DataDef.create( TS_FULL_ID + ".gui.PanelCombiCondInfo.IsValidationPane", //$NON-NLS-1$
          BOOLEAN, TSID_DEFAULT_VALUE, AV_FALSE );

  /**
   * The reference in the context to initialize {@link #topicManager()}.
   */
  public static final ITsGuiContextRefDef<ITsConditionsTopicManager> REFDEF_TOPIC_MANAGER =
      new TsGuiContextRefDef<>( TS_FULL_ID + ".gui.PanelCombiCondInfo.RefTopicManager", //$NON-NLS-1$
          ITsConditionsTopicManager.class, IOptionSet.NULL );

  /**
   * The RGB color of the keyword text background in formula.
   */
  public static final IDataDef OPDEF_KEYWORD_BKG_RGB =
      DataDef.create( TS_FULL_ID + ".gui.PanelCombiCondInfo.KeywordBkgRgb", VALOBJ, //$NON-NLS-1$
          TSID_NAME, STR_KEYWORD_BKG_RGB, //
          TSID_DESCRIPTION, STR_KEYWORD_BKG_RGB_D, //
          TSID_KEEPER_ID, RGBKeeper.KEEPER_ID, //
          TSID_DEFAULT_VALUE, avValobj( new RGB( 220, 220, 255 ) ) //
      );

  /**
   * The RGB color of the keyword text font in formula.
   */
  public static final IDataDef OPDEF_KEYWORD_FG_RGB =
      DataDef.create( TS_FULL_ID + ".gui.PanelCombiCondInfo.KeywordFgRgb", VALOBJ, //$NON-NLS-1$
          TSID_NAME, STR_KEYWORD_FG_RGB, //
          TSID_DESCRIPTION, STR_KEYWORD_FG_RGB_D, //
          TSID_KEEPER_ID, RGBKeeper.KEEPER_ID, //
          TSID_DEFAULT_VALUE, avValobj( ETsColor.BLACK.rgb() ) //
      );

  /**
   * Flags that the keyword text will be displayed by bold font.
   */
  public static final IDataDef OPDEF_KEYWORD_IS_BOLD =
      DataDef.create( TS_FULL_ID + ".gui.PanelCombiCondInfo.KeywordIsBold", BOOLEAN, //$NON-NLS-1$
          TSID_NAME, STR_KEYWORD_IS_BOLD, //
          TSID_DESCRIPTION, STR_KEYWORD_IS_BOLD_D, //
          TSID_KEEPER_ID, RGBKeeper.KEEPER_ID, //
          TSID_DEFAULT_VALUE, AV_TRUE //
      );

  private final LogicalFormulaParser formulaParser = new LogicalFormulaParser();

  private ITsConditionsTopicManager topicManager; // never is null
  private ITsCombiCondInfoBuilder   cciBuilder;   // never is null

  private FormulaTextWidget         textWidget     = null;
  private PanelCcBuilderSinglesList singlesList    = null;
  private ValidationResultPanel     validationPane = null;
  private boolean                   editable       = true;

  private ILogFoNode lfNode = ILogFoNode.NONE;

  /**
   * Constructor.
   * <p>
   * Constructor stores reference to the context, does not creates copy.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aIsViewer boolean - viewer flag, sets {@link #isViewer()} value
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public PanelCombiCondInfo( ITsGuiContext aContext, boolean aIsViewer ) {
    super( aContext, aIsViewer );
    topicManager = REFDEF_TOPIC_MANAGER.getRef( aContext, ITsConditionsTopicManager.NONE );
    cciBuilder = topicManager.newBuilder();
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private void whenTextWidgetChanged() {
    String formulaText = textWidget.getFormulaText();
    lfNode = formulaParser.parse( formulaText );
    cciBuilder.setLogicalFormulaNode( lfNode );
    singlesList.refresh();
    updateValidationVisual();
    fireChangeEvent();
  }

  private void whenSingleListChanged() {
    updateValidationVisual();
    fireChangeEvent();
  }

  private void updateValidationVisual() {
    if( !isControlValid() ) {
      return;
    }
    if( validationPane != null ) {
      ValidationResult vr = cciBuilder.canBuild();
      validationPane.setShownValidationResult( vr );
    }
    updateFormulaHighlights();
  }

  private void updateFormulaHighlights() {
    String text = textWidget.getFormulaText();
    ILogFoNode lfn = formulaParser.parse( text );
    String tooltip;
    int errPos;
    IList<StyleRange> ll;
    if( formulaParser.formulaTokens().isError() ) { // formula has errors
      errPos = LexanUtils.getErrorCharPos( formulaParser.formulaTokens() );
      ILexanToken tk = formulaParser.formulaTokens().firstErrorToken();
      tooltip = tk != null ? tk.str() : EMPTY_STRING;
      ll = IList.EMPTY;
    }
    else { // no errors in formula
      errPos = -1;
      tooltip = lfn.toString();
      ll = makeFormulaHighlights();
    }
    textWidget.setHighlights( errPos, ll );
    textWidget.setToolTipText( tooltip );
  }

  private IList<StyleRange> makeFormulaHighlights() {
    IListEdit<StyleRange> ll = new ElemArrayList<>();
    RGB fgRgb = OPDEF_KEYWORD_FG_RGB.getValue( tsContext().params() ).asValobj();
    RGB bkRgb = OPDEF_KEYWORD_BKG_RGB.getValue( tsContext().params() ).asValobj();
    Color fgColor = colorManager().getColor( fgRgb );
    Color bkColor = colorManager().getColor( bkRgb );
    int fontStyle = SWT.NONE;
    if( OPDEF_KEYWORD_IS_BOLD.getValue( tsContext().params() ).asBool() ) {
      fontStyle = SWT.BOLD;
    }
    int starCharIndex = 0;
    for( int i = 0; i < formulaParser.formulaTokens().tokens().size(); i++ ) {
      ILexanToken tk = formulaParser.formulaTokens().tokens().get( i );
      int subsLen = formulaParser.formulaTokens().subStrings().get( i ).length();
      if( tk.isKind( TKID_KEYWORD ) ) {
        if( subsLen > 0 ) {
          StyleRange r = new StyleRange( starCharIndex, subsLen, fgColor, bkColor, fontStyle );
          ll.add( r );
        }
      }
      starCharIndex += subsLen;
    }
    return ll;
  }

  // ------------------------------------------------------------------------------------
  // AbstractGenericEntityEditPanel
  //

  @Override
  protected ITsCombiCondInfo doGetEntity() {
    return cciBuilder.build();
  }

  @Override
  protected ValidationResult doCanGetEntity() {
    return cciBuilder.canBuild();
  }

  @Override
  protected void doProcessSetEntity() {
    ITsCombiCondInfo ccInf = specifiedEntity() != null ? specifiedEntity() : ITsCombiCondInfo.NEVER;
    cciBuilder.setAsInfo( ccInf );
    TsCombiCondInfoTokenizer tokenizer = new TsCombiCondInfoTokenizer( ccInf );
    textWidget.setFormulaText( tokenizer.getFormulaString() );
    whenTextWidgetChanged();
  }

  @Override
  protected Control doCreateControl( Composite aParent ) {
    Composite board = new Composite( aParent, SWT.NONE );
    board.setLayout( new BorderLayout() );
    // textWidget
    textWidget = new FormulaTextWidget( board, tsContext() );
    textWidget.getControl().setLayoutData( BorderLayout.NORTH );
    // singlesList
    singlesList = new PanelCcBuilderSinglesList( tsContext(), cciBuilder );
    singlesList.createControl( board );
    singlesList.getControl().setLayoutData( BorderLayout.CENTER );
    // validationPane
    boolean isValidationPane = OPDEF_IS_VALIDATION_PANE.getValue( tsContext().params() ).asBool();
    if( isValidationPane ) {
      validationPane = new ValidationResultPanel( board, tsContext() );
      validationPane.setLayoutData( BorderLayout.SOUTH );
    }
    // setup
    textWidget.genericChangeEventer().addListener( e -> whenTextWidgetChanged() );
    singlesList.setCombiCondInfoBuilder( cciBuilder );
    singlesList.genericChangeEventer().addListener( s -> whenSingleListChanged() );
    updateValidationVisual();
    return board;
  }

  // ------------------------------------------------------------------------------------
  // IPanelCombiCondInfo
  //

  @Override
  public boolean isEditable() {
    return editable;
  }

  @Override
  public void setEditable( boolean aEditable ) {
    if( !isViewer() && editable != aEditable ) {
      editable = aEditable;
      textWidget.setEditable( editable );
      singlesList.setEditable( editable );
    }
  }

  @Override
  public ITsConditionsTopicManager topicManager() {
    return cciBuilder != null ? cciBuilder.topicManager() : null;
  }

  @Override
  public void setTopicManager( ITsConditionsTopicManager aTopicManager ) {
    TsNullArgumentRtException.checkNull( aTopicManager );
    cciBuilder = aTopicManager.newBuilder();
    if( isControlValid() ) {
      textWidget.setFormulaText( EMPTY_STRING );
      singlesList.setCombiCondInfoBuilder( cciBuilder );
      updateValidationVisual();
    }
  }

  // ------------------------------------------------------------------------------------
  // Static API
  //

  /**
   * Invokes {@link ITsCombiCondInfo} edit dialog.
   *
   * @param aDialogInfo {@link ITsDialogInfo} - dialog window parameters
   * @param aInitVal {@link ITsCombiCondInfo} - initial value to display or <code>null</code>
   * @param aTopicManager {@link ITsConditionsTopicManager} - single types topic manager
   * @return {@link ITsCombiCondInfo} - edited CCP or <code>null</code> if user cancels editing
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ITsCombiCondInfo edit( ITsDialogInfo aDialogInfo, ITsCombiCondInfo aInitVal,
      ITsConditionsTopicManager aTopicManager ) {
    TsNullArgumentRtException.checkNulls( aDialogInfo, aTopicManager );
    REFDEF_TOPIC_MANAGER.setRef( aDialogInfo.tsContext(), aTopicManager );
    IDialogPanelCreator<ITsCombiCondInfo, ITsConditionsTopicManager> creator =
        ( p, d ) -> new TsDialogGenericEntityEditPanel<>( p, d, PANEL_CREATOR );
    TsDialog<ITsCombiCondInfo, ITsConditionsTopicManager> d =
        new TsDialog<>( aDialogInfo, aInitVal, aTopicManager, creator );
    return d.execData();
  }

}
